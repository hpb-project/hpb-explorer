package com.hpb.bc.rpc;

import com.hpb.bc.event.HpbBlockInfo;
import com.hpb.bc.event.HpbEventHandler;
import com.hpb.bc.exception.ApiException;
import com.hpb.bc.model.EventData;
import com.hpb.bc.model.HpbData;
import com.hpb.bc.model.HpbHash;
import com.hpb.bc.solidity.SmartContractByteCode;
import com.hpb.bc.solidity.SolidityContractDetails;
import com.hpb.bc.solidity.SolidityEvent;
import com.hpb.bc.solidity.SolidityType;
import com.hpb.bc.solidity.abi.AbiParam;
import com.hpb.bc.solidity.converters.SolidityTypeGroup;
import com.hpb.bc.solidity.converters.decoders.SolidityTypeDecoder;
import com.hpb.bc.solidity.converters.decoders.list.CollectionDecoder;
import com.hpb.bc.solidity.converters.encoders.SolidityTypeEncoder;
import com.hpb.bc.solidity.converters.encoders.list.CollectionEncoder;
import com.hpb.bc.solidity.values.*;
import io.hpb.web3.tx.ChainId;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.ReplaySubject;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.http.util.Asserts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.hpb.web3.protocol.core.DefaultBlockParameter;
import com.hpb.bc.solidity.values.Nonce;

import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

import static com.hpb.bc.solidity.values.HpbValue.wei;


/**
 * Created by davidroon on 20.04.16.
 * This code is released under Apache 2 license
 */
public class HpbProxy {
    private static final int ADDITIONAL_GAS_FOR_CONTRACT_CREATION = 15_000;
    private static final int ADDITIONAL_GAS_DIRTY_FIX = 200_000;
    private static final Logger logger = LoggerFactory.getLogger(HpbProxy.class);

    private final ReplaySubject<HpbTransactionRequest> transactionPublisher = ReplaySubject.create(100);
    private final Flowable<HpbTransactionRequest> pendingTransactionObservable = transactionPublisher.toFlowable(BackpressureStrategy.BUFFER);

    private final Map<HpbTransactionRequest, CompletableFuture<HpbTransactionExecutionResult>> futureMap = new ConcurrentHashMap<>();

    private final HpbBackend hpb;
    private final HpbEventHandler eventHandler;
    private final HpbConfig config;
    private final Map<HpbAddress, Set<HpbHash>> pendingTransactions = new HashMap<>();
    private final Map<HpbAddress, Nonce> nonces = new ConcurrentHashMap<>();
    private final Map<SolidityTypeGroup, List<SolidityTypeEncoder>> encoders = new HashMap<>();
    private final Map<SolidityTypeGroup, List<SolidityTypeDecoder>> decoders = new HashMap<>();
    private final List<Class<? extends CollectionDecoder>> listDecoders = new ArrayList<>();
    private final List<Class<? extends CollectionEncoder>> listEncoders = new ArrayList<>();
    private final Set<Class<?>> voidClasses = new HashSet<>();
    private final ExecutorService txExecutor = Executors.newSingleThreadExecutor();
    private final ReentrantLock nonceLock = new ReentrantLock();
    private final ReentrantLock txLock = new ReentrantLock();

    HpbProxy(HpbBackend hpb, HpbEventHandler eventHandler, HpbConfig config) {
        this.hpb = hpb;
        this.eventHandler = eventHandler;
        this.config = config;
        updateNonce();
        hpb.register(eventHandler);
        processTransactions();
    }

    private void processTransactions() {
        pendingTransactionObservable
                .doOnError(err -> logger.error("Error while processing transactions: " + err.getMessage(), err))
                .subscribeOn(Schedulers.from(txExecutor))
                .subscribe(txRequest -> txExecutor.submit(() -> process(txRequest)));
    }

    private void process(HpbTransactionRequest txRequest) {
        try {
            logger.debug("Executing new transaction: " + txRequest.hashCode());
            txLock.lock();
            Nonce nonce = getNonce(txRequest.getAccount().getAddress());
            HpbHash hash = hpb.submit(txRequest, nonce);
            increasePendingTransactionCounter(txRequest.getAccount().getAddress(), hash);
            Optional.ofNullable(futureMap.get(txRequest))
                    .ifPresent(future -> future.complete(new HpbTransactionExecutionResult(hash, nonce)));
            futureMap.remove(txRequest);
        } catch (Throwable t) {
            logger.error("Interrupted error while waiting for transactions to be submitted:", t);
            Optional.ofNullable(futureMap.get(txRequest))
                    .ifPresent(future -> future.completeExceptionally(t));
        } finally {
            txLock.unlock();
        }
    }

