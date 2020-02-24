package com.hpb.bc.propeller;

import com.hpb.bc.propeller.model.HpbStatediffbyblockandTx;
import io.hpb.web3.protocol.Web3Service;
import io.hpb.web3.protocol.core.DefaultBlockParameter;
import io.hpb.web3.protocol.core.JsonRpc2_0Web3;
import io.hpb.web3.protocol.core.Request;
import io.hpb.web3.protocol.core.methods.request.ShhFilter;
import io.hpb.web3.protocol.core.methods.request.Transaction;
import io.hpb.web3.protocol.core.methods.response.*;
import io.hpb.web3.protocol.websocket.events.LogNotification;
import io.hpb.web3.protocol.websocket.events.NewHeadsNotification;
import io.hpb.web3.utils.Numeric;
import org.apache.commons.lang3.StringUtils;
/*import rx.Observable;*/

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;

public class HpbPropeller extends JsonRpc2_0Web3 {

    public Request<?, HpbStatediffbyblockandTx> hpbGetStatediffbyblockandTxByBlockNumberOrTxHash(BigInteger blockNumber, String txHash) {
        List<String> stringList = new ArrayList<>();
        if (blockNumber == null && StringUtils.isNotEmpty(txHash)) {

            stringList = Arrays.asList("", txHash);
        } else if (blockNumber != null && StringUtils.isEmpty(txHash)) {
            stringList = Arrays.asList(Numeric.toHexStringWithPrefixSafe(blockNumber), "");
        } else if (blockNumber != null && StringUtils.isNotEmpty(txHash)) {
            stringList = Arrays.asList(Numeric.toHexStringWithPrefixSafe(blockNumber), txHash);
        }

        return new Request<>("hpb_getStatediffbyblockandTx", stringList, web3Service, HpbStatediffbyblockandTx.class);
    }


    public Request<?, HpbStatediffbyblockandTx> hpbGetStatediffbyblockandTxByBlockHashOrTxHash(String blockHash, String txHash) {
        List<String> stringList = Arrays.asList(blockHash, txHash);
        return new Request<>("hpb_getStatediffbyblockandTx", stringList, web3Service, HpbStatediffbyblockandTx.class);
    }


    public Request<?, HpbStatediffbyblockandTx> hpbGetStatediffbyblock(BigInteger blockNumber) {
        return new Request<>("hpb_getStatediffbyblock", Arrays.asList(Numeric.toHexStringWithPrefixSafe(blockNumber)), web3Service, HpbStatediffbyblockandTx.class);
    }

    public HpbPropeller(Web3Service web3Service) {
        super(web3Service);
    }

    public HpbPropeller(Web3Service web3Service, long pollingInterval, ScheduledExecutorService scheduledExecutorService) {
        super(web3Service, pollingInterval, scheduledExecutorService);
    }

    @Override
    public Request<?, Web3ClientVersion> web3ClientVersion() {
        return super.web3ClientVersion();
    }

    @Override
    public Request<?, Web3Sha3> web3Sha3(String data) {
        return super.web3Sha3(data);
    }

    @Override
    public Request<?, NetVersion> netVersion() {
        return super.netVersion();
    }

    @Override
    public Request<?, NetListening> netListening() {
        return super.netListening();
    }

    @Override
    public Request<?, NetPeerCount> netPeerCount() {
        return super.netPeerCount();
    }

    @Override
    public Request<?, HpbProtocolVersion> hpbProtocolVersion() {
        return super.hpbProtocolVersion();
    }

    @Override
    public Request<?, HpbCoinbase> hpbCoinbase() {
        return super.hpbCoinbase();
    }

    @Override
    public Request<?, HpbSyncing> hpbSyncing() {
        return super.hpbSyncing();
    }

    @Override
    public Request<?, HpbMining> hpbMining() {
        return super.hpbMining();
    }

    @Override
    public Request<?, HpbHashrate> hpbHashrate() {
        return super.hpbHashrate();
    }

    @Override
    public Request<?, HpbGasPrice> hpbGasPrice() {
        return super.hpbGasPrice();
    }

    @Override
    public Request<?, HpbAccounts> hpbAccounts() {
        return super.hpbAccounts();
    }

    @Override
    public Request<?, HpbBlockNumber> hpbBlockNumber() {
        return super.hpbBlockNumber();
    }

