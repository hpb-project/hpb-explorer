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

package com.hpb.bc.solidity.converters.encoders.list;


import com.hpb.bc.exception.ApiException;
import com.hpb.bc.model.HpbData;
import com.hpb.bc.solidity.SolidityType;
import com.hpb.bc.solidity.converters.encoders.NumberEncoder;
import com.hpb.bc.solidity.converters.encoders.SolidityTypeEncoder;

import java.util.List;

/**
 * Created by davidroon on 04.04.17.
 * This code is released under Apache 2 license
 */
public abstract class CollectionEncoder implements SolidityTypeEncoder {
    private final NumberEncoder numberEncoder = new NumberEncoder();
    private final List<SolidityTypeEncoder> encoders;
    private final int size;
    private final boolean isDynamic;

    CollectionEncoder(List<SolidityTypeEncoder> encoders) {
        this.encoders = encoders;
        this.size = 0;
        this.isDynamic = true;
    }

    CollectionEncoder(List<SolidityTypeEncoder> encoders, Integer size) {
        this.encoders = encoders;
        this.size = size;
        this.isDynamic = false;
    }

    public HpbData encode(List<?> lst, SolidityType solidityType) {
        if (isDynamic) {
            return encodeDynamicSizeList(lst, solidityType);
        }
        return encodeFixedSizeList(lst, solidityType);
    }

    private HpbData encodeDynamicSizeList(List<?> lst, SolidityType solidityType) {
        HpbData result = lst.stream()
                .map(entry -> encoders.stream()
                        .filter(enc -> enc.canConvert(entry.getClass()))
                        .findFirst().orElseThrow(() -> new ApiException("no encoder found for list entry"))
                        .encode(entry, solidityType))
                .reduce(HpbData.empty(), HpbData::merge);

        return numberEncoder.encode(lst.size(), SolidityType.UINT).merge(result);
    }

    private HpbData encodeFixedSizeList(List<?> lst, SolidityType solidityType) {
        int diff = size - lst.size();
        if (diff < 0) {
            throw new ApiException("List size (" + lst.size() + ") != " + size + " for type " + solidityType.name() + "[" + size + "]");
        }
        HpbData result = lst.stream()
                .map(entry -> encoders.stream()
                        .filter(enc -> enc.canConvert(entry.getClass()))
                        .findFirst().orElseThrow(() -> new ApiException("no encoder found for list entry"))
                        .encode(entry, solidityType))
                .reduce(HpbData.empty(), HpbData::merge);

        for (int i = 0; i < diff; i++) {
            result = result.merge(HpbData.emptyWord());
        }

        return result;
    }
}
