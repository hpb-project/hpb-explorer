//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//
package com.hpb.bc.solidity.values;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.math.BigInteger;
import java.util.Arrays;

import com.hpb.bc.solidity.ByteArrayWrapper;
import com.hpb.bc.util.ByteUtil;
import com.hpb.bc.util.FastByteComparisons;
import org.spongycastle.util.encoders.Hex;

public final class DataWord implements Comparable<DataWord> {
    public static final int MAX_POW = 256;
    public static final BigInteger _2_256 = BigInteger.valueOf(2L).pow(256);
    public static final BigInteger MAX_VALUE;
    public static final DataWord ZERO;
    public static final DataWord ONE;
    public static final long MEM_SIZE = 64L;
    private final byte[] data;

    private DataWord(byte[] data) {
        if (data != null && data.length == 32) {
            this.data = data;
        } else {
            throw new RuntimeException("Input byte array should have 32 bytes in it!");
        }
    }

    public static DataWord of(byte[] data) {
        if (data != null && data.length != 0) {
            int leadingZeroBits = ByteUtil.numberOfLeadingZeros(data);
            int valueBits = 8 * data.length - leadingZeroBits;
            if (valueBits <= 8) {
                if (data[data.length - 1] == 0) {
                    return ZERO;
                }

                if (data[data.length - 1] == 1) {
                    return ONE;
                }
            }

            if (data.length == 32) {
                return new DataWord(Arrays.copyOf(data, data.length));
            } else if (data.length <= 32) {
                byte[] bytes = new byte[32];
                System.arraycopy(data, 0, bytes, 32 - data.length, data.length);
                return new DataWord(bytes);
            } else {
                throw new RuntimeException(String.format("Data word can't exceed 32 bytes: 0x%s", ByteUtil.toHexString(data)));
            }
        } else {
            return ZERO;
        }
    }

    public static DataWord of(ByteArrayWrapper wrappedData) {
        return of(wrappedData.getData());
    }

    @JsonCreator
    public static DataWord of(String data) {
        return of(Hex.decode(data));
    }

    public static DataWord of(byte num) {
        byte[] bb = new byte[32];
        bb[31] = num;
        return new DataWord(bb);
    }

    public static DataWord of(int num) {
        return of(ByteUtil.intToBytes(num));
    }

    public static DataWord of(long num) {
        return of(ByteUtil.longToBytes(num));
    }

    public byte[] getData() {
        return Arrays.copyOf(this.data, this.data.length);
    }

    private byte[] copyData() {
        return Arrays.copyOf(this.data, this.data.length);
    }

    public byte[] getNoLeadZeroesData() {
        return ByteUtil.stripLeadingZeroes(this.copyData());
    }

    public byte[] getLast20Bytes() {
        return Arrays.copyOfRange(this.data, 12, this.data.length);
    }

    public BigInteger value() {
        return new BigInteger(1, this.data);
    }

    public int intValue() {
        int intVal = 0;
        byte[] var2 = this.data;
        int var3 = var2.length;

        for (int var4 = 0; var4 < var3; ++var4) {
            byte aData = var2[var4];
            intVal = (intVal << 8) + (aData & 255);
        }

        return intVal;
    }

    public int intValueSafe() {
        int bytesOccupied = this.bytesOccupied();
        int intValue = this.intValue();
        return bytesOccupied <= 4 && intValue >= 0 ? intValue : 2147483647;
    }

    public long longValue() {
        long longVal = 0L;
        byte[] var3 = this.data;
        int var4 = var3.length;

        for (int var5 = 0; var5 < var4; ++var5) {
            byte aData = var3[var5];
            longVal = (longVal << 8) + (long) (aData & 255);
        }

        return longVal;
    }

    public long longValueSafe() {
        int bytesOccupied = this.bytesOccupied();
        long longValue = this.longValue();
        return bytesOccupied <= 8 && longValue >= 0L ? longValue : 9223372036854775807L;
    }

    public BigInteger sValue() {
        return new BigInteger(this.data);
    }

    public String bigIntValue() {
        return (new BigInteger(this.data)).toString();
    }

    public boolean isZero() {
        if (this == ZERO) {
            return true;
        } else {
            return this.compareTo(ZERO) == 0;
        }
    }

    public boolean isNegative() {
        int result = this.data[0] & 128;
        return result == 128;
    }

    public DataWord and(DataWord word) {
        byte[] newData = this.copyData();

        for (int i = 0; i < this.data.length; ++i) {
            newData[i] &= word.data[i];
        }

        return new DataWord(newData);
    }

    public DataWord or(DataWord word) {
        byte[] newData = this.copyData();

        for (int i = 0; i < this.data.length; ++i) {
            newData[i] |= word.data[i];
        }

        return new DataWord(newData);
    }

    public DataWord xor(DataWord word) {
        byte[] newData = this.copyData();

        for (int i = 0; i < this.data.length; ++i) {
            newData[i] ^= word.data[i];
        }

        return new DataWord(newData);
    }

    public DataWord negate() {
        return this.isZero() ? ZERO : this.bnot().add(ONE);
    }

    public DataWord bnot() {
        return this.isZero() ? new DataWord(ByteUtil.copyToArray(MAX_VALUE)) : new DataWord(ByteUtil.copyToArray(MAX_VALUE.subtract(this.value())));
    }

    public DataWord add(DataWord word) {
        byte[] newData = new byte[32];
        int i = 31;

        for (int overflow = 0; i >= 0; --i) {
            int v = (this.data[i] & 255) + (word.data[i] & 255) + overflow;
            newData[i] = (byte) v;
            overflow = v >>> 8;
        }

        return new DataWord(newData);
    }

