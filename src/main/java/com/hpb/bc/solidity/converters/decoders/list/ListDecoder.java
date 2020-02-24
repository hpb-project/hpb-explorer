package com.hpb.bc.solidity.converters.decoders.list;


import com.hpb.bc.model.HpbData;
import com.hpb.bc.solidity.converters.decoders.SolidityTypeDecoder;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by davidroon on 04.04.17.
 * This code is released under Apache 2 license
 */
public class ListDecoder extends CollectionDecoder {

    public ListDecoder(List<SolidityTypeDecoder> decoders) {
        super(decoders);
    }

    public ListDecoder(List<SolidityTypeDecoder> decoders, Integer size) {
        super(decoders, size);
    }

    @Override
    public List<?> decode(Integer index, HpbData data, Type resultType) {
        return new ArrayList<>(Arrays.asList(decodeCollection(index, data, getGenericType(resultType))));
    }

    @Override
    public boolean canDecode(Class<?> resultCls) {
        return resultCls.equals(List.class);
    }
}
