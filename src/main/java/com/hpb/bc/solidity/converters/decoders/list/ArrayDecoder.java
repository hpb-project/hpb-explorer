package com.hpb.bc.solidity.converters.decoders.list;


import com.hpb.bc.model.HpbData;
import com.hpb.bc.solidity.converters.decoders.SolidityTypeDecoder;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by davidroon on 04.04.17.
 * This code is released under Apache 2 license
 */
public class ArrayDecoder extends CollectionDecoder {

    public ArrayDecoder(List<SolidityTypeDecoder> decoders) {
        super(decoders);
    }

    public ArrayDecoder(List<SolidityTypeDecoder> decoders, Integer size) {
        super(decoders, size);
    }

    @Override
    public Object[] decode(Integer index, HpbData data, Type resultType) {
        return decodeCollection(index, data, ((Class) resultType).getComponentType());
    }

    @Override
    public boolean canDecode(Class<?> resultCls) {
        return resultCls.isArray();
    }
}
