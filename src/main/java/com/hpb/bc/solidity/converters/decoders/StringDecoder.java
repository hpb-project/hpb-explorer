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


import com.hpb.bc.model.HpbData;
import org.apache.commons.lang3.ArrayUtils;

import java.lang.reflect.Type;

import static com.hpb.bc.model.HpbData.WORD_SIZE;

/**
 * Created by davidroon on 04.04.17.
 * This code is released under Apache 2 license
 */
public class StringDecoder implements SolidityTypeDecoder {
    private final NumberDecoder numberDecoder = new NumberDecoder();

    @Override
    public String decode(Integer index, HpbData data, Type resultType) {
        Integer strIndex = numberDecoder.decode(index, data, Integer.class).intValue() / WORD_SIZE;
        Integer len = numberDecoder.decode(strIndex, data, Integer.class).intValue();
        return new String(ArrayUtils.subarray(data.data, (strIndex + 1) * WORD_SIZE, (strIndex + 1) * WORD_SIZE + len));
    }

    @Override
    public boolean canDecode(Class<?> resultCls) {
        return String.class.equals(resultCls);
    }
}