    HpbProxy addVoidClass(Class<?> cls) {
        voidClasses.add(cls);
        return this;
    }

    HpbProxy addEncoder(final SolidityTypeGroup typeGroup, final SolidityTypeEncoder encoder) {
        List<SolidityTypeEncoder> encoderList = encoders.computeIfAbsent(typeGroup, key -> new ArrayList<>());
        encoderList.add(0, encoder);
        return this;
    }

    HpbProxy addListDecoder(final Class<? extends CollectionDecoder> decoder) {
        listDecoders.add(0, decoder);
        return this;
    }

    HpbProxy addListEncoder(final Class<? extends CollectionEncoder> decoder) {
        listEncoders.add(0, decoder);
        return this;
    }

    HpbProxy addDecoder(final SolidityTypeGroup typeGroup, final SolidityTypeDecoder decoder) {
        List<SolidityTypeDecoder> decoderList = decoders.computeIfAbsent(typeGroup, key -> new ArrayList<>());
        decoderList.add(0, decoder);
        return this;
    }

    CompletableFuture<HpbAddress> publishWithValue(SolidityContractDetails contract, HpbAccount account, HpbValue value, Object... constructorArgs) {
        return createContractWithValue(contract, account, value, constructorArgs);
    }

    CompletableFuture<HpbAddress> publish(SolidityContractDetails contract, HpbAccount account, Object... constructorArgs) {
        return createContract(contract, account, constructorArgs);
    }

    Nonce getNonce(final HpbAddress address) {
        try {
            nonceLock.lock();
            nonces.computeIfAbsent(address, hpb::getNonce);
            Integer offset = Optional.ofNullable(pendingTransactions.get(address)).map(Set::size).orElse(0);
            return nonces.get(address).add(offset);
        } finally {
            nonceLock.unlock();
        }
    }

    SmartContractByteCode getCode(HpbAddress address) {
        return hpb.getCode(address);
    }

    <T> Observable<T> observeEvents(SolidityEvent<T> eventDefinition, HpbAddress contractAddress) {
        return observeEventsWithInfo(eventDefinition, contractAddress).map(EventInfo::getResult);
    }

    <T> Observable<EventInfo<T>> observeEventsWithInfo(SolidityEvent<T> eventDefinition, HpbAddress contractAddress) {
        Asserts.check(eventDefinition != null, "event definition cannot be null!");
        Asserts.check(contractAddress != null, "contract address cannot be null!");
        return eventHandler.observeBlocks().flatMapIterable(block -> block.receipts)
                .filter(receipt -> contractAddress.equals(receipt.receiveAddress))
                .flatMap(receipt -> {
                    List<EventData> events = receipt.events;
                    return Observable.fromIterable(events.stream().filter(eventDefinition::match)
                            .map(data -> new EventInfo<>(receipt.hash, eventDefinition.parseEvent(data)))
                            .collect(Collectors.toList()));
                });
    }

    private CompletableFuture<HpbAddress> publishContract(HpbValue ethValue, HpbData data, HpbAccount account) {
        return this.sendTxInternal(ethValue, data, account, HpbAddress.empty())
                .thenCompose(CallDetails::getResult)
                .thenApply(receipt -> receipt.contractAddress);
    }

    CompletableFuture<CallDetails> sendTx(HpbValue value, HpbData data, HpbAccount account, HpbAddress address) {
        return this.sendTxInternal(value, data, account, address);
    }

    public SmartContract getSmartContract(SolidityContractDetails details, HpbAddress address, HpbAccount account) {
        return new SmartContract(details, account, address, this, hpb);
    }

    private CompletableFuture<HpbAddress> createContract(SolidityContractDetails contract, HpbAccount account, Object... constructorArgs) {
        return createContractWithValue(contract, account, wei(0), constructorArgs);
    }

    private CompletableFuture<HpbAddress> createContractWithValue(SolidityContractDetails contract, HpbAccount account, HpbValue value, Object... constructorArgs) {
        HpbData argsEncoded = new SmartContract(contract, account, HpbAddress.empty(), this, hpb)
                .getConstructor(constructorArgs)
                .map(constructor -> constructor.encode(constructorArgs))
                .orElseGet(() -> {
                    if (constructorArgs.length > 0) {
                        throw new ApiException("No constructor found with params (" + printTypes(constructorArgs) + ")");
                    }
                    return HpbData.empty();
                });
        return publishContract(value, HpbData.of(ArrayUtils.addAll(contract.getBinary().data, argsEncoded.data)), account);

    }

