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


import com.hpb.bc.rpc.SmartContract;

import java.lang.reflect.Method;
import java.util.concurrent.CompletableFuture;

/**
 * Created by davidroon on 06.06.17.
 */
public class HpbPayableCall<T> {

    private final SmartContract contract;
    private final Method method;
    private final Object[] arguments;

    public HpbPayableCall(SmartContract contract, Method method, Object[] arguments) {
        this.contract = contract;
        this.method = method;
        this.arguments = arguments;
    }

    public CompletableFuture<HpbPropellerCall<T>> with(HpbValue value) {
        return contract.callFunctionAndGetDetails(value, method, arguments)
                .thenApply(details -> (HpbPropellerCall<T>) new HpbPropellerCall<>(details.getNonce(), details.getGasEstimate(), details.getTxHash(), contract.transformDetailsToResult(details, method)));
    }
}
