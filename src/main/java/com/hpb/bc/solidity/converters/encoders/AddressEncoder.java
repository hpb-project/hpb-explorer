package com.hpb.bc.solidity.converters.encoders;


import com.hpb.bc.model.HpbData;
import com.hpb.bc.solidity.SolidityType;
import com.hpb.bc.solidity.values.HpbAddress;

import java.math.BigInteger;

/**
 * Created by davidroon on 05.04.17.
 * This code is released under Apache 2 license
 */
public class AddressEncoder implements SolidityTypeEncoder {
    private final NumberEncoder numberEncoder = new NumberEncoder();

    @Override
    public boolean canConvert(Class<?> type) {
        return HpbAddress.class.equals(type);
    }

    @Override
    public HpbData encode(Object arg, SolidityType solidityType) {
        return numberEncoder.encode(new BigInteger(1, ((HpbAddress) arg).address), SolidityType.INT);
    }
}
