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

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.hpb.bc.model.HpbData;
import com.hpb.bc.solidity.abi.AbiEntry;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TruffleSolidityContractDetails implements SolidityContractDetails {

    private final List<AbiEntry> abi;
    private final String bytecode;
    private final String metadata;

    public TruffleSolidityContractDetails() {
        this(null, null, null);
    }

    public TruffleSolidityContractDetails(List<AbiEntry> abi, String bytecode, String metadata) {
        this.abi = abi;
        this.bytecode = bytecode;
        this.metadata = metadata;
    }

    @Override
    public List<AbiEntry> getAbi() {
        return abi;
    }

    @Override
    public String getMetadata() {
        return metadata;
    }

    @Override
    public HpbData getBinary() {
        return HpbData.of(bytecode);
    }

    public String getBytecode() {
        return bytecode;
    }
}
