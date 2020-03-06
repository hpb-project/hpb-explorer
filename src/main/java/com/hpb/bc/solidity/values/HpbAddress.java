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

package com.hpb.bc.solidity.values;

import com.hpb.bc.exception.ApiException;
import com.hpb.bc.model.HpbData;
import org.spongycastle.util.encoders.Hex;

import java.util.Arrays;

/**
 * Created by davidroon on 19.04.16.
 * This code is released under Apache 2 license
 */
public final class HpbAddress {
    public static final int MAX_ADDRESS_SIZE = 20;
    private static final byte[] EMPTY_ARRAY = new byte[0];
    public final byte[] address;

    private HpbAddress(byte[] address) {
        if (address.length > MAX_ADDRESS_SIZE) {
            throw new ApiException("byte array of the address cannot be bigger than 20.value:" + Hex.toHexString(address));
        }
        this.address = address;
    }

    public static HpbAddress of(byte[] address) {
        if (address == null) {
            return HpbAddress.empty();
        }

        return new HpbAddress(trimLeft(address));
    }

    public static HpbAddress of(final String address) {
        if (address == null) {
            return empty();
        }
        if (address.startsWith("0x")) {
            return of(Hex.decode(address.substring(2)));
        }
        return of(Hex.decode(address));
    }

    public static byte[] trimLeft(byte[] address) {
        int firstNonZeroPos = 0;
        while (firstNonZeroPos < address.length && address[firstNonZeroPos] == 0) {
            firstNonZeroPos++;
        }

        byte[] newAddress = new byte[address.length - firstNonZeroPos];
        System.arraycopy(address, firstNonZeroPos, newAddress, 0, address.length - firstNonZeroPos);

        return newAddress;
    }

    public static HpbAddress empty() {
        return HpbAddress.of(EMPTY_ARRAY);
    }

    @Override
    public String toString() {
        return Hex.toHexString(address);
    }

    public String normalizedString() {
        return toData().toString();
    }

    public String withLeading0x() {
        return "0x" + this.toString();
    }

    public String normalizedWithLeading0x() {
        return this.toData().withLeading0x();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        HpbAddress that = (HpbAddress) o;
        return Arrays.equals(address, that.address);

    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(address);
    }

    public boolean isEmpty() {
        return Arrays.equals(this.address, EMPTY_ARRAY);
    }

    public HpbData toData() {
        return HpbData.of(normalize(address));
    }

    private byte[] normalize(byte[] data) {
        byte[] normalizedData = new byte[20];
        System.arraycopy(data, 0, normalizedData, normalizedData.length - data.length, data.length);
        return normalizedData;
    }
}
