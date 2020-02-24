package com.hpb.bc.solidity.values;

public class ContractMetadata {
    public String abi;
    public String bin;
    public String solInterface;
    public String metadata;

    public ContractMetadata() {
    }

    public String getInterface() {
        return this.solInterface;
    }

    public void setInterface(String solInterface) {
        this.solInterface = solInterface;
    }
}