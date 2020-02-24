package com.hpb.bc.solidity.converters.encoders;


import com.hpb.bc.model.HpbData;
import com.hpb.bc.solidity.SolidityType;
import com.hpb.bc.solidity.values.HpbValue;

/**
 * Created by davidroon on 03.04.17.
 * This code is released under Apache 2 license
 */
public class HpbValueEncoder implements SolidityTypeEncoder {

    @Override
    public boolean canConvert(Class<?> type) {
        return HpbValue.class.equals(type);
    }

    @Override
    public HpbData encode(Object arg, SolidityType solidityType) {
        HpbValue value = (HpbValue) arg;
        return HpbData.of(value.inWei());
    }
}