    @Override
    public Request<?, HpbGetBalance> hpbGetBalance(String address, DefaultBlockParameter defaultBlockParameter) {
        return super.hpbGetBalance(address, defaultBlockParameter);
    }

    @Override
    public Request<?, HpbGetStorageAt> hpbGetStorageAt(String address, BigInteger position, DefaultBlockParameter defaultBlockParameter) {
        return super.hpbGetStorageAt(address, position, defaultBlockParameter);
    }

    @Override
    public Request<?, HpbGetTransactionCount> hpbGetTransactionCount(String address, DefaultBlockParameter defaultBlockParameter) {
        return super.hpbGetTransactionCount(address, defaultBlockParameter);
    }

    @Override
    public Request<?, HpbGetBlockTransactionCountByHash> hpbGetBlockTransactionCountByHash(String blockHash) {
        return super.hpbGetBlockTransactionCountByHash(blockHash);
    }

    @Override
    public Request<?, HpbGetBlockTransactionCountByNumber> hpbGetBlockTransactionCountByNumber(DefaultBlockParameter defaultBlockParameter) {
        return super.hpbGetBlockTransactionCountByNumber(defaultBlockParameter);
    }

    @Override
    public Request<?, HpbGetUncleCountByBlockHash> hpbGetUncleCountByBlockHash(String blockHash) {
        return super.hpbGetUncleCountByBlockHash(blockHash);
    }

    @Override
    public Request<?, HpbGetUncleCountByBlockNumber> hpbGetUncleCountByBlockNumber(DefaultBlockParameter defaultBlockParameter) {
        return super.hpbGetUncleCountByBlockNumber(defaultBlockParameter);
    }

    @Override
    public Request<?, HpbGetCode> hpbGetCode(String address, DefaultBlockParameter defaultBlockParameter) {
        return super.hpbGetCode(address, defaultBlockParameter);
    }

    @Override
    public Request<?, HpbSign> hpbSign(String address, String sha3HashOfDataToSign) {
        return super.hpbSign(address, sha3HashOfDataToSign);
    }

    @Override
    public Request<?, HpbSendTransaction> hpbSendTransaction(Transaction transaction) {
        return super.hpbSendTransaction(transaction);
    }

    @Override
    public Request<?, HpbSendTransaction> hpbSendRawTransaction(String signedTransactionData) {
        return super.hpbSendRawTransaction(signedTransactionData);
    }

    @Override
    public Request<?, HpbCall> hpbCall(Transaction transaction, DefaultBlockParameter defaultBlockParameter) {
        return super.hpbCall(transaction, defaultBlockParameter);
    }

    @Override
    public Request<?, HpbEstimateGas> hpbEstimateGas(Transaction transaction) {
        return super.hpbEstimateGas(transaction);
    }

    @Override
    public Request<?, HpbBlock> hpbGetBlockByHash(String blockHash, boolean returnFullTransactionObjects) {
        return super.hpbGetBlockByHash(blockHash, returnFullTransactionObjects);
    }

    @Override
    public Request<?, HpbBlock> hpbGetBlockByNumber(DefaultBlockParameter defaultBlockParameter, boolean returnFullTransactionObjects) {
        return super.hpbGetBlockByNumber(defaultBlockParameter, returnFullTransactionObjects);
    }

    @Override
    public Request<?, HpbTransaction> hpbGetTransactionByHash(String transactionHash) {
        return super.hpbGetTransactionByHash(transactionHash);
    }

    @Override
    public Request<?, HpbTransaction> hpbGetTransactionByBlockHashAndIndex(String blockHash, BigInteger transactionIndex) {
        return super.hpbGetTransactionByBlockHashAndIndex(blockHash, transactionIndex);
    }

    @Override
    public Request<?, HpbTransaction> hpbGetTransactionByBlockNumberAndIndex(DefaultBlockParameter defaultBlockParameter, BigInteger transactionIndex) {
        return super.hpbGetTransactionByBlockNumberAndIndex(defaultBlockParameter, transactionIndex);
    }

    @Override
    public Request<?, HpbGetTransactionReceipt> hpbGetTransactionReceipt(String transactionHash) {
        return super.hpbGetTransactionReceipt(transactionHash);
    }

