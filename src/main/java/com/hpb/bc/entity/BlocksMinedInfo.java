package com.hpb.bc.entity;

/**
 * Created with IDEA
 *
 * @ Author：wangm
 * @ Date：Created in  2018/12/13 11:41
 * @ Description：APi-旷工块信息
 */
public class BlocksMinedInfo {
    private String blockNumber;
    private String timeStamp;
    private String blockReward;

    public String getBlockNumber() {
        return blockNumber;
    }

    public void setBlockNumber(String blockNumber) {
        this.blockNumber = blockNumber;
    }

    public String getTimeStamp(String transactionTimestamp) {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getBlockReward() {
        return blockReward;
    }

    public void setBlockReward(String blockReward) {
        this.blockReward = blockReward;
    }
}
