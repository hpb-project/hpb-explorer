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

package com.hpb.bc.model;


import com.hpb.bc.util.Crypto;
import com.hpb.bc.util.Sha3;
import org.apache.commons.lang3.ArrayUtils;
import org.spongycastle.util.encoders.Hex;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Arrays;


public final class HpbData implements Serializable {
    public static final int WORD_SIZE = 32;
    public final byte[] data;
    public final int length;

    private HpbData(byte[] data) {
        this.data = data;
        this.length = data.length;
    }

    public static HpbData of(BigInteger b) {
        byte[] bytes = new byte[WORD_SIZE];
        Arrays.fill(bytes, (byte) (b.signum() < 0 ? -1 : 0));
        byte[] biBytes = b.toByteArray();
        int start = biBytes.length == WORD_SIZE + 1 ? 1 : 0;
        int length = Math.min(biBytes.length, WORD_SIZE);
        System.arraycopy(biBytes, start, bytes, WORD_SIZE - length, length);
        return HpbData.of(bytes);
    }

    public static HpbData of(byte[] data) {
        return new HpbData(data);
    }

    public static HpbData of(final String data) {
        if (data != null && data.startsWith("0x")) {
            return of(Hex.decode(data.substring(2)));
        }
        return of(Hex.decode(data));
    }

    public static HpbData empty() {
        return HpbData.of(new byte[0]);
    }

    public static HpbData emptyWord() {
        return HpbData.of(new byte[WORD_SIZE]);
    }

    public static HpbData of(int length) {
        return HpbData.of(BigInteger.valueOf(length));
    }

    public static HpbData of(long length) {
        return HpbData.of(BigInteger.valueOf(length));
    }

    public static HpbData of(byte length) {
        return HpbData.of(BigInteger.valueOf(length));
    }

    public String withLeading0x() {
        return "0x" + this.toString();
    }

    @Override
    public String toString() {
        return Hex.toHexString(data);
    }

    public HpbData merge(HpbData data) {
        if (data.isEmpty()) {
            return this;
        }
        return new HpbData(ArrayUtils.addAll(this.data, data.data));
    }

    public boolean isEmpty() {
        return data.length == 0;
    }

    public int length() {
        return data.length;
    }

    public Sha3 sha3() {

        return new Sha3(Crypto.sha3(data));
    }

    public HpbData word(int index) {
        byte[] word = ArrayUtils.subarray(data, WORD_SIZE * index, WORD_SIZE * (index + 1));
        if (word.length < WORD_SIZE) {
            word = ArrayUtils.addAll(word, new byte[WORD_SIZE - word.length]);
        }
        return HpbData.of(word);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        HpbData ethData = (HpbData) o;
        return Arrays.equals(data, ethData.data);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(data);
    }
}

