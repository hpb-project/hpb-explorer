package com.hpb.bc.solidity.converters.encoders;


import com.hpb.bc.model.HpbData;
import com.hpb.bc.solidity.SolidityType;

/**
 * Created by davidroon on 02.04.17.
 * This code is released under Apache 2 license
 */
public interface SolidityTypeEncoder {
    boolean canConvert(Class<?> type);

    HpbData encode(Object arg, SolidityType solidityType);

}
