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
import com.hpb.bc.solidity.values.Nonce;

import java.util.concurrent.CompletableFuture;

/**
 * Created by davidroon on 06.06.17.
 */
public class CallDetails {
    private final CompletableFuture<HpbTransactionReceipt> result;
    private final HpbHash txHash;
    private final Nonce nonce;
    private final GasUsage gasEstimate;

    public CallDetails(CompletableFuture<HpbTransactionReceipt> result, HpbHash txHash, Nonce nonce, GasUsage gasEstimate) {
        this.result = result;
        this.txHash = txHash;
        this.nonce = nonce;
        this.gasEstimate = gasEstimate;
    }


    public CompletableFuture<HpbTransactionReceipt> getResult() {
        return result;
    }

    public Nonce getNonce() {
        return nonce;
    }

    public GasUsage getGasEstimate() {
        return gasEstimate;
    }

    public HpbHash getTxHash() {
        return txHash;
    }
}
