package com.hpb.bc.solidity;

//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//


import com.hpb.bc.util.ByteUtil;
import com.hpb.bc.util.FastByteComparisons;

import java.io.Serializable;
import java.util.Arrays;

public class ByteArrayWrapper implements Comparable<ByteArrayWrapper>, Serializable {
    private final byte[] data;
    private int hashCode = 0;

    public ByteArrayWrapper(byte[] data) {
        if (data == null) {
            throw new NullPointerException("Data must not be null");
        } else {
            this.data = data;
            this.hashCode = Arrays.hashCode(data);
        }
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof ByteArrayWrapper)) {
            return false;
        } else {
            byte[] otherData = ((ByteArrayWrapper) other).getData();
            return FastByteComparisons.compareTo(this.data, 0, this.data.length, otherData, 0, otherData.length) == 0;
        }
    }

    @Override
    public int hashCode() {
        return this.hashCode;
    }

    @Override
    public int compareTo(ByteArrayWrapper o) {
        return FastByteComparisons.compareTo(this.data, 0, this.data.length, o.getData(), 0, o.getData().length);
    }

    public byte[] getData() {
        return this.data;
    }

    @Override
    public String toString() {
        return ByteUtil.toHexString(this.data);
    }
}
