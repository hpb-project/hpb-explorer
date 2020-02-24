package com.hpb.bc.solidity.converters.decoders;


import com.hpb.bc.model.HpbData;
import com.hpb.bc.solidity.values.HpbAddress;

import java.lang.reflect.Type;

/**
 * Created by davidroon on 04.04.17.
 * This code is released under Apache 2 license
 */
public class AddressDecoder implements SolidityTypeDecoder {

    @Override
    public HpbAddress decode(Integer index, HpbData data, Type resultType) {
        return HpbAddress.of(data.word(index).data);
    }

    @Override
    public boolean canDecode(Class<?> resultCls) {
        return HpbAddress.class.equals(resultCls);
    }
}
