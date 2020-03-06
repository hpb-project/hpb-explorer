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

import java.util.Arrays;
import java.util.Optional;

/**
 * Created by davidroon on 02.04.17.
 * This code is released under Apache 2 license
 */
public enum SolidityType {
    UINT, UINT8, UINT16, UINT32, UINT64, UINT128, UINT256,
    INT, INT8, INT16, INT32, INT64, INT128, INT256,
    BOOL, STRING(true), ARRAY(true), BYTES(true), ADDRESS, BYTES32, BYTES16, BYTES8;

    public final boolean isDynamic;

    SolidityType() {
        this.isDynamic = false;
    }


    SolidityType(boolean isDynamic) {
        this.isDynamic = isDynamic;
    }

    public static Optional<SolidityType> find(String type) {
        final String typeToSearch = type.contains("[") ? type.substring(0, type.indexOf("[")) : type;
        return Arrays.stream(values())
                .filter(value -> value.name().equalsIgnoreCase(typeToSearch)).findFirst();
    }
}
