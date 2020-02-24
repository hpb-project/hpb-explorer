package com.hpb.bc.solidity.values;

import com.hpb.bc.model.HpbHash;
import com.hpb.bc.solidity.values.Nonce;

public class HpbTransactionExecutionResult {
    public final HpbHash transactionHash;
    public final Nonce nonce;

    public HpbTransactionExecutionResult(HpbHash transactionHash, Nonce nonce) {
        this.transactionHash = transactionHash;
        this.nonce = nonce;
    }
}