    public DataWord add2(DataWord word) {
        BigInteger result = this.value().add(word.value());
        return new DataWord(ByteUtil.copyToArray(result.and(MAX_VALUE)));
    }

    public DataWord mul(DataWord word) {
        BigInteger result = this.value().multiply(word.value());
        return new DataWord(ByteUtil.copyToArray(result.and(MAX_VALUE)));
    }

    public DataWord div(DataWord word) {
        if (word.isZero()) {
            return ZERO;
        } else {
            BigInteger result = this.value().divide(word.value());
            return new DataWord(ByteUtil.copyToArray(result.and(MAX_VALUE)));
        }
    }

    public DataWord sDiv(DataWord word) {
        if (word.isZero()) {
            return ZERO;
        } else {
            BigInteger result = this.sValue().divide(word.sValue());
            return new DataWord(ByteUtil.copyToArray(result.and(MAX_VALUE)));
        }
    }

    public DataWord sub(DataWord word) {
        BigInteger result = this.value().subtract(word.value());
        return new DataWord(ByteUtil.copyToArray(result.and(MAX_VALUE)));
    }

    public DataWord exp(DataWord word) {
        BigInteger newData = this.value().modPow(word.value(), _2_256);
        return new DataWord(ByteUtil.copyToArray(newData));
    }

    public DataWord mod(DataWord word) {
        if (word.isZero()) {
            return ZERO;
        } else {
            BigInteger result = this.value().mod(word.value());
            return new DataWord(ByteUtil.copyToArray(result.and(MAX_VALUE)));
        }
    }

    public DataWord sMod(DataWord word) {
        if (word.isZero()) {
            return ZERO;
        } else {
            BigInteger result = this.sValue().abs().mod(word.sValue().abs());
            result = this.sValue().signum() == -1 ? result.negate() : result;
            return new DataWord(ByteUtil.copyToArray(result.and(MAX_VALUE)));
        }
    }

    public DataWord addmod(DataWord word1, DataWord word2) {
        if (word2.isZero()) {
            return ZERO;
        } else {
            BigInteger result = this.value().add(word1.value()).mod(word2.value());
            return new DataWord(ByteUtil.copyToArray(result.and(MAX_VALUE)));
        }
    }

    public DataWord mulmod(DataWord word1, DataWord word2) {
        if (!this.isZero() && !word1.isZero() && !word2.isZero()) {
            BigInteger result = this.value().multiply(word1.value()).mod(word2.value());
            return new DataWord(ByteUtil.copyToArray(result.and(MAX_VALUE)));
        } else {
            return ZERO;
        }
    }

    public DataWord shiftLeft(DataWord arg) {
        if (arg.value().compareTo(BigInteger.valueOf(256L)) >= 0) {
            return ZERO;
        } else {
            BigInteger result = this.value().shiftLeft(arg.intValueSafe());
            return new DataWord(ByteUtil.copyToArray(result.and(MAX_VALUE)));
        }
    }

    public DataWord shiftRight(DataWord arg) {
        if (arg.value().compareTo(BigInteger.valueOf(256L)) >= 0) {
            return ZERO;
        } else {
            BigInteger result = this.value().shiftRight(arg.intValueSafe());
            return new DataWord(ByteUtil.copyToArray(result.and(MAX_VALUE)));
        }
    }

    public DataWord shiftRightSigned(DataWord arg) {
        if (arg.value().compareTo(BigInteger.valueOf(256L)) >= 0) {
            return this.isNegative() ? ONE.negate() : ZERO;
        } else {
            BigInteger result = this.sValue().shiftRight(arg.intValueSafe());
            return new DataWord(ByteUtil.copyToArray(result.and(MAX_VALUE)));
        }
    }

    @JsonValue
    @Override
    public String toString() {
        return ByteUtil.toHexString(this.data);
    }

    public String toPrefixString() {
        byte[] pref = this.getNoLeadZeroesData();
        if (pref.length == 0) {
            return "";
        } else {
            return pref.length < 7 ? Hex.toHexString(pref) : Hex.toHexString(pref).substring(0, 6);
        }
    }

    public String shortHex() {
        String hexValue = Hex.toHexString(this.getNoLeadZeroesData()).toUpperCase();
        return "0x" + hexValue.replaceFirst("^0+(?!$)", "");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o != null && this.getClass() == o.getClass()) {
            DataWord that = (DataWord) o;
            return Arrays.equals(this.data, that.data);
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(this.data);
    }

    @Override
    public int compareTo(DataWord o) {
        if (o == null) {
            return -1;
        } else {
            int result = FastByteComparisons.compareTo(this.data, 0, this.data.length, o.data, 0, o.data.length);
            return (int) Math.signum((float) result);
        }
    }

    public DataWord signExtend(byte k) {
        if (0 <= k && k <= 31) {
            byte mask = (byte) (this.sValue().testBit(k * 8 + 7) ? -1 : 0);
            byte[] newData = this.copyData();

            for (int i = 31; i > k; --i) {
                newData[31 - i] = (byte) mask;
            }

            return new DataWord(newData);
        } else {
            throw new IndexOutOfBoundsException();
        }
    }

    public int bytesOccupied() {
        int firstNonZero = ByteUtil.firstNonZeroByte(this.data);
        return firstNonZero == -1 ? 0 : 31 - firstNonZero + 1;
    }

    public boolean isHex(String hex) {
        return Hex.toHexString(this.data).equals(hex);
    }

    public String asString() {
        return new String(this.getNoLeadZeroesData());
    }

    static {
        MAX_VALUE = _2_256.subtract(BigInteger.ONE);
        ZERO = new DataWord(new byte[32]);
        ONE = of((byte) 1);
    }
}
