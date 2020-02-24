package com.hpb.bc.solidity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.hpb.bc.model.HpbData;
import com.hpb.bc.solidity.abi.AbiEntry;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TruffleSolidityContractDetails implements SolidityContractDetails {

    private final List<AbiEntry> abi;
    private final String bytecode;
    private final String metadata;

    public TruffleSolidityContractDetails() {
        this(null, null, null);
    }

    public TruffleSolidityContractDetails(List<AbiEntry> abi, String bytecode, String metadata) {
        this.abi = abi;
        this.bytecode = bytecode;
        this.metadata = metadata;
    }

    @Override
    public List<AbiEntry> getAbi() {
        return abi;
    }

    @Override
    public String getMetadata() {
        return metadata;
    }

    @Override
    public HpbData getBinary() {
        return HpbData.of(bytecode);
    }

    public String getBytecode() {
        return bytecode;
    }
}
