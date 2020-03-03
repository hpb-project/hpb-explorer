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

import java.math.BigInteger;

/**
 * Created by davidroon on 03.04.17.
 * This code is released under Apache 2 license
 */
public class GasUsage {
    private final BigInteger usage;

    public GasUsage(BigInteger usage) {
        this.usage = usage;
    }

    public BigInteger getUsage() {
        return usage;
    }

    public GasUsage add(int additionalGasForContractCreation) {
        return new GasUsage(this.usage.add(BigInteger.valueOf(additionalGasForContractCreation)));
    }

    @Override
    public String toString() {
        return "GasUsage{" +
                "usage=" + usage +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        GasUsage gasUsage = (GasUsage) o;

        return usage != null ? usage.equals(gasUsage.usage) : gasUsage.usage == null;
    }

    @Override
    public int hashCode() {
        return usage != null ? usage.hashCode() : 0;
    }
}
