package com.hpb.bc.solidity.values;

import com.hpb.bc.model.HpbHash;

import java.util.Optional;

public class HpbTransactionInfo {

    private final HpbHash transactionHash;
    private final HpbTransactionReceipt receipt;
    private final HpbTransactionStatus status;
    private final HpbHash blockHash;
    private final boolean contractCreation;

    public HpbTransactionInfo(HpbHash transactionHash, HpbTransactionReceipt receipt, HpbTransactionStatus status, HpbHash blockHash, boolean contractCreation) {
        this.transactionHash = transactionHash;
        this.receipt = receipt;
        this.status = status;
        this.blockHash = blockHash;
        this.contractCreation = contractCreation;
    }

    public HpbTransactionInfo(HpbHash transactionHash, HpbTransactionReceipt receipt, HpbTransactionStatus status, HpbHash blockHash) {
        this(transactionHash, receipt, status, blockHash, receipt != null && Optional.ofNullable(receipt).map(r -> r.receiveAddress.isEmpty()).orElse(false));
    }

    public Optional<HpbTransactionReceipt> getReceipt() {
        return Optional.ofNullable(receipt);
    }

    public HpbTransactionStatus getStatus() {
        return status;
    }

    public HpbHash getTransactionHash() {
        return transactionHash;
    }

    public boolean isContractCreation() {
        return contractCreation;
    }

    public HpbHash getBlockHash() {
        return blockHash;
    }
}
