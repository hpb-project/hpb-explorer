package com.hpb.bc.model;

public class NodeRewardStaticsQueryModel {

    private String address;

    private  long startBlockTimestamp;

    private long endBlockTimestamp;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public long getStartBlockTimestamp() {
        return startBlockTimestamp;
    }

    public void setStartBlockTimestamp(long startBlockTimestamp) {
        this.startBlockTimestamp = startBlockTimestamp;
    }

    public long getEndBlockTimestamp() {
        return endBlockTimestamp;
    }

    public void setEndBlockTimestamp(long endBlockTimestamp) {
        this.endBlockTimestamp = endBlockTimestamp;
    }
}
