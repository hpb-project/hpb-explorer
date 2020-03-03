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

package com.hpb.bc.util;


import com.hpb.bc.exception.ApiException;
import com.hpb.bc.exception.InvalidAddressException;
import com.hpb.bc.exception.InvalidTxHashException;

import javax.validation.constraints.NotNull;
import java.lang.reflect.Field;
import java.math.BigInteger;
import java.util.*;
import java.util.regex.Pattern;


public class BasicUtils {

    private static final int MAX_END_BLOCK = 999999999;
    private static final int MIN_START_BLOCK = 0;

    private static final Pattern ADDRESS_PATTERN = Pattern.compile("0x[a-zA-Z0-9]{40}");
    private static final Pattern TXHASH_PATTERN = Pattern.compile("0x[a-zA-Z0-9]{64}");
    private static final Pattern HEX_PATTERN = Pattern.compile("[a-zA-Z0-9]+");

    public static boolean isEmpty(String value) {
        return value == null || value.isEmpty();
    }

    public static boolean isBlank(String value) {
        return value == null || value.isEmpty() || value.trim().isEmpty();
    }

    public static <T> boolean isEmpty(Collection<T> collection) {
        return (collection == null || collection.isEmpty());
    }


    public static long compensateMinBlock(long blockNumber) {
        return (blockNumber < MIN_START_BLOCK)
                ? MIN_START_BLOCK
                : blockNumber;
    }

    public static long compensateMaxBlock(long blockNumber) {
        return (blockNumber > MAX_END_BLOCK)
                ? MAX_END_BLOCK
                : blockNumber;
    }

    public static boolean isNotAddress(String value) {
        return isEmpty(value) || !ADDRESS_PATTERN.matcher(value).matches();
    }

    public static boolean isNotTxHash(String value) {
        return isEmpty(value) || !TXHASH_PATTERN.matcher(value).find();
    }

    public static boolean isNotHex(String value) {
        return isEmpty(value) || !HEX_PATTERN.matcher(value).matches();
    }

    public static BigInteger parseHex(String hex) {
        try {
            if (BasicUtils.isEmpty(hex)) {
                return BigInteger.valueOf(0);
            }

            final String formatted = (hex.length() > 2 && hex.charAt(0) == '0' && hex.charAt(1) == 'x')
                    ? hex.substring(2)
                    : hex;

            return new BigInteger(formatted, 16);
        } catch (NumberFormatException e) {
            return BigInteger.valueOf(-1L);
        }
    }

    public static void validateAddress(String address) {
        if (isNotAddress(address)) {
            throw new InvalidAddressException("Address [" + address + "] is not Ethereum based.");
        }
    }

    public static void validateTxHash(String txhash) {
        if (isNotTxHash(txhash)) {
            throw new InvalidTxHashException("TxHash [" + txhash + "] is not Ethereum based.");
        }
    }


    public static void validateAddresses(List<String> addresses) {
        for (String address : addresses) {
            if (isNotAddress(address)) {
                throw new InvalidAddressException("Address [" + address + "] is not Ethereum based.");
            }
        }
    }

    @NotNull
    public static List<List<String>> partition(List<String> list, int pairSize) {
        if (isEmpty(list)) {
            return Collections.emptyList();
        }
        final List<List<String>> partitioned = new ArrayList<>();
        final Iterator<String> iterator = list.iterator();
        int counter = 0;

        List<String> temp = new ArrayList<>();
        while (iterator.hasNext()) {
            temp.add(iterator.next());

            if (++counter > pairSize) {
                partitioned.add(temp);
                temp = new ArrayList<>();
                counter = 0;
            }
        }

        if (!temp.isEmpty()) {
            partitioned.add(temp);
        }
        return partitioned;
    }

    public static Map<String, String> convertClassToMap(Object object) {
        Class clazz = object.getClass();
        Map<String, String> map = new TreeMap<>();
        try {

            for (; clazz != Object.class; clazz = clazz.getSuperclass()) {
                Field[] field = clazz.getDeclaredFields();
                for (Field f : field) {
                    f.setAccessible(true);
                    if (f.get(object) == null) {
                        map.put(f.getName(), "");
                    } else {
                        map.put(f.getName(), f.get(object).toString());
                    }
                }
            }
        } catch (IllegalAccessException e) {
            throw new ApiException(e.getMessage(), e);
        }
        return map;
    }
}
