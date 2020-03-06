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
 * Created by davidroon on 15.01.17.
 * This code is released under Apache 2 license
 */
public class Payable<T> {

    private final SmartContract contract;
    private final Method method;
    private final Object[] arguments;

    public Payable(SmartContract contract, Method method, Object[] arguments) {
        this.contract = contract;
        this.method = method;
        this.arguments = arguments;
    }

    public CompletableFuture<T> with(HpbValue value) {
        return (CompletableFuture<T>) contract.callFunction(value, method, arguments);
    }

}
