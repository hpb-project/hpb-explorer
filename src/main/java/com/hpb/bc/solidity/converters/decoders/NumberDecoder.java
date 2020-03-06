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

package com.hpb.bc.solidity.converters.decoders;


import com.hpb.bc.exception.ApiException;
import com.hpb.bc.model.HpbData;
import com.hpb.bc.util.CastUtil;

import java.lang.reflect.Type;
import java.math.BigInteger;

import static com.hpb.bc.model.HpbData.WORD_SIZE;


/**
 * Created by davidroon on 03.04.17.
 * This code is released under Apache 2 license
 */
public class NumberDecoder implements SolidityTypeDecoder {

    @Override
    public Number decode(Integer index, HpbData data, Type resultType) {
        HpbData word = data.word(index);
        if (word.length() > WORD_SIZE) {
            throw new ApiException("a word should be of size 32:" + word.length());
        }
        BigInteger number = word.isEmpty() ? BigInteger.ZERO : new BigInteger(word.data);

        return CastUtil.<Number>matcher()
                .typeNameEquals("long", number::longValueExact)
                .typeNameEquals("int", number::intValueExact)
                .typeNameEquals("short", number::shortValueExact)
                .typeNameEquals("byte", number::byteValueExact)
                .typeEquals(Long.class, number::longValueExact)
                .typeEquals(Integer.class, number::intValueExact)
                .typeEquals(Short.class, number::shortValueExact)
                .typeEquals(Byte.class, number::byteValueExact)
                .typeEquals(BigInteger.class, () -> number)
                .orElseThrowWithErrorMessage("cannot convert to " + resultType.getTypeName())
                .matches((Class<? extends Number>) resultType);
    }

    @Override
    public boolean canDecode(Class<?> resultCls) {
        return resultCls.getTypeName().equals("int") || resultCls.getTypeName().equals("long") || Number.class.isAssignableFrom(resultCls);
    }
}
