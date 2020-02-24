package com.hpb.bc.solidity.converters.decoders;


import com.hpb.bc.model.HpbData;

import java.lang.reflect.Type;

/**
 * Created by davidroon on 02.04.17.
 * This code is released under Apache 2 license
 */
public interface SolidityTypeDecoder {
    Object decode(Integer index, HpbData data, Type resultType);

    boolean canDecode(Class<?> resultCls);
}
