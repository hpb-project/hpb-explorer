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

import com.hpb.bc.model.HpbHash;

/**
 * Created by davidroon on 03.04.17.
 * This code is released under Apache 2 license
 */
public class EventInfo<T> {
    private final HpbHash transactionHash;
    private final T result;

    public EventInfo(HpbHash transactionHash, T result) {
        this.transactionHash = transactionHash;
        this.result = result;
    }

    public HpbHash getTransactionHash() {
        return transactionHash;
    }

    public T getResult() {
        return result;
    }

    @Override
    public String toString() {
        return "EventInfo{" +
                "transactionHash=" + transactionHash +
                ", result=" + result +
                '}';
    }
}
