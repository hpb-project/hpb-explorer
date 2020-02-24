package com.hpb.bc.solidity.converters.decoders;


import com.hpb.bc.model.HpbData;

import java.lang.reflect.Type;

/**
 * Created by davidroon on 08.04.17.
 * This code is released under Apache 2 license
 */
public class EnumDecoder implements SolidityTypeDecoder {

    private final NumberDecoder numberDecoder = new NumberDecoder();

    @Override
    public Enum decode(Integer index, HpbData data, Type resultType) {
        Integer ordinal = numberDecoder.decode(index, data, Integer.class).intValue();
        return ((Class<? extends Enum>) resultType).getEnumConstants()[ordinal];
    }

    @Override
    public boolean canDecode(Class<?> resultCls) {
        return resultCls.isEnum();
    }
}
