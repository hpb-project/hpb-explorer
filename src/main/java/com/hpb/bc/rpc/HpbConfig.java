package com.hpb.bc.rpc;


import com.hpb.bc.solidity.values.GasPrice;


public class HpbConfig {

    private final long blockWaitLimit;
    private final GasPrice gasPrice;

    public HpbConfig(long blockWaitLimit, GasPrice gasPrice) {

        this.blockWaitLimit = blockWaitLimit;
        this.gasPrice = gasPrice;
    }


    public long blockWaitLimit() {
        return blockWaitLimit;
    }

    public GasPrice getGasPrice() {
        return gasPrice;
    }

    public static class Builder {

        protected long blockWaitLimit = 16;
        protected GasPrice gasPrice;

        public HpbConfig build() {
            return new HpbConfig(blockWaitLimit, gasPrice);
        }

    }
}
