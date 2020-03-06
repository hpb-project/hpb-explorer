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


import com.hpb.bc.model.HpbData;
import com.hpb.bc.solidity.SolidityType;
import com.hpb.bc.solidity.values.HpbAddress;

import java.math.BigInteger;

/**
 * Created by davidroon on 05.04.17.
 * This code is released under Apache 2 license
 */
public class AddressEncoder implements SolidityTypeEncoder {
    private final NumberEncoder numberEncoder = new NumberEncoder();

    @Override
    public boolean canConvert(Class<?> type) {
        return HpbAddress.class.equals(type);
    }

    @Override
    public HpbData encode(Object arg, SolidityType solidityType) {
        return numberEncoder.encode(new BigInteger(1, ((HpbAddress) arg).address), SolidityType.INT);
    }
}
