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
import com.hpb.bc.solidity.values.HpbAddress;

import java.lang.reflect.Type;

/**
 * Created by davidroon on 04.04.17.
 * This code is released under Apache 2 license
 */
public class AddressDecoder implements SolidityTypeDecoder {

    @Override
    public HpbAddress decode(Integer index, HpbData data, Type resultType) {
        return HpbAddress.of(data.word(index).data);
    }

    @Override
    public boolean canDecode(Class<?> resultCls) {
        return HpbAddress.class.equals(resultCls);
    }
}
