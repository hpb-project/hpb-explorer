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
import net.bytebuddy.asm.Advice;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SolcSolidityContractDetails implements SolidityContractDetails {

    private final String abi;
    private final String bin;
    private final String metadata;
    private List<AbiEntry> entries;

    public SolcSolidityContractDetails() {
        this(null, null, null);
    }

    public SolcSolidityContractDetails(String abi, String bin, String metadata) {
        this.abi = abi;
        this.bin = bin;
        this.metadata = metadata;
    }

    public String getBin() {
        return bin;
    }

    @Override
    public String getMetadata() {
        return metadata;
    }

    @Override
    public synchronized List<AbiEntry> getAbi() {
        if (entries == null) {
            entries = AbiEntry.parse(abi);
        }

        return entries;
    }

    @Override
    public HpbData getBinary() {
        return HpbData.of(bin);
    }

}
