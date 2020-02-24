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