    private String printTypes(Object[] constructorArgs) {
        return Arrays.stream(constructorArgs).map(arg -> {
            if (arg == null) {
                return "null";
            } else {
                return arg.getClass().getSimpleName();
            }
        }).reduce((a, b) -> a + ", " + b).orElse("[no args]");
    }

    private CompletableFuture<HpbTransactionExecutionResult> submitTransaction(HpbTransactionRequest txRequest) {
        if (futureMap.containsKey(txRequest)) {
            return futureMap.get(txRequest);
        }
        CompletableFuture<HpbTransactionExecutionResult> future = new CompletableFuture<>();
        logger.debug("Accepted transaction " + txRequest.hashCode());
        transactionPublisher.onNext(txRequest);
        futureMap.put(txRequest, future);
        return future;
    }

    private CompletableFuture<CallDetails> sendTxInternal(HpbValue value, HpbData data, HpbAccount account, HpbAddress toAddress) {
        return eventHandler.ready().thenCompose(v -> {
            GasUsage gasLimit = estimateGas(value, data, account, toAddress);
            /* GasPrice gasPrice = hpb.getGasPrice();*/

            return submitTransaction(new HpbTransactionRequest(account, toAddress, value, data, gasLimit))
                    .thenApply(executionResult -> new CallDetails(this.waitForResult(executionResult.transactionHash), executionResult.transactionHash, executionResult.nonce, gasLimit));
        });
    }

    private CompletableFuture<HpbTransactionReceipt> waitForResult(HpbHash txHash) {
        Objects.requireNonNull(txHash);
        long currentBlock = eventHandler.getCurrentBlockNumber();

        Flowable<HpbTransactionInfo> droppedTxs = eventHandler.observeTransactions()
                .toFlowable(BackpressureStrategy.BUFFER)
                .filter(params -> params.getReceipt().map(receipt -> Objects.equals(receipt.hash, txHash))
                        .orElse(false) && params.getStatus() == HpbTransactionStatus.Dropped);

        Flowable<HpbTransactionInfo> timeoutBlock = eventHandler.observeBlocks()
                .toFlowable(BackpressureStrategy.BUFFER)
                .filter(blockParams -> blockParams.blockNumber > currentBlock + config.blockWaitLimit())
                .map(blockInfo -> new EmptyHpbTransactionInfo());

        Flowable<HpbTransactionInfo> blockTxs = eventHandler.observeBlocks()
                .toFlowable(BackpressureStrategy.BUFFER)
                .map(block -> hpb.getTransactionInfo(txHash))
                .map(optInfo -> optInfo.flatMap(HpbTransactionInfo::getReceipt))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(this::createTransactionParameters);

        CompletableFuture<HpbTransactionReceipt> futureResult = new CompletableFuture<>();

        Flowable
                .merge(droppedTxs, blockTxs, timeoutBlock)
                .filter(txInfo -> !(txInfo instanceof EmptyHpbTransactionInfo))
                .map(params -> {
                    if (params == null) {
                        throw new ApiException("the transaction has not been included in the last " + config.blockWaitLimit() + " blocks");
                    }
                    HpbTransactionReceipt receipt = params.getReceipt().orElseThrow(() -> new ApiException("no Transaction receipt found!"));
                    if (params.getStatus() == HpbTransactionStatus.Dropped) {
                        throw new ApiException("the transaction has been dropped! - " + receipt.error);
                    }
                    Optional<HpbTransactionReceipt> result = checkForErrors(receipt);
                    return result.orElseThrow(() -> new ApiException("error with the transaction " + receipt.hash + ". error:" + receipt.error));
                })
                .first(new EmptyHpbTransactionReceipt())
                .subscribe(futureResult::complete, futureResult::completeExceptionally);

        return futureResult;
    }

    public GasUsage estimateGas(HpbValue value, HpbData data, HpbAccount account, HpbAddress toAddress) {
        GasUsage gasLimit = hpb.estimateGas(account, toAddress, value, data);
        //if it is a contract creation
        if (toAddress.isEmpty()) {
            gasLimit = gasLimit.add(ADDITIONAL_GAS_FOR_CONTRACT_CREATION);
        }
        return gasLimit.add(ADDITIONAL_GAS_DIRTY_FIX);
    }

