package com.hpb.bc.solidity.converters.encoders;


import com.hpb.bc.exception.ApiException;
import com.hpb.bc.model.HpbData;
import com.hpb.bc.solidity.SolidityType;

/**
 * Created by davidroon on 23.04.17.
 * This code is released under Apache 2 license
 */
public class HpbDataEncoder implements SolidityTypeEncoder {
    private final NumberEncoder numberEncoder = new NumberEncoder();

    @Override
    public boolean canConvert(Class<?> type) {
        return HpbData.class.equals(type);
    }

    @Override
    public HpbData encode(Object arg, SolidityType solidityType) {
        switch (solidityType) {
            case BYTES:
                return encodeToBytes((HpbData) arg);
            case BYTES8:
            case BYTES16:
            case BYTES32:
                return encodeToBytes32((HpbData) arg);
            default:
                throw new ApiException("HpbData can be encoded to Bytes and Bytes32 only");
        }
    }

    private HpbData encodeToBytes32(HpbData arg) {
        if (arg.length() > HpbData.WORD_SIZE) {
            throw new ApiException("bytes32 is a word only, HpbData data too big " + arg.length());
        }
        return arg.word(0);
    }

    private HpbData encodeToBytes(HpbData arg) {
        HpbData lengthData = numberEncoder.encode(arg.length(), SolidityType.UINT);
        return lengthData.merge(arg);
    }
}
