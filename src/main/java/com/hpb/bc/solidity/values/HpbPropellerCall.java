package com.hpb.bc.solidity.values;

import com.hpb.bc.model.HpbHash;
import com.hpb.bc.solidity.values.Nonce;

import java.util.concurrent.CompletableFuture;

/**
 * Created by davidroon on 06.06.17.
 */
public class HpbPropellerCall<T> {

    private final Nonce nonce;
    private final GasUsage gasEstimate;

    private final HpbHash transactionHash;
    private final CompletableFuture<T> result;

    public HpbPropellerCall(Nonce nonce, GasUsage gasEstimate, HpbHash transactionHash, CompletableFuture<T> result) {
        this.transactionHash = transactionHash;
        this.result = result;
        this.nonce = nonce;
        this.gasEstimate = gasEstimate;
    }

    public CompletableFuture<T> getResult() {
        return result;
    }

    public HpbHash getTransactionHash() {
        return transactionHash;
    }
}

