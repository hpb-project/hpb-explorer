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

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * Created by davidroon on 03.04.17.
 * This code is released under Apache 2 license
 */
public class NumberEncoder implements SolidityTypeEncoder {

    @Override
    public boolean canConvert(Class<?> type) {
        return type.getTypeName().equals("int") ||
                type.getTypeName().equals("long") ||
                type.getTypeName().equals("byte") ||
                Number.class.isAssignableFrom(type);
    }

    @Override
    public HpbData encode(Object arg, SolidityType solidityType) {
        if (solidityType.name().startsWith("U")) {
            if (arg instanceof BigInteger) {
                //we can only accept non decimal values
                if (((BigInteger) arg).signum() == -1) {
                    throw new ApiException("unsigned type cannot encode negative values");
                }
            } else if (arg instanceof BigDecimal) {
                return encode(((BigDecimal) arg).toBigInteger(), solidityType);
            } else if (((Number) arg).longValue() < 0) {
                throw new ApiException("unsigned type cannot encode negative values." + ((Number) arg).longValue());
            }
        }
        if (arg instanceof BigInteger) {
            return HpbData.of((BigInteger) arg);
        }
        return HpbData.of(((Number) arg).longValue());
    }
}
