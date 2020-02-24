package com.hpb.bc.solidity.converters.decoders;


import com.hpb.bc.model.HpbData;

import java.lang.reflect.Type;

/**
 * Created by davidroon on 23.04.17.
 * This code is released under Apache 2 license
 */
public class HpbDataDecoder implements SolidityTypeDecoder {
    @Override
    public HpbData decode(Integer index, HpbData data, Type resultType) {
        return data.word(index);
    }

    @Override
    public boolean canDecode(Class<?> resultCls) {
        return HpbData.class.equals(resultCls);
    }
}
