package com.hpb.bc.solidity.converters.encoders.list;

import com.hpb.bc.model.HpbData;
import com.hpb.bc.solidity.SolidityType;
import com.hpb.bc.solidity.converters.encoders.SolidityTypeEncoder;

import java.util.List;

/**
 * Created by davidroon on 14.04.17.
 * This code is released under Apache 2 license
 */
public class ListEncoder extends CollectionEncoder {

    public ListEncoder(List<SolidityTypeEncoder> encoders) {
        super(encoders);
    }

    public ListEncoder(List<SolidityTypeEncoder> encoders, Integer size) {
        super(encoders, size);
    }

    @Override
    public boolean canConvert(Class<?> type) {
        return List.class.isAssignableFrom(type);
    }

    @Override
    public HpbData encode(Object arg, SolidityType solidityType) {
        return encode((List) arg, solidityType);
    }


}
