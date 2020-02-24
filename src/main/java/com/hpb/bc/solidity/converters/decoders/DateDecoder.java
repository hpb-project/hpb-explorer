package com.hpb.bc.solidity.converters.decoders;


import com.hpb.bc.model.HpbData;

import java.lang.reflect.Type;
import java.util.Date;

/**
 * Created by davidroon on 05.04.17.
 * This code is released under Apache 2 license
 */
public class DateDecoder implements SolidityTypeDecoder {

    private final NumberDecoder numberDecoder = new NumberDecoder();

    @Override
    public Date decode(Integer index, HpbData data, Type resultType) {
        return new Date(numberDecoder.decode(index, data, Long.class).longValue());
    }

    @Override
    public boolean canDecode(Class<?> resultCls) {
        return resultCls.equals(Date.class);
    }
}
