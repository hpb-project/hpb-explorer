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

package com.hpb.bc.util;


import com.hpb.bc.exception.HpbApiException;
import com.hpb.bc.model.HpbData;

public class Sha3 {
    public final byte[] hash;

    public HpbData toData() {
        return HpbData.of(hash);
    }

    public Sha3(byte[] hash) {
        if (hash.length != 32) {
            throw new HpbApiException("SHA3 hash length should be 32");
        }
        this.hash = hash;
    }
}
