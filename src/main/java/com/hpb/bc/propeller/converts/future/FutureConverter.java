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

package com.hpb.bc.propeller.converts.future;

import com.hpb.bc.rpc.SmartContract;
import com.hpb.bc.solidity.values.CallDetails;
import com.hpb.bc.solidity.values.HpbPropellerCall;

import java.lang.reflect.Method;
import java.util.concurrent.CompletableFuture;

/**
 * Created by davidroon on 26.02.17.
 * This code is released under Apache 2 license
 */
public interface FutureConverter {
    <T> Object convert(CompletableFuture<T> future);

    HpbPropellerCall convertWithDetails(CallDetails details, CompletableFuture<?> futureResult);

    boolean isFutureType(Class cls);

    boolean isFutureTypeWithDetails(Class cls);

    boolean isPayableType(Class cls);

    boolean isPayableTypeWithDetails(Class cls);

    default boolean isAnyFutureDependentType(Class cls) {
        return isFutureType(cls) ||
                isFutureTypeWithDetails(cls) ||
                isPayableType(cls) ||
                isPayableTypeWithDetails(cls);
    }

    Object getPayable(SmartContract smartContract, Object[] arguments, Method method);

    Object getPayableWithDetails(SmartContract smartContract, Object[] arguments, Method method);

}
