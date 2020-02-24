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
