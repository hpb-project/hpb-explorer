package com.hpb.bc.solidity;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.Map;

public class CompilerContractResult implements Serializable {

    @JsonProperty("contracts")
    private Map<String, ContractMetadata> contracts;
    @JsonProperty("version")
    public String version;

    public Map<String, ContractMetadata> getContracts() {
        return contracts;
    }

    public void setContracts(Map<String, ContractMetadata> contracts) {
        this.contracts = contracts;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}
