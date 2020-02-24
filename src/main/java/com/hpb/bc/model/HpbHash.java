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
