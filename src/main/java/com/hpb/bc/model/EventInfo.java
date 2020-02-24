package com.hpb.bc.model;

public class EventInfo<T> {
    private final HpbHash transactionHash;
    private final T result;

    public EventInfo(HpbHash transactionHash, T result) {
        this.transactionHash = transactionHash;
        this.result = result;
    }

    public HpbHash getTransactionHash() {
        return this.transactionHash;
    }

    public T getResult() {
        return this.result;
    }

    @Override
    public String toString() {
        return "EventInfo{transactionHash=" + this.transactionHash + ", result=" + this.result + '}';
    }
}