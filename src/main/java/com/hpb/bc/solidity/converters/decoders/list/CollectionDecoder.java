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

package com.hpb.bc.solidity.converters.decoders.list;


import com.hpb.bc.exception.ApiException;
import com.hpb.bc.model.HpbData;
import com.hpb.bc.solidity.converters.decoders.NumberDecoder;
import com.hpb.bc.solidity.converters.decoders.SolidityTypeDecoder;

import java.lang.reflect.Array;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

import static com.hpb.bc.model.HpbData.WORD_SIZE;


/**
 * Created by davidroon on 04.04.17.
 * This code is released under Apache 2 license
 */
public abstract class CollectionDecoder implements SolidityTypeDecoder {
    private final List<SolidityTypeDecoder> decoders;
    private final int size;
    private final boolean isDynamic;
    private final NumberDecoder numberDecoder = new NumberDecoder();

    CollectionDecoder(List<SolidityTypeDecoder> decoders, Integer size) {
        this.decoders = decoders;
        this.size = size;
        this.isDynamic = false;
    }

    CollectionDecoder(List<SolidityTypeDecoder> decoders) {
        this.decoders = decoders;
        this.size = 0;
        this.isDynamic = true;
    }

    Object[] decodeCollection(Integer index, HpbData data, Class<?> subResultType) {
        SolidityTypeDecoder decoder = decoders.stream()
                .filter(dec -> dec.canDecode(subResultType)).findFirst()
                .orElseThrow(() -> new ApiException("no decoder found. serious bug detected!"));
        int size = this.size;
        int arrIndex = index;

        if (this.isDynamic) {
            arrIndex = (numberDecoder.decode(index, data, Integer.class).intValue() / WORD_SIZE);
            size = numberDecoder.decode(arrIndex, data, Integer.class).intValue();
            arrIndex++;
        }

        Object[] result = (Object[]) Array.newInstance(subResultType, size);
        for (int i = 0; i < size; ++i) {
            result[i] = decoder.decode(i + arrIndex, data, subResultType);
        }
        return result;
    }

    Class<?> getGenericType(Type genericType) {
        return (Class<?>) ((ParameterizedType) genericType).getActualTypeArguments()[0];
    }
}
