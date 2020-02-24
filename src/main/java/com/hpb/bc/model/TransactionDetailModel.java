package com.hpb.bc.model;

import io.hpb.web3.protocol.core.methods.response.Log;
import io.hpb.web3.protocol.core.methods.response.Transaction;

import java.util.List;

/**
 * 交易詳情
 */
public class TransactionDetailModel extends Transaction {

    /**
     * 消耗gas
     */
    private String gasUsed;

    /**
     * 交易時間
     */
    private String transactionTimestamp;

    private String transactionHash;

    private String transactionStatus;


    private String gasSpent;

    private String gasLimit;

    private List<Log> logs;

    private String logsBloom;

    private String valueStr;
    /**
     * 交易类型 S 智能合约交易,C 普通交易
     */
    private String txType;

    private String decodeInputData;


    public String getGasUsed() {
        return gasUsed;
    }

    public void setGasUsed(String gasUsed) {
        this.gasUsed = gasUsed;
    }

    public String getTransactionTimestamp() {
        return transactionTimestamp;
    }

    public void setTransactionTimestamp(String transactionTimestamp) {
        this.transactionTimestamp = transactionTimestamp;
    }

    public String getTransactionHash() {
        return transactionHash;
    }

    public void setTransactionHash(String transactionHash) {
        this.transactionHash = transactionHash;
    }

    public String getTransactionStatus() {
        return transactionStatus;
    }

    public void setTransactionStatus(String transactionStatus) {
        this.transactionStatus = transactionStatus;
    }


    public String getGasSpent() {
        return gasSpent;
    }

    public void setGasSpent(String gasSpent) {
        this.gasSpent = gasSpent;
    }

    public String getGasLimit() {
        return gasLimit;
    }

    public void setGasLimit(String gasLimit) {
        this.gasLimit = gasLimit;
    }


    public List<Log> getLogs() {
        return logs;
    }

    public void setLogs(List<Log> logs) {
        this.logs = logs;
    }

    public String getLogsBloom() {
        return logsBloom;
    }

    public void setLogsBloom(String logsBloom) {
        this.logsBloom = logsBloom;
    }

    public String getValueStr() {
        return valueStr;
    }

    public void setValueStr(String valueStr) {
        this.valueStr = valueStr;
    }

    public String getTxType() {
        return txType;
    }

    public void setTxType(String txType) {
        this.txType = txType;
    }


    public String getDecodeInputData() {
        return decodeInputData;
    }

    public void setDecodeInputData(String decodeInputData) {
        this.decodeInputData = decodeInputData;
    }
}
