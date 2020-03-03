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
import com.hpb.bc.solidity.values.HpbPayableCall;
import com.hpb.bc.solidity.values.HpbPropellerCall;
import com.hpb.bc.solidity.values.Payable;
import io.hpb.web3.protocol.core.methods.response.HpbCall;

import java.lang.reflect.Method;
import java.util.concurrent.CompletableFuture;

/**
 * Created by davidroon on 26.02.17.
 * This code is released under Apache 2 license
 */
public class CompletableFutureConverter implements FutureConverter {
    @Override
    public CompletableFuture convert(CompletableFuture future) {
        return future;
    }

    @Override
    public HpbPropellerCall convertWithDetails(CallDetails details, CompletableFuture<?> futureResult) {
        return new HpbPropellerCall<>(details.getNonce(), details.getGasEstimate(), details.getTxHash(), futureResult);
    }

    @Override
    public boolean isFutureType(Class cls) {
        return CompletableFuture.class.equals(cls);
    }

    @Override
    public boolean isFutureTypeWithDetails(Class cls) {
        return HpbCall.class.equals(cls);
    }

    @Override
    public boolean isPayableType(Class cls) {
        return Payable.class.equals(cls);
    }

    @Override
    public boolean isPayableTypeWithDetails(Class cls) {
        return HpbPayableCall.class.equals(cls);
    }

    public boolean isPayableWithDetailsType(Class cls) {
        return HpbPayableCall.class.equals(cls);
    }

    @Override
    public Payable getPayable(SmartContract smartContract, Object[] arguments, Method method) {
        return new Payable(smartContract, method, arguments);
    }

    @Override
    public HpbPayableCall getPayableWithDetails(SmartContract smartContract, Object[] arguments, Method method) {
        return new HpbPayableCall(smartContract, method, arguments);
    }
}
