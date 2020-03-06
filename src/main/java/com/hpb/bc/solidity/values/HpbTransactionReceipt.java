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

import com.hpb.bc.model.EventData;
import com.hpb.bc.model.HpbData;
import com.hpb.bc.model.HpbHash;

import java.util.List;

/**
 * Created by davidroon on 03.02.17.
 * This code is released under Apache 2 license
 */
public class HpbTransactionReceipt {
    public final HpbHash hash;
    public final HpbHash blockHash;
    public final HpbAddress sender;
    public final HpbAddress receiveAddress;
    public final HpbAddress contractAddress;
    public final HpbData callData;
    public final String error;
    public final HpbData executionResult;
    public final boolean isSuccessful;
    public final List<EventData> events;
    public final HpbValue ethValue;

    public HpbTransactionReceipt(HpbHash hash, HpbHash blockHash, HpbAddress sender, HpbAddress receiveAddress, HpbAddress contractAddress, HpbData callData, String error, HpbData executionResult, boolean isSuccessful, List<EventData> events, HpbValue ethValue) {
        this.hash = hash;
        this.blockHash = blockHash;
        this.sender = sender;
        this.receiveAddress = receiveAddress;
        this.contractAddress = contractAddress;
        this.callData = callData;
        this.error = error;
        this.executionResult = executionResult;
        this.isSuccessful = isSuccessful;
        this.events = events;
        this.ethValue = ethValue;
    }
}
