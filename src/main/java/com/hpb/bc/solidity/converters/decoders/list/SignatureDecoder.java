package com.hpb.bc.solidity.converters.decoders.list;


import com.hpb.bc.model.HpbData;
import com.hpb.bc.solidity.converters.decoders.NumberDecoder;
import com.hpb.bc.solidity.converters.decoders.SolidityTypeDecoder;
import com.hpb.bc.solidity.values.HpbSignature;
import org.apache.commons.lang3.ArrayUtils;

import java.lang.reflect.Type;
import java.util.List;

import static com.hpb.bc.model.HpbData.WORD_SIZE;


/**
 * Created by davidroon on 23.04.17.
 * This code is released under Apache 2 license
 */
public class SignatureDecoder extends CollectionDecoder {

    private final NumberDecoder decoder = new NumberDecoder();

    public SignatureDecoder(List<SolidityTypeDecoder> decoders) {
        super(decoders);
    }

    public SignatureDecoder(List<SolidityTypeDecoder> decoders, Integer size) {
        super(decoders, size);
    }

    @Override
    public HpbSignature decode(Integer index, HpbData data, Type resultType) {
        Integer strIndex = decoder.decode(index, data, Integer.class).intValue() / WORD_SIZE;
        Integer len = decoder.decode(strIndex, data, Integer.class).intValue();
        return HpbSignature.of(ArrayUtils.subarray(data.data, (strIndex + 1) * WORD_SIZE, (strIndex + 1) * WORD_SIZE + len));
    }

    @Override
    public boolean canDecode(Class<?> resultCls) {
        return HpbSignature.class.equals(resultCls);
    }
}
