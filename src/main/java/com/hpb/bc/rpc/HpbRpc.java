package com.hpb.bc.rpc;

import com.hpb.bc.event.HpbBlockInfo;
import com.hpb.bc.event.HpbEventHandler;
import com.hpb.bc.model.EventData;
import com.hpb.bc.model.HpbData;
import com.hpb.bc.model.HpbHash;
import com.hpb.bc.solidity.SmartContractByteCode;
import com.hpb.bc.solidity.SolidityEvent;
import com.hpb.bc.solidity.values.*;


import io.hpb.web3.protocol.core.methods.response.HpbBlock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.hpb.web3.protocol.core.DefaultBlockParameter;

import io.hpb.web3.protocol.core.methods.response.Log;
import io.hpb.web3.protocol.core.methods.response.Transaction;

import java.util.*;
import java.util.stream.Collectors;


public class HpbRpc implements HpbBackend {
    private static final Logger logger = LoggerFactory.getLogger(HpbRpc.class);

    private final Web3JFacade web3JFacade;
    private final HpbRpcEventGenerator ethereumRpcEventGenerator;
    private final ChainId chainId;


    public HpbRpc(Web3JFacade web3JFacade, ChainId chainId, HpbRpcConfig config) {
        this.web3JFacade = web3JFacade;
        this.ethereumRpcEventGenerator = new HpbRpcEventGenerator(web3JFacade, config, this);

        this.chainId = chainId;
    }

    @Override
    public HpbValue getBalance(HpbAddress address) {
        return HpbValue.wei(web3JFacade.getBalance(address).getBalance());
    }

    @Override
    public boolean addressExists(HpbAddress address) {
        return web3JFacade.getTransactionCount(address).intValue() > 0 || web3JFacade.getBalance(address).getBalance().intValue() > 0 || !web3JFacade.getCode(address).isEmpty();
    }

    @Override
    public HpbHash submit(HpbTransactionRequest request, Nonce nonce) {
         
        return null;
    }



    @Override
    public GasUsage estimateGas(HpbAccount account, HpbAddress address, HpbValue value, HpbData data) {
        return new GasUsage(web3JFacade.estimateGas(account, address, value, data));
    }

    @Override
    public Nonce getNonce(HpbAddress currentAddress) {
        return new Nonce(web3JFacade.getTransactionCount(currentAddress));
    }

    @Override
    public long getCurrentBlockNumber() {
        return web3JFacade.getCurrentBlockNumber();
    }

    @Override
    public Optional<HpbBlockInfo> getBlock(long number) {
        return web3JFacade.getBlock(number).map(this::toBlockInfo);
    }

    @Override
    public Optional<HpbBlockInfo> getBlock(HpbHash ethHash) {
        return web3JFacade.getBlock(ethHash).map(this::toBlockInfo);
    }

    @Override
    public SmartContractByteCode getCode(HpbAddress address) {
        return web3JFacade.getCode(address);
    }

    @Override
    public HpbData constantCall(HpbAccount account, HpbAddress address, HpbValue value, HpbData data) {
        return web3JFacade.constantCall(account, address, data);
    }

    @Override
    public List<EventData> logCall(DefaultBlockParameter fromBlock, DefaultBlockParameter toBlock, SolidityEvent eventDefinition, HpbAddress address, String... optionalTopics) {
        return web3JFacade.loggingCall(fromBlock, toBlock, eventDefinition, address, optionalTopics).stream().map(log -> toEventInfo(HpbHash.of(log.getTransactionHash()), log)).collect(Collectors.toList());
    }

    @Override
    public void register(HpbEventHandler eventHandler) {
        ethereumRpcEventGenerator.addListener(eventHandler);
    }

    @Override
    public Optional<HpbTransactionInfo> getTransactionInfo(HpbHash hash) {
        return Optional.ofNullable(web3JFacade.getReceipt(hash))
                .filter(web3jReceipt -> web3jReceipt.getBlockHash() != null)
                .flatMap(web3jReceipt -> Optional.ofNullable(web3JFacade.getTransaction(hash))
                        .map(transaction -> {
                            HpbTransactionReceipt receipt = toReceipt(transaction, web3jReceipt);
                            HpbTransactionStatus status = transaction.getBlockHash().isEmpty() ? HpbTransactionStatus.Unknown : HpbTransactionStatus.Executed;
                            return new HpbTransactionInfo(hash, receipt, status, HpbHash.of(transaction.getBlockHash()));
                        })
                );
    }

    @Override
    public ChainId getChainId() {
        return chainId;
    }

    HpbBlockInfo toBlockInfo(HpbBlock ethBlock) {
        return Optional.ofNullable(ethBlock.getBlock()).map(block -> {
            try {
                Map<String, HpbBlock.TransactionObject> txObjects = block.getTransactions().stream()
                        .map(tx -> (HpbBlock.TransactionObject) tx.get()).collect(Collectors.toMap(HpbBlock.TransactionObject::getHash, e -> e));

                Map<String, io.hpb.web3.protocol.core.methods.response.TransactionReceipt> receipts = txObjects.values().stream()
                        .map(tx -> Optional.ofNullable(web3JFacade.getReceipt(HpbHash.of(tx.getHash()))))
                        .filter(Optional::isPresent)
                        .map(Optional::get)
                        .filter(web3jReceipt -> web3jReceipt.getBlockHash() != null)
                        .collect(Collectors.toMap(io.hpb.web3.protocol.core.methods.response.TransactionReceipt::getTransactionHash, e -> e));

                List<HpbTransactionReceipt> receiptList = receipts.entrySet().stream()
                        .map(entry -> toReceipt(txObjects.get(entry.getKey()), entry.getValue())).collect(Collectors.toList());

                return new HpbBlockInfo(block.getNumber().longValue(), receiptList);
            } catch (Throwable ex) {
                logger.error("error while converting to block info", ex);
                return new HpbBlockInfo(block.getNumber().longValue(), Collections.emptyList());
            }
        }).orElseGet(() -> new HpbBlockInfo(-1, new ArrayList<>()));
    }

    private HpbTransactionReceipt toReceipt(Transaction tx, io.hpb.web3.protocol.core.methods.response.TransactionReceipt receipt) {
        boolean successful = !receipt.getGasUsed().equals(tx.getGas());
        String error = "";
        if (!successful) {
            error = "All the gas was used! an error occurred";
        }

        return new HpbTransactionReceipt(HpbHash.of(receipt.getTransactionHash()), HpbHash.of(receipt.getBlockHash()), HpbAddress.of(receipt.getFrom()), HpbAddress.of(receipt.getTo()), HpbAddress.of(receipt.getContractAddress()), HpbData.of(tx.getInput()), error, HpbData.empty(), successful, toEventInfos(HpbHash.of(receipt.getTransactionHash()), receipt.getLogs()), HpbValue.wei(tx.getValue()));
    }

    private List<EventData> toEventInfos(HpbHash transactionHash, List<Log> logs) {
        return logs.stream().map(log -> this.toEventInfo(transactionHash, log)).collect(Collectors.toList());
    }

    private EventData toEventInfo(HpbHash transactionHash, Log log) {
        List<HpbData> topics = log.getTopics().stream().map(HpbData::of).collect(Collectors.toList());
        if (topics.size() > 0) {
            HpbData eventSignature = topics.get(0);
            HpbData eventArguments = HpbData.of(log.getData());
            return new EventData(transactionHash, eventSignature, eventArguments, topics.subList(1, topics.size()));
        } else {
            return new EventData(transactionHash, HpbData.empty(), HpbData.empty(), new ArrayList<>());
        }
    }
}
