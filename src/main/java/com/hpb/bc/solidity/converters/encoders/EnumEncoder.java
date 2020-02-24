package com.hpb.bc.solidity.converters.encoders;


import com.hpb.bc.model.HpbData;
import com.hpb.bc.solidity.SolidityType;

/**
 * Created by davidroon on 08.04.17.
 * This code is released under Apache 2 license
 */
public class EnumEncoder implements SolidityTypeEncoder {

    private final NumberEncoder numberEncoder = new NumberEncoder();

    @Override
    public boolean canConvert(Class<?> type) {
        return type.isEnum();
    }

    @Override
    public HpbData encode(Object arg, SolidityType solidityType) {
        return numberEncoder.encode(((Enum) arg).ordinal(), SolidityType.UINT);
    }
}
