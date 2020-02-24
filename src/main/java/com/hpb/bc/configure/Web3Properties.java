package com.hpb.bc.configure;


import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * web3 property container.
 */
@ConfigurationProperties(prefix = "web3")
public class Web3Properties {

    public static final String WEB3_PREFIX = "web3";

    private String clientAddress;

    private Long tableSplit;

    private Long startBlock;

    private Boolean adminClient;

    private String networkId;

    private Long httpTimeoutSeconds;

    private String maxTps;

    private String transactionClientAddress;

    private String ercClientAddress;

    private String tokenAddress;

    private String tokenKeyStorePath;

    private String tokenPassword;

    private String solcCmd;

    private String cpath;


    public String getClientAddress() {
        return clientAddress;
    }

    public void setClientAddress(String clientAddress) {
        this.clientAddress = clientAddress;
    }

    public Boolean isAdminClient() {
        return adminClient;
    }

    public void setAdminClient(Boolean adminClient) {
        this.adminClient = adminClient;
    }

    public String getNetworkId() {
        return networkId;
    }

    public void setNetworkId(String networkId) {
        this.networkId = networkId;
    }

    public Long getHttpTimeoutSeconds() {
        return httpTimeoutSeconds;
    }

    public void setHttpTimeoutSeconds(Long httpTimeoutSeconds) {
        this.httpTimeoutSeconds = httpTimeoutSeconds;
    }

    public Long getTableSplit() {
        return tableSplit;
    }

    public void setTableSplit(Long tableSplit) {
        this.tableSplit = tableSplit;
    }

    public Long getStartBlock() {
        return startBlock;
    }

    public void setStartBlock(Long startBlock) {
        this.startBlock = startBlock;
    }


    public String getMaxTps() {
        return maxTps;
    }

    public void setMaxTps(String maxTps) {
        this.maxTps = maxTps;
    }

    public String getTransactionClientAddress() {
        return transactionClientAddress;
    }

    public void setTransactionClientAddress(String transactionClientAddress) {
        this.transactionClientAddress = transactionClientAddress;
    }

    public String getErcClientAddress() {
        return ercClientAddress;
    }

    public void setErcClientAddress(String ercClientAddress) {
        this.ercClientAddress = ercClientAddress;
    }


    public String getTokenAddress() {
        return tokenAddress;
    }

    public void setTokenAddress(String tokenAddress) {
        this.tokenAddress = tokenAddress;
    }

    public String getTokenKeyStorePath() {
        return tokenKeyStorePath;
    }

    public void setTokenKeyStorePath(String tokenKeyStorePath) {
        this.tokenKeyStorePath = tokenKeyStorePath;
    }

    public String getTokenPassword() {
        return tokenPassword;
    }

    public void setTokenPassword(String tokenPassword) {
        this.tokenPassword = tokenPassword;
    }

    public String getSolcCmd() {
        return solcCmd;
    }

    public void setSolcCmd(String solcCmd) {
        this.solcCmd = solcCmd;
    }

    public String getCpath() {
        return cpath;
    }

    public void setCpath(String cpath) {
        this.cpath = cpath;
    }
}
