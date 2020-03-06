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

/**
 * Created by davidroon on 03.04.17.
 * This code is released under Apache 2 license
 */
public class GasPrice {
    private final HpbValue price;

    public GasPrice(HpbValue price) {
        this.price = price;
    }

    public HpbValue getPrice() {
        return price;
    }

    @Override
    public String toString() {
        return "GasPrice{" +
                "price=" + price +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GasPrice gasPrice = (GasPrice) o;

        return price != null ? price.equals(gasPrice.price) : gasPrice.price == null;
    }

    @Override
    public int hashCode() {
        return price != null ? price.hashCode() : 0;
    }
}
