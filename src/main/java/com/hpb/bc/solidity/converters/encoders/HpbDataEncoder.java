/*
 * Copyright 2020 HPB Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
