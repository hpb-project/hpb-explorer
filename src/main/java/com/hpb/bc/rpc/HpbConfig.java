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

package com.hpb.bc.rpc;


import com.hpb.bc.solidity.values.GasPrice;


public class HpbConfig {

    private final long blockWaitLimit;
    private final GasPrice gasPrice;

    public HpbConfig(long blockWaitLimit, GasPrice gasPrice) {

        this.blockWaitLimit = blockWaitLimit;
        this.gasPrice = gasPrice;
    }


    public long blockWaitLimit() {
        return blockWaitLimit;
    }

    public GasPrice getGasPrice() {
        return gasPrice;
    }

    public static class Builder {

        protected long blockWaitLimit = 16;
        protected GasPrice gasPrice;

        public HpbConfig build() {
            return new HpbConfig(blockWaitLimit, gasPrice);
        }

    }
}
