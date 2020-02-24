package com.hpb.bc.model;

import io.hpb.web3.protocol.core.methods.response.Log;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;

public class HpbEventModel  extends  BaseModel {

    private  String txHash;

    private BigInteger blockNumber;

    private BigInteger blockTimestamp;

    private String methodId;

    private String methodName;

    private String eventName;


    private String topics;

    private List<Log> logList;

    private Map<String,Object> detailParaMap;

    public String getTxHash() {
        return txHash;
    }

    public void setTxHash(String txHash) {
        this.txHash = txHash;
    }

    public BigInteger getBlockNumber() {
        return blockNumber;
    }

    public void setBlockNumber(BigInteger blockNumber) {
        this.blockNumber = blockNumber;
    }

    public BigInteger getBlockTimestamp() {
        return blockTimestamp;
    }

    public void setBlockTimestamp(BigInteger blockTimestamp) {
        this.blockTimestamp = blockTimestamp;
    }

    public String getMethodId() {
        return methodId;
    }

    public void setMethodId(String methodId) {
        this.methodId = methodId;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getTopics() {
        return topics;
    }

    public void setTopics(String topics) {
        this.topics = topics;
    }

    public List<Log> getLogList() {
        return logList;
    }

    public void setLogList(List<Log> logList) {
        this.logList = logList;
    }

    public Map<String, Object> getDetailParaMap() {
        return detailParaMap;
    }

    public void setDetailParaMap(Map<String, Object> detailParaMap) {
        this.detailParaMap = detailParaMap;
    }
}