    @Override
    public Request<?, HpbBlock> hpbGetUncleByBlockHashAndIndex(String blockHash, BigInteger transactionIndex) {
        return super.hpbGetUncleByBlockHashAndIndex(blockHash, transactionIndex);
    }

    @Override
    public Request<?, HpbBlock> hpbGetUncleByBlockNumberAndIndex(DefaultBlockParameter defaultBlockParameter, BigInteger uncleIndex) {
        return super.hpbGetUncleByBlockNumberAndIndex(defaultBlockParameter, uncleIndex);
    }

    @Override
    public Request<?, HpbGetCompilers> hpbGetCompilers() {
        return super.hpbGetCompilers();
    }

    @Override
    public Request<?, HpbCompileLLL> hpbCompileLLL(String sourceCode) {
        return super.hpbCompileLLL(sourceCode);
    }

    @Override
    public Request<?, HpbCompileSolidity> hpbCompileSolidity(String sourceCode) {
        return super.hpbCompileSolidity(sourceCode);
    }

    @Override
    public Request<?, HpbCompileSerpent> hpbCompileSerpent(String sourceCode) {
        return super.hpbCompileSerpent(sourceCode);
    }

    @Override
    public Request<?, HpbFilter> hpbNewFilter(io.hpb.web3.protocol.core.methods.request.HpbFilter hpbFilter) {
        return super.hpbNewFilter(hpbFilter);
    }

    @Override
    public Request<?, HpbFilter> hpbNewBlockFilter() {
        return super.hpbNewBlockFilter();
    }

    @Override
    public Request<?, HpbFilter> hpbNewPendingTransactionFilter() {
        return super.hpbNewPendingTransactionFilter();
    }

    @Override
    public Request<?, HpbUninstallFilter> hpbUninstallFilter(BigInteger filterId) {
        return super.hpbUninstallFilter(filterId);
    }

    @Override
    public Request<?, HpbLog> hpbGetFilterChanges(BigInteger filterId) {
        return super.hpbGetFilterChanges(filterId);
    }

    @Override
    public Request<?, HpbLog> hpbGetFilterLogs(BigInteger filterId) {
        return super.hpbGetFilterLogs(filterId);
    }

    @Override
    public Request<?, HpbLog> hpbGetLogs(io.hpb.web3.protocol.core.methods.request.HpbFilter hpbFilter) {
        return super.hpbGetLogs(hpbFilter);
    }

    @Override
    public Request<?, HpbGetWork> hpbGetWork() {
        return super.hpbGetWork();
    }

    @Override
    public Request<?, HpbSubmitWork> hpbSubmitWork(String nonce, String headerPowHash, String mixDigest) {
        return super.hpbSubmitWork(nonce, headerPowHash, mixDigest);
    }

    @Override
    public Request<?, HpbSubmitHashrate> hpbSubmitHashrate(String hashrate, String clientId) {
        return super.hpbSubmitHashrate(hashrate, clientId);
    }

    @Override
    public Request<?, DbPutString> dbPutString(String databaseName, String keyName, String stringToStore) {
        return super.dbPutString(databaseName, keyName, stringToStore);
    }

    @Override
    public Request<?, DbGetString> dbGetString(String databaseName, String keyName) {
        return super.dbGetString(databaseName, keyName);
    }

    @Override
    public Request<?, DbPutHex> dbPutHex(String databaseName, String keyName, String dataToStore) {
        return super.dbPutHex(databaseName, keyName, dataToStore);
    }

/*    @Override
    public Request<?, DbGetHex> dbGetHex(String databaseName, String keyName) {
        return super.dbGetHex(databaseName, keyName);
    }*/

    @Override
    public Request<?, ShhPost> shhPost(io.hpb.web3.protocol.core.methods.request.ShhPost shhPost) {
        return super.shhPost(shhPost);
    }

    @Override
    public Request<?, ShhVersion> shhVersion() {
        return super.shhVersion();
    }

    @Override
    public Request<?, ShhNewIdentity> shhNewIdentity() {
        return super.shhNewIdentity();
    }

    @Override
    public Request<?, ShhHasIdentity> shhHasIdentity(String identityAddress) {
        return super.shhHasIdentity(identityAddress);
    }

    @Override
    public Request<?, ShhNewGroup> shhNewGroup() {
        return super.shhNewGroup();
    }

