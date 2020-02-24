package com.hpb.bc.solidity.converters.encoders;


import com.hpb.bc.exception.ApiException;
import com.hpb.bc.model.HpbData;
import com.hpb.bc.solidity.SolidityType;

import java.math.BigInteger;

/**
 * Created by davidroon on 03.04.17.
 * This code is released under Apache 2 license
 */
public class BooleanEncoder implements SolidityTypeEncoder {

    @Override
    public boolean canConvert(Class<?> type) {
        return type.equals(Boolean.class) || type.getTypeName().equals("boolean");
    }

    @Override
    public HpbData encode(Object arg, SolidityType solidityType) {
        if (arg instanceof Boolean) {
            return HpbData.of((Boolean) arg ? BigInteger.ONE : BigInteger.ZERO);
        }
        throw new ApiException("cannot encode bool value:" + arg);
    }
}