    public Set<HpbHash> getPendingTransactions(HpbAddress address) {
        return pendingTransactions.get(address);
    }

    private HpbTransactionInfo createTransactionParameters(HpbTransactionReceipt receipt) {
        return new HpbTransactionInfo(receipt.hash, receipt, HpbTransactionStatus.Executed, receipt.blockHash);
    }

    private Optional<HpbTransactionReceipt> checkForErrors(final HpbTransactionReceipt receipt) {
        if (receipt.isSuccessful) {
            return Optional.of(receipt);
        } else {
            return Optional.empty();
        }
    }

    private void updateNonce() {
        observeTransactions();
        observeBlocks();
    }

    private void observeTransactions() {
        eventHandler.observeTransactions()
                .toFlowable(BackpressureStrategy.BUFFER)
                .subscribeOn(Schedulers.trampoline())
                .filter(tx -> tx.getStatus() == HpbTransactionStatus.Dropped)
                .subscribe(params -> {
                    HpbTransactionReceipt receipt = params.getReceipt().orElseThrow(() -> new ApiException("no Transaction receipt found!"));
                    HpbAddress currentAddress = receipt.sender;
                    HpbHash hash = receipt.hash;
                    nonceLock.lock();
                    Optional.ofNullable(pendingTransactions.get(currentAddress)).ifPresent(hashes -> {
                        hashes.remove(hash);
                        nonces.put(currentAddress, hpb.getNonce(currentAddress));
                    });
                    nonceLock.unlock();
                }, err -> logger.error("Error while observing transactions: " + err.getMessage(), err));
    }

    private void observeBlocks() {
        eventHandler.observeBlocks()
                .toFlowable(BackpressureStrategy.BUFFER)
                .subscribeOn(Schedulers.trampoline())
                .subscribe(params -> {
                    nonceLock.lock();
                    params.receipts
                            .forEach(receipt -> Optional.ofNullable(pendingTransactions.get(receipt.sender))
                                    .ifPresent(hashes -> {
                                        hashes.remove(receipt.hash);
                                        nonces.put(receipt.sender, hpb.getNonce(receipt.sender));
                                    }));
                    nonceLock.unlock();
                }, err -> logger.error("Error while observing blocks: " + err.getMessage(), err));
    }

    HpbEventHandler events() {
        return eventHandler;
    }

    boolean addressExists(final HpbAddress address) {
        return hpb.addressExists(address);
    }

    HpbValue getBalance(final HpbAddress address) {
        return hpb.getBalance(address);
    }

    private void increasePendingTransactionCounter(HpbAddress address, HpbHash hash) {
        Set<HpbHash> hashes = pendingTransactions.computeIfAbsent(address, (key) -> Collections.synchronizedSet(new HashSet<>()));
        hashes.add(hash);
        pendingTransactions.put(address, hashes);
    }

    List<SolidityTypeEncoder> getEncoders(AbiParam abiParam) {
        SolidityType type = SolidityType.find(abiParam.getType())
                .orElseThrow(() -> new ApiException("unknown type " + abiParam.getType()));
        if (abiParam.isArray()) {
            return listEncoders.stream().map(cls -> {
                try {
                    if (abiParam.isDynamic()) {
                        return cls.getConstructor(List.class).newInstance(getEncoders(type, abiParam));
                    }
                    return cls.getConstructor(List.class, Integer.class).newInstance(getEncoders(type, abiParam), abiParam.getArraySize());
                } catch (InstantiationException | NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
                    throw new ApiException("error while preparing list encoders", e);
                }
            }).collect(Collectors.toList());
        }
        return getEncoders(type, abiParam);
    }

    private List<SolidityTypeEncoder> getEncoders(final SolidityType type, AbiParam abiParam) {
        return Optional.ofNullable(encoders.get(SolidityTypeGroup.resolveGroup(type)))
                .orElseThrow(() -> new ApiException("no encoder found for solidity type " + abiParam.getType()));
    }

    List<SolidityTypeDecoder> getDecoders(AbiParam abiParam) {
        SolidityType type = SolidityType.find(abiParam.getType())
                .orElseThrow(() -> new ApiException("unknown type " + abiParam.getType()));

        SolidityTypeGroup typeGroup = SolidityTypeGroup.resolveGroup(type);

        if (abiParam.isArray() || type.equals(SolidityType.BYTES)) {
            return listDecoders.stream().map(cls -> {
                try {
                    if (abiParam.isDynamic()) {
                        return cls.getConstructor(List.class).newInstance(decoders.get(typeGroup));
                    }
                    return cls.getConstructor(List.class, Integer.class).newInstance(decoders.get(typeGroup), abiParam.getArraySize());
                } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
                    throw new ApiException("error while creating a List decoder", e);
                }
            }).collect(Collectors.toList());
        }

