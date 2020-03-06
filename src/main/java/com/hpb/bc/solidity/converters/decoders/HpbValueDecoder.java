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
import com.hpb.bc.solidity.values.HpbValue;

import java.lang.reflect.Type;
import java.math.BigInteger;

import static com.hpb.bc.model.HpbData.WORD_SIZE;

/**
 * Created by davidroon on 03.04.17.
 * This code is released under Apache 2 license
 */
public class HpbValueDecoder implements SolidityTypeDecoder {

    @Override
    public HpbValue decode(Integer index, HpbData data, Type resultType) {
        HpbData word = data.word(index);
        if (word.length() > WORD_SIZE) {
            throw new ApiException("a word should be of size 32:" + word.length());
        }
        return HpbValue.wei(word.isEmpty() ? BigInteger.ZERO : new BigInteger(word.data));

    }

    @Override
    public boolean canDecode(Class<?> resultCls) {
        return HpbValue.class.equals(resultCls);
    }
}
