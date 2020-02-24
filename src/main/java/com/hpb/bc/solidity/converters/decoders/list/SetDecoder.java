package com.hpb.bc.solidity.converters.decoders.list;


import com.hpb.bc.model.HpbData;
import com.hpb.bc.solidity.converters.decoders.SolidityTypeDecoder;

import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by davidroon on 04.04.17.
 * This code is released under Apache 2 license
 */
public class SetDecoder extends CollectionDecoder {

    public SetDecoder(List<SolidityTypeDecoder> decoders) {
        super(decoders);
    }

    public SetDecoder(List<SolidityTypeDecoder> decoders, Integer size) {
        super(decoders, size);
    }

    @Override
    public Set<?> decode(Integer index, HpbData data, Type resultType) {
        return new HashSet<>(Arrays.asList(decodeCollection(index, data, getGenericType(resultType))));
    }

    @Override
    public boolean canDecode(Class<?> resultCls) {
        return resultCls.equals(Set.class);
    }
}
