package com.hpb.bc.rpc;

import java.io.IOError;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Executors;

import com.hpb.bc.exception.ApiException;
import com.hpb.bc.model.HpbData;
import com.hpb.bc.model.HpbHash;
import com.hpb.bc.solidity.SmartContractByteCode;
import com.hpb.bc.solidity.SolidityEvent;
import com.hpb.bc.solidity.values.*;
import io.hpb.web3.protocol.Web3;
import io.hpb.web3.protocol.core.methods.response.HpbBlock;
import io.hpb.web3.protocol.core.methods.request.HpbFilter;
import io.hpb.web3.protocol.core.methods.response.HpbGetBalance;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.hpb.web3.protocol.core.DefaultBlockParameter;
import io.hpb.web3.protocol.core.DefaultBlockParameterName;
import io.hpb.web3.protocol.core.DefaultBlockParameterNumber;
import io.hpb.web3.protocol.core.Response;
import io.hpb.web3.protocol.core.methods.request.Transaction;
import io.hpb.web3.protocol.core.methods.response.Log;
import io.hpb.web3.protocol.core.methods.response.TransactionReceipt;
import io.hpb.web3.utils.Numeric;
import io.reactivex.Observable;

/**
 * Created by davidroon on 19.11.16.
 * This code is released under Apache 2 license
 */
public class Web3JFacade {
    private static final Logger logger = LoggerFactory.getLogger(Web3JFacade.class);
    private final Web3 web3;
    private final Web3jBlockHandler blockEventHandler = new Web3jBlockHandler();
    private BigInteger lastBlockNumber = BigInteger.ZERO;

    public Web3JFacade(final Web3 web3) {
        this.web3 = web3;
    }

    HpbData constantCall(final HpbAccount account, final HpbAddress address, final HpbData data) {
        try {
            return HpbData.of(handleError(web3.hpbCall(new Transaction(
                    account.getAddress().normalizedWithLeading0x(),
                    null,
                    null,
                    null,
                    address.normalizedWithLeading0x(),
                    BigInteger.ZERO,
                    data.toString()
            ), DefaultBlockParameterName.LATEST).send()));
        } catch (IOException e) {
            throw new IOError(e);
        }
    }

    List<Log> loggingCall(DefaultBlockParameter fromBlock, DefaultBlockParameter toBlock, SolidityEvent eventDefiniton, final HpbAddress address, final String... optionalTopics) {
        HpbFilter ethFilter = new HpbFilter(fromBlock, toBlock, address.withLeading0x());
        ethFilter.addSingleTopic(eventDefiniton.getDescription().signatureLong().withLeading0x());
        ethFilter.addOptionalTopics(optionalTopics);
        List<Log> list = new ArrayList<>();
//        this.web3.hpbLogFlowable(ethFilter).subscribe(list::add).dispose();
        return list;
    }

    BigInteger getTransactionCount(HpbAddress address) {
        try {
            return Numeric.decodeQuantity(handleError(web3.hpbGetTransactionCount(address.normalizedWithLeading0x(), DefaultBlockParameterName.LATEST).send()));
        } catch (IOException e) {
            throw new IOError(e);
        }
    }

/*    Flowable<HpbBlock> observeBlocks() {
//        return web3.blockFlowable(true);
//        return null;
    }*/

    rx.Observable<HpbBlock> observeBlocks() {
        return web3.blockObservable(true);

    }

