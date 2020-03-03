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


public abstract class FastByteComparisons {
    public FastByteComparisons() {
    }

    public static boolean equal(byte[] b1, byte[] b2) {
        return b1.length == b2.length && compareTo(b1, 0, b1.length, b2, 0, b2.length) == 0;
    }

    public static int compareTo(byte[] b1, int s1, int l1, byte[] b2, int s2, int l2) {
        return FastByteComparisons.LexicographicalComparerHolder.BEST_COMPARER.compareTo(b1, s1, l1, b2, s2, l2);
    }

    private static FastByteComparisons.Comparer<byte[]> lexicographicalComparerJavaImpl() {
        return FastByteComparisons.LexicographicalComparerHolder.PureJavaComparer.INSTANCE;
    }

    private static class LexicographicalComparerHolder {
        static final String UNSAFE_COMPARER_NAME = FastByteComparisons.LexicographicalComparerHolder.class.getName() + "$UnsafeComparer";
        static final FastByteComparisons.Comparer<byte[]> BEST_COMPARER = getBestComparer();

        private LexicographicalComparerHolder() {
        }

        static FastByteComparisons.Comparer<byte[]> getBestComparer() {
            try {
                Class<?> theClass = Class.forName(UNSAFE_COMPARER_NAME);
                FastByteComparisons.Comparer<byte[]> comparer = (FastByteComparisons.Comparer) theClass.getEnumConstants()[0];
                return comparer;
            } catch (Throwable var2) {
                return FastByteComparisons.lexicographicalComparerJavaImpl();
            }
        }

        private static enum PureJavaComparer implements FastByteComparisons.Comparer<byte[]> {
            INSTANCE;

            private PureJavaComparer() {
            }

            @Override
            public int compareTo(byte[] buffer1, int offset1, int length1, byte[] buffer2, int offset2, int length2) {
                if (buffer1 == buffer2 && offset1 == offset2 && length1 == length2) {
                    return 0;
                } else {
                    int end1 = offset1 + length1;
                    int end2 = offset2 + length2;
                    int i = offset1;

                    for (int j = offset2; i < end1 && j < end2; ++j) {
                        int a = buffer1[i] & 255;
                        int b = buffer2[j] & 255;
                        if (a != b) {
                            return a - b;
                        }

                        ++i;
                    }

                    return length1 - length2;
                }
            }
        }
    }

    private interface Comparer<T> {
        int compareTo(T var1, int var2, int var3, T var4, int var5, int var6);
    }
}
