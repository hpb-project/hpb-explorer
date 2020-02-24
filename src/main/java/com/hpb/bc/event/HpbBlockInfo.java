package com.hpb.bc.event;


import com.hpb.bc.solidity.values.HpbTransactionReceipt;

import java.util.List;

public class HpbBlockInfo {
    public final long blockNumber;
    public final List<HpbTransactionReceipt> receipts;

    public HpbBlockInfo(long blockNumber, List<HpbTransactionReceipt> receipts) {
        this.blockNumber = blockNumber;
        this.receipts = receipts;
    }


    @Override
    public String toString() {
        return "BlockInfo{" +
                "blockNumber=" + blockNumber +
                ", receipts=" + receipts +
                '}';
    }
}
