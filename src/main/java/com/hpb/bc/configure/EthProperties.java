package com.hpb.bc.configure;


import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * EthProperties property container.
 */
@Component
@ConfigurationProperties(prefix = "eth")
public class EthProperties {

    public static final String WEB3_PREFIX = "eth";

    private String ethNodeIp;

    private String contractAddress;

    private String toAddress;

    /**
     * 消币账户余额
     */
    private String toAddressBalance;
    /**
     * 官方前5个消币账户余额；
     */
    private String fiveAddressBalance;


    public String getEthNodeIp() {
        return ethNodeIp;
    }

    public void setEthNodeIp(String ethNodeIp) {
        this.ethNodeIp = ethNodeIp;
    }

    public String getContractAddress() {
        return contractAddress;
    }

    public void setContractAddress(String contractAddress) {
        this.contractAddress = contractAddress;
    }

    public String getToAddress() {
        return toAddress;
    }

    public void setToAddress(String toAddress) {
        this.toAddress = toAddress;
    }


    public String getToAddressBalance() {
        return toAddressBalance;
    }

    public void setToAddressBalance(String toAddressBalance) {
        this.toAddressBalance = toAddressBalance;
    }

    public String getFiveAddressBalance() {
        return fiveAddressBalance;
    }

    public void setFiveAddressBalance(String fiveAddressBalance) {
        this.fiveAddressBalance = fiveAddressBalance;
    }
}
