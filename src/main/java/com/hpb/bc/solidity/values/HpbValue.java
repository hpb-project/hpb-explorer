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

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.Objects;

/**
 * Created by davidroon on 06.11.16.
 * This code is released under Apache 2 license
 */
public class HpbValue implements Comparable<HpbValue> {
    private static final BigDecimal ETHER_CONVERSION = BigDecimal.valueOf(1_000_000_000_000_000_000L);
    private final BigDecimal value;

    public HpbValue(BigInteger value) {
        if (value.signum() == -1) {
            throw new ApiException("a value cannot be negative");
        }

        this.value = new BigDecimal(value);
    }

    public static HpbValue hpber(final BigInteger value) {
        return wei(value.multiply(ETHER_CONVERSION.toBigInteger()));
    }

    public static HpbValue hpber(final Double value) {
        return hpber(BigDecimal.valueOf(value));
    }

    public static HpbValue hpber(final BigDecimal value) {
        return wei(ETHER_CONVERSION.multiply(value).toBigInteger());
    }

    public static HpbValue hpber(final long value) {
        return hpber(BigInteger.valueOf(value));
    }

    public static HpbValue wei(final int value) {
        return wei(BigInteger.valueOf(value));
    }

    public static HpbValue wei(final long value) {
        return wei(BigInteger.valueOf(value));
    }

    public static HpbValue wei(final BigInteger value) {
        return new HpbValue(value);
    }

    public BigInteger inWei() {
        return value.toBigInteger();
    }

    public BigDecimal inHpb() {
        return value.divide(ETHER_CONVERSION, RoundingMode.HALF_DOWN);
    }

    public boolean isZero() {
        return inWei().signum() != 1;
    }

    public HpbValue plus(HpbValue value) {
        return new HpbValue(this.value.add(value.value).toBigInteger());
    }

    public HpbValue minus(HpbValue value) {
        return new HpbValue(this.value.subtract(value.value).toBigInteger());
    }

    @Override
    public int compareTo(HpbValue o) {
        return value.compareTo(o.value);
    }

    @Override
    public boolean equals(Object o) {
        return o != null && Objects.equals(value, ((HpbValue) o).value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public String toString() {
        return value + " Wei";
    }
}