    @Override
    public Request<?, ShhAddToGroup> shhAddToGroup(String identityAddress) {
        return super.shhAddToGroup(identityAddress);
    }

    @Override
    public Request<?, ShhNewFilter> shhNewFilter(ShhFilter shhFilter) {
        return super.shhNewFilter(shhFilter);
    }

    @Override
    public Request<?, ShhUninstallFilter> shhUninstallFilter(BigInteger filterId) {
        return super.shhUninstallFilter(filterId);
    }

    @Override
    public Request<?, ShhMessages> shhGetFilterChanges(BigInteger filterId) {
        return super.shhGetFilterChanges(filterId);
    }

    @Override
    public Request<?, ShhMessages> shhGetMessages(BigInteger filterId) {
        return super.shhGetMessages(filterId);
    }

  /*  @Override
    public Observable<NewHeadsNotification> newHeadsNotifications() {
        return super.newHeadsNotifications();
    }

    @Override
    public Observable<LogNotification> logsNotifications(List<String> addresses, List<String> topics) {
        return super.logsNotifications(addresses, topics);
    }

    @Override
    public Observable<String> hpbBlockHashObservable() {
        return super.hpbBlockHashObservable();
    }

    @Override
    public Observable<String> hpbPendingTransactionHashObservable() {
        return super.hpbPendingTransactionHashObservable();
    }

    @Override
    public Observable<Log> hpbLogObservable(io.hpb.web3.protocol.core.methods.request.HpbFilter hpbFilter) {
        return super.hpbLogObservable(hpbFilter);
    }

    @Override
    public Observable<io.hpb.web3.protocol.core.methods.response.Transaction> transactionObservable() {
        return super.transactionObservable();
    }

    @Override
    public Observable<io.hpb.web3.protocol.core.methods.response.Transaction> pendingTransactionObservable() {
        return super.pendingTransactionObservable();
    }

    @Override
    public Observable<HpbBlock> blockObservable(boolean fullTransactionObjects) {
        return super.blockObservable(fullTransactionObjects);
    }

    @Override
    public Observable<HpbBlock> replayBlocksObservable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock, boolean fullTransactionObjects) {
        return super.replayBlocksObservable(startBlock, endBlock, fullTransactionObjects);
    }

    @Override
    public Observable<HpbBlock> replayBlocksObservable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock, boolean fullTransactionObjects, boolean ascending) {
        return super.replayBlocksObservable(startBlock, endBlock, fullTransactionObjects, ascending);
    }

    @Override
    public Observable<io.hpb.web3.protocol.core.methods.response.Transaction> replayTransactionsObservable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        return super.replayTransactionsObservable(startBlock, endBlock);
    }

    @Override
    public Observable<HpbBlock> catchUpToLatestBlockObservable(DefaultBlockParameter startBlock, boolean fullTransactionObjects, Observable<HpbBlock> onCompleteObservable) {
        return super.catchUpToLatestBlockObservable(startBlock, fullTransactionObjects, onCompleteObservable);
    }

    @Override
    public Observable<HpbBlock> catchUpToLatestBlockObservable(DefaultBlockParameter startBlock, boolean fullTransactionObjects) {
        return super.catchUpToLatestBlockObservable(startBlock, fullTransactionObjects);
    }

    @Override
    public Observable<io.hpb.web3.protocol.core.methods.response.Transaction> catchUpToLatestTransactionObservable(DefaultBlockParameter startBlock) {
        return super.catchUpToLatestTransactionObservable(startBlock);
    }

    @Override
    public Observable<HpbBlock> catchUpToLatestAndSubscribeToNewBlocksObservable(DefaultBlockParameter startBlock, boolean fullTransactionObjects) {
        return super.catchUpToLatestAndSubscribeToNewBlocksObservable(startBlock, fullTransactionObjects);
    }

    @Override
    public Observable<io.hpb.web3.protocol.core.methods.response.Transaction> catchUpToLatestAndSubscribeToNewTransactionsObservable(DefaultBlockParameter startBlock) {
        return super.catchUpToLatestAndSubscribeToNewTransactionsObservable(startBlock);
    }*/

    @Override
    public void shutdown() {
        super.shutdown();
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public String toString() {
        return super.toString();
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
    }
}