        return Optional.ofNullable(decoders.get(typeGroup))
                .orElseThrow(() -> new ApiException("no decoder found for solidity type " + abiParam.getType()));
    }

    public <T> boolean isVoidType(Class<T> cls) {
        return voidClasses.contains(cls);
    }

    public <T> List<T> getEventsAtBlock(SolidityEvent<T> eventDefinition, HpbAddress address, Long blockNumber) {
        return hpb.getBlock(blockNumber).map(block -> getEventsAtBlock(eventDefinition, address, block)).orElseGet(ArrayList::new);
    }

    public <T> List<T> getEventsAtBlock(SolidityEvent<T> eventDefinition, HpbAddress address, HpbHash blockHash) {
        return hpb.getBlock(blockHash).map(block -> getEventsAtBlock(eventDefinition, address, block)).orElseGet(ArrayList::new);
    }

    private <T> List<T> getEventsAtBlock(SolidityEvent<T> eventDefinition, HpbAddress address, HpbBlockInfo blockInfo) {
        return getEventsAtBlockWithInfo(eventDefinition, address, blockInfo).stream()
                .map(EventInfo::getResult)
                .collect(Collectors.toList());
    }

    public <T> List<EventInfo<T>> getEventsAtBlockWithInfo(SolidityEvent<T> eventDefinition, HpbAddress address, Long blockNumber) {
        return hpb.getBlock(blockNumber).map(block -> getEventsAtBlockWithInfo(eventDefinition, address, block)).orElseGet(ArrayList::new);
    }

    public <T> List<EventInfo<T>> getEventsAtBlockWithInfo(SolidityEvent<T> eventDefinition, HpbAddress address, HpbHash blockHash) {
        return hpb.getBlock(blockHash).map(block -> getEventsAtBlockWithInfo(eventDefinition, address, block)).orElseGet(ArrayList::new);
    }

    private <T> List<EventInfo<T>> getEventsAtBlockWithInfo(SolidityEvent<T> eventDefinition, HpbAddress address, HpbBlockInfo blockInfo) {
        return blockInfo.receipts.stream()
                .filter(params -> address.equals(params.receiveAddress))
                .flatMap(params -> params.events.stream())
                .filter(eventDefinition::match)
                .map(data -> new EventInfo<>(data.getTransactionHash(), eventDefinition.parseEvent(data)))
                .collect(Collectors.toList());
    }


    public <T> List<T> getEventsAtTransaction(SolidityEvent<T> eventDefinition, HpbAddress address, HpbHash transactionHash) {
        return getEventsAtTransactionWithInfo(eventDefinition, address, transactionHash).stream()
                .map(EventInfo::getResult).collect(Collectors.toList());
    }

    public <T> List<EventInfo<T>> getEventsAtTransactionWithInfo(SolidityEvent<T> eventDefinition, HpbAddress address, HpbHash transactionHash) {
        HpbTransactionReceipt receipt = hpb.getTransactionInfo(transactionHash).flatMap(HpbTransactionInfo::getReceipt).orElseThrow(() -> new ApiException("no Transaction receipt found!"));
        if (address.equals(receipt.receiveAddress)) {
            return receipt.events.stream().filter(eventDefinition::match)
                    .map(data -> new EventInfo<>(data.getTransactionHash(), eventDefinition.parseEvent(data)))
                    .collect(Collectors.toList());
        }

        return new ArrayList<>();
    }

    public List<EventData> getLogs(DefaultBlockParameter fromBlock, DefaultBlockParameter toBlock, SolidityEvent eventDefiniton, HpbAddress address, String... optionalTopics) {
        return hpb.logCall(fromBlock, toBlock, eventDefiniton, address, optionalTopics);
    }

    public long getCurrentBlockNumber() {
        return eventHandler.getCurrentBlockNumber();
    }

    public Optional<HpbTransactionInfo> getHpbTransactionInfo(HpbHash hash) {
        return hpb.getTransactionInfo(hash);
    }

    public ChainId getChainId() {
//        return hpb.getChainId();
        return null;
    }
}
