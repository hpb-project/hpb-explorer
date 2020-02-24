package com.hpb.bc.solidity.values;

import com.hpb.bc.model.HpbHash;
import com.hpb.bc.solidity.values.Nonce;

import java.util.concurrent.CompletableFuture;

/**
 * Created by davidroon on 06.06.17.
 */
public class CallDetails {
    private final CompletableFuture<HpbTransactionReceipt> result;
    private final HpbHash txHash;
    private final Nonce nonce;
    private final GasUsage gasEstimate;

    public CallDetails(CompletableFuture<HpbTransactionReceipt> result, HpbHash txHash, Nonce nonce, GasUsage gasEstimate) {
        this.result = result;
        this.txHash = txHash;
        this.nonce = nonce;
        this.gasEstimate = gasEstimate;
    }


    public CompletableFuture<HpbTransactionReceipt> getResult() {
        return result;
    }

    public Nonce getNonce() {
        return nonce;
    }

    public GasUsage getGasEstimate() {
        return gasEstimate;
    }

    public HpbHash getTxHash() {
        return txHash;
    }
}
