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

package com.hpb.bc.solidity;


import com.hpb.bc.model.EventData;
import com.hpb.bc.solidity.abi.AbiEntry;
import com.hpb.bc.solidity.converters.decoders.SolidityTypeDecoder;

import java.util.List;

public abstract class SolidityEvent<T> {
    private final AbiEntry description;
    private final List<List<SolidityTypeDecoder>> decoders;

    public SolidityEvent(AbiEntry description, List<List<SolidityTypeDecoder>> decoders) {
        this.description = description;
        this.decoders = decoders;
    }

    public AbiEntry getDescription() {
        return description;
    }

    public List<List<SolidityTypeDecoder>> getDecoders() {
        return decoders;
    }

    public abstract T parseEvent(EventData eventData);

    public boolean match(EventData data) {
        return data.getEventSignature().equals(description.signature()) || data.getEventSignature().equals(description.signatureLong());
    }

}
