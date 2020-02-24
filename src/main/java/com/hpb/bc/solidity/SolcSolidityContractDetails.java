package com.hpb.bc.solidity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.hpb.bc.model.HpbData;
import com.hpb.bc.solidity.abi.AbiEntry;
import net.bytebuddy.asm.Advice;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SolcSolidityContractDetails implements SolidityContractDetails {

    private final String abi;
    private final String bin;
    private final String metadata;
    private List<AbiEntry> entries;

    public SolcSolidityContractDetails() {
        this(null, null, null);
    }

    public SolcSolidityContractDetails(String abi, String bin, String metadata) {
        this.abi = abi;
        this.bin = bin;
        this.metadata = metadata;
    }

    public String getBin() {
        return bin;
    }

    @Override
    public String getMetadata() {
        return metadata;
    }

    @Override
    public synchronized List<AbiEntry> getAbi() {
        if (entries == null) {
            entries = AbiEntry.parse(abi);
        }

        return entries;
    }

    @Override
    public HpbData getBinary() {
        return HpbData.of(bin);
    }

}
