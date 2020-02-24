package com.hpb.bc.solidity.converters.encoders;


import com.hpb.bc.model.HpbData;
import com.hpb.bc.solidity.SolidityType;
import com.hpb.bc.solidity.values.HpbAccount;

/**
 * Created by davidroon on 05.04.17.
 * This code is released under Apache 2 license
 */
public class AccountEncoder implements SolidityTypeEncoder {
    private final AddressEncoder addressEncoder = new AddressEncoder();

    @Override
    public boolean canConvert(Class<?> type) {
        return HpbAccount.class.equals(type);
    }

    @Override
    public HpbData encode(Object arg, SolidityType solidityType) {
        return addressEncoder.encode(((HpbAccount) arg).getAddress(), SolidityType.ADDRESS);
    }
}