    Observable<HpbBlock> observeBlocksPolling(long pollingFrequence) {
        Executors.newCachedThreadPool().submit(() -> {
            while (true) {
                try {
                    HpbBlock currentBlock = web3.hpbGetBlockByNumber(DefaultBlockParameter.valueOf(DefaultBlockParameterName.LATEST.name()), true).send();
                    BigInteger currentBlockNumber = currentBlock.getBlock().getNumber();

                    if (currentBlockNumber.compareTo(this.lastBlockNumber) > 0) {

                        //Set last block to current block -1 in case last block is zero to prevent all blocks from being retrieved
                        if (this.lastBlockNumber.equals(BigInteger.ZERO)) {
                            this.lastBlockNumber = currentBlockNumber.subtract(BigInteger.ONE);
                        }

                        //In case the block number of the current block is more than 1 higher than the last block, retrieve intermediate blocks
                        for (BigInteger i = this.lastBlockNumber.add(BigInteger.ONE); i.compareTo(currentBlockNumber) < 0; i = i.add(BigInteger.ONE)) {
                            HpbBlock missedBlock = web3.hpbGetBlockByNumber(DefaultBlockParameter.valueOf(i), true).send();
                            this.lastBlockNumber = i;
                            blockEventHandler.newElement(missedBlock);
                        }

                        this.lastBlockNumber = currentBlockNumber;
                        blockEventHandler.newElement(currentBlock);
                    }
                } catch (Throwable e) {
                    logger.warn("error while polling blocks", e);
                }
                Thread.sleep(pollingFrequence);
            }
        });
        return blockEventHandler.observable;
    }

    BigInteger estimateGas(HpbAccount account, HpbAddress address, HpbValue value, HpbData data) {
        try {
            return Numeric.decodeQuantity(handleError(web3.hpbEstimateGas(new Transaction(account.getAddress().normalizedWithLeading0x(), null, null, null,
                    address.isEmpty() ? null : address.normalizedWithLeading0x(), value.inWei(), data.toString())).send()));
        } catch (IOException e) {
            throw new IOError(e);
        }
    }

    GasPrice getGasPrice() {
        try {
            return new GasPrice(HpbValue.wei(Numeric.decodeQuantity(handleError(web3.hpbGasPrice().send()))));
        } catch (IOException e) {
            throw new IOError(e);
        }
    }

    HpbHash sendTransaction(final HpbData rawTransaction) {
        try {
            return HpbHash.of(handleError(web3.hpbSendRawTransaction(rawTransaction.withLeading0x()).send()));
        } catch (IOException e) {
            throw new IOError(e);
        }
    }

    public HpbGetBalance getBalance(HpbAddress address) {
        try {
            return web3.hpbGetBalance(address.normalizedWithLeading0x(), DefaultBlockParameterName.LATEST).send();
        } catch (IOException e) {
            throw new IOError(e);
        }
    }

    private <S, T extends Response<S>> S handleError(final T response) {
        if (response.hasError()) {
            throw new ApiException(response.getError().getMessage());
        }
        return response.getResult();
    }

    SmartContractByteCode getCode(HpbAddress address) {
        try {
            return SmartContractByteCode.of(web3.hpbGetCode(address.normalizedWithLeading0x(), DefaultBlockParameterName.LATEST).send().getCode());
        } catch (IOException e) {
            throw new IOError(e);
        }
    }

    long getCurrentBlockNumber() {
        try {
            return web3.hpbBlockNumber().send().getBlockNumber().longValue();
        } catch (IOException e) {
            throw new IOError(e);
        }
    }

    TransactionReceipt getReceipt(HpbHash hash) {
        try {
            return handleError(web3.hpbGetTransactionReceipt(hash.withLeading0x()).send());
        } catch (IOException e) {
            throw new ApiException("error while retrieving the transactionReceipt", e);
        }
    }

    io.hpb.web3.protocol.core.methods.response.Transaction getTransaction(HpbHash hash) {
        try {
            return handleError(web3.hpbGetTransactionByHash(hash.withLeading0x()).send());
        } catch (IOException e) {
            throw new ApiException("error while retrieving the transactionReceipt", e);
        }
    }

    Optional<HpbBlock> getBlock(long blockNumber) {
        try {
            return Optional.ofNullable(web3.hpbGetBlockByNumber(new DefaultBlockParameterNumber(BigInteger.valueOf(blockNumber)), true).send());
        } catch (IOException e) {
            throw new ApiException("error while retrieving the block " + blockNumber, e);
        }
    }

    Optional<HpbBlock> getBlock(HpbHash blockHash) {
        try {
            return Optional.ofNullable(web3.hpbGetBlockByHash(blockHash.withLeading0x(), true).send());
        } catch (IOException e) {
            throw new ApiException("error while retrieving the block " + blockHash.withLeading0x(), e);
        }
    }
}
