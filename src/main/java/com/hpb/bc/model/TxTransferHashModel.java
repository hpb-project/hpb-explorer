package com.hpb.bc.model;

public class TxTransferHashModel extends BaseModel {

    private String txHash;

    private String address;

    public String getTxHash() {
        return txHash;
    }

    public void setTxHash(String txHash) {
        this.txHash = txHash;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
