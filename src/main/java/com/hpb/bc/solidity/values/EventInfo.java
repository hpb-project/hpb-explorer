package com.hpb.bc.solidity.values;

import com.hpb.bc.model.HpbHash;

/**
 * Created by davidroon on 03.04.17.
 * This code is released under Apache 2 license
 */
public class EventInfo<T> {
    private final HpbHash transactionHash;
    private final T result;

    public EventInfo(HpbHash transactionHash, T result) {
        this.transactionHash = transactionHash;
        this.result = result;
    }

    public HpbHash getTransactionHash() {
        return transactionHash;
    }

    public T getResult() {
        return result;
    }

    @Override
    public String toString() {
        return "EventInfo{" +
                "transactionHash=" + transactionHash +
                ", result=" + result +
                '}';
    }
}
