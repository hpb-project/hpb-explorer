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

import org.spongycastle.util.encoders.Hex;

import java.io.Serializable;
import java.util.Arrays;

public final class HpbHash implements Serializable {
    public final byte[] data;

    private HpbHash(byte[] data) {
        this.data = data;
    }

    public static HpbHash of(byte[] data) {
        return new HpbHash(data);
    }

    public static HpbHash of(final String data) {
       if (data != null && data.startsWith("0x")) {
            return of(Hex.decode(data.substring(2)));
        }
        return of(Hex.decode(data));
      // return  data;
    }

    public static HpbHash empty() {
        return HpbHash.of(new byte[0]);
    }

    public boolean isEmpty() {
        return data.length == 0;
    }

    public String withLeading0x() {
        return "0x" + this.toString();
    }

    @Override
    public String toString() {
        return Hex.toHexString(data);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        HpbHash ethData = (HpbHash) o;
        return Arrays.equals(data, ethData.data);

    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(data);
    }
}
