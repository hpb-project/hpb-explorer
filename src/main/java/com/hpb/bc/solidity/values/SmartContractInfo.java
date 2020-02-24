package com.hpb.bc.solidity.values;

/**
 * Created by davidroon on 21.09.16.
 * This code is released under Apache 2 license
 */
public class SmartContractInfo {
    private final HpbAddress address;
    private final HpbAccount account;

    public SmartContractInfo(HpbAddress address, HpbAccount account) {
        this.address = address;
        this.account = account;
    }

    public HpbAddress getAddress() {
        return address;
    }

    public HpbAccount getAccount() {
        return account;
    }
}
