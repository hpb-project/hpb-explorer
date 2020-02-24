package com.hpb.bc.solidity.converters.encoders;


import com.hpb.bc.exception.ApiException;
import com.hpb.bc.model.HpbData;
import com.hpb.bc.solidity.SolidityType;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * Created by davidroon on 03.04.17.
 * This code is released under Apache 2 license
 */
public class NumberEncoder implements SolidityTypeEncoder {

    @Override
    public boolean canConvert(Class<?> type) {
        return type.getTypeName().equals("int") ||
                type.getTypeName().equals("long") ||
                type.getTypeName().equals("byte") ||
                Number.class.isAssignableFrom(type);
    }

    @Override
    public HpbData encode(Object arg, SolidityType solidityType) {
        if (solidityType.name().startsWith("U")) {
            if (arg instanceof BigInteger) {
                //we can only accept non decimal values
                if (((BigInteger) arg).signum() == -1) {
                    throw new ApiException("unsigned type cannot encode negative values");
                }
            } else if (arg instanceof BigDecimal) {
                return encode(((BigDecimal) arg).toBigInteger(), solidityType);
            } else if (((Number) arg).longValue() < 0) {
                throw new ApiException("unsigned type cannot encode negative values." + ((Number) arg).longValue());
            }
        }
        if (arg instanceof BigInteger) {
            return HpbData.of((BigInteger) arg);
        }
        return HpbData.of(((Number) arg).longValue());
    }
}
