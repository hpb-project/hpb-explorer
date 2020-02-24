package com.hpb.bc.solidity.converters.encoders;

import com.hpb.bc.model.HpbData;
import com.hpb.bc.solidity.SolidityType;
import com.hpb.bc.solidity.values.HpbSignature;


/**
 * Created by davidroon on 05.04.17.
 * This code is released under Apache 2 license
 */
public class SignatureEncoder implements SolidityTypeEncoder {

    private final HpbDataEncoder dataEncoder = new HpbDataEncoder();

    @Override
    public boolean canConvert(Class<?> type) {
        return HpbSignature.class.equals(type);
    }

    @Override
    public HpbData encode(Object arg, SolidityType solidityType) {
        return dataEncoder.encode(((HpbSignature) arg).toData(), SolidityType.BYTES);
    }
}
