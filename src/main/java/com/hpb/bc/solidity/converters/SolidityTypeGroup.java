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

package com.hpb.bc.solidity.converters;


import com.hpb.bc.exception.ApiException;
import com.hpb.bc.solidity.SolidityType;

/**
 * Created by davidroon on 04.04.17.
 * This code is released under Apache 2 license
 */
public enum SolidityTypeGroup {
    Number, Bool, String, Array, Raw, Address;

    public static SolidityTypeGroup resolveGroup(final SolidityType type) {
        switch (type) {
            case INT:
            case INT8:
            case INT16:
            case INT32:
            case INT64:
            case INT128:
            case INT256:
            case UINT:
            case UINT8:
            case UINT16:
            case UINT32:
            case UINT64:
            case UINT128:
            case UINT256:
                return SolidityTypeGroup.Number;
            case BOOL:
                return SolidityTypeGroup.Bool;
            case STRING:
                return SolidityTypeGroup.String;
            case ARRAY:
                return SolidityTypeGroup.Array;
            case ADDRESS:
                return SolidityTypeGroup.Address;

            case BYTES:
            case BYTES32:
                return SolidityTypeGroup.Raw;

            default:
                throw new ApiException("not encoder found for " + type.name());
        }
    }
}
