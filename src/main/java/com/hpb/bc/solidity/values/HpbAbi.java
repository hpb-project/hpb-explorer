package com.hpb.bc.solidity.values;

/**
 * Created by davidroon on 25.04.17.
 * This code is released under Apache 2 license
 */
public final class HpbAbi {
    private final String abi;

    private HpbAbi(String abi) {
        this.abi = abi;
    }

    public static HpbAbi of(String abi) {
        return new HpbAbi(abi);
    }

    public String getAbi() {
        return abi;
    }
}
