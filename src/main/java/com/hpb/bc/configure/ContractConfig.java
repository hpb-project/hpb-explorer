package com.hpb.bc.configure;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "web3j.contract")
public class ContractConfig {
    private String solcCmd;

    public String getSolcCmd() {
        return solcCmd;
    }

    public void setSolcCmd(String solcCmd) {
        this.solcCmd = solcCmd;
    }

}
