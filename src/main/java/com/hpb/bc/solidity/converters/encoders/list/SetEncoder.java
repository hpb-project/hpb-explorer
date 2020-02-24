package com.hpb.bc.solidity.converters.encoders.list;


import com.hpb.bc.model.HpbData;
import com.hpb.bc.solidity.SolidityType;
import com.hpb.bc.solidity.converters.encoders.SolidityTypeEncoder;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

/**
 * Created by davidroon on 14.04.17.
 * This code is released under Apache 2 license
 */
public class SetEncoder extends CollectionEncoder {

    public SetEncoder(List<SolidityTypeEncoder> encoders) {
        super(encoders);
    }

    public SetEncoder(List<SolidityTypeEncoder> encoders, Integer size) {
        super(encoders, size);
    }

    @Override
    public boolean canConvert(Class<?> type) {
        return Set.class.isAssignableFrom(type);
    }

    @Override
    public HpbData encode(Object arg, SolidityType solidityType) {
        return encode(Arrays.asList(((Set) arg).toArray()), solidityType);
    }


}
