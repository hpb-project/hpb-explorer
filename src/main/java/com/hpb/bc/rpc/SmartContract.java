package com.hpb.bc.rpc;


import com.hpb.bc.exception.ApiException;
import com.hpb.bc.model.HpbData;
import com.hpb.bc.solidity.SolidityContractDetails;
import com.hpb.bc.solidity.SolidityFunction;
import com.hpb.bc.solidity.SolidityType;
import com.hpb.bc.solidity.abi.AbiEntry;
import com.hpb.bc.solidity.converters.decoders.SolidityTypeDecoder;
import com.hpb.bc.solidity.converters.encoders.SolidityTypeEncoder;
import com.hpb.bc.solidity.values.CallDetails;
import com.hpb.bc.solidity.values.HpbAccount;
import com.hpb.bc.solidity.values.HpbAddress;
import com.hpb.bc.solidity.values.HpbValue;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import static com.hpb.bc.solidity.values.HpbValue.wei;


/**
 * Created by davidroon on 20.04.16.
 * This code is released under Apache 2 license
 */
public class SmartContract {
    private final HpbAddress address;
    private final HpbBackend ethereum;
    private final SolidityContractDetails contract;
    private final HpbProxy proxy;
    private final HpbAccount account;

    SmartContract(SolidityContractDetails contract, HpbAccount account, HpbAddress address, HpbProxy proxy, HpbBackend ethereum) {
        this.contract = contract;
        this.account = account;
        this.proxy = proxy;
        this.address = address;
        this.ethereum = ethereum;
    }

    public List<SolidityFunction> getFunctions() {
        return contract.getAbi().stream()
                .filter(entry -> "function".equals(entry.getType()))
                .map(this::buildFunction)
                .collect(Collectors.toList());
    }

    public List<SolidityFunction> getConstructors() {
        return contract.getAbi().stream()
                .filter(entry -> "constructor".equals(entry.getType()))
                .map(this::buildFunction)
                .collect(Collectors.toList());
    }

    private SolidityFunction buildFunction(AbiEntry entry) {
        List<SolidityType> parameters = entry.getInputs().stream()
                .map(abiParam -> SolidityType.find(abiParam.getType()).<ApiException>orElseThrow(() -> new ApiException("unknown type " + abiParam.getType()))).collect(Collectors.toList());

        return new SolidityFunction(entry, parameters, getEncoders(entry), getDecoders(entry));
    }

    private List<List<SolidityTypeDecoder>> getDecoders(AbiEntry entry) {
        return entry.getOutputs().stream()
                .map(proxy::getDecoders)
                .collect(Collectors.toList());
    }

    private List<List<SolidityTypeEncoder>> getEncoders(AbiEntry entry) {
        return entry.getInputs().stream()
                .map(proxy::getEncoders)
                .collect(Collectors.toList());
    }

    Object callConstFunction(Method method, HpbValue value, Object... args) {
        return getFunction(method).map(func -> {
            HpbData data = func.encode(args);
            if (method.getGenericReturnType() instanceof Class && proxy.isVoidType((Class<?>) method.getGenericReturnType())) {
                return null;
            }
            return func.decode(ethereum.constantCall(account, address, value, data), method.getGenericReturnType());
        }).orElseThrow(() -> new ApiException("could not find the function " + method.getName() + " that maches the arguments"));
    }

    CompletableFuture<?> callFunction(Method method, Object... args) {
        return callFunction(wei(0), method, args);
    }

    public CompletableFuture<?> callFunction(HpbValue value, Method method, Object... args) {
        return callFunctionAndGetDetails(value, method, args)
                .thenCompose(callDetails -> this.transformDetailsToResult(callDetails, method));
    }

    private SolidityFunction getFunctionOrThrow(Method method) {
        return getFunction(method)
                .orElseThrow(() -> new ApiException("function " + method.getName() + " cannot be found. available:" + getAvailableFunctions()));
    }

    public CompletableFuture<?> transformDetailsToResult(CallDetails details, Method method) {
        return details.getResult().thenApply(receipt -> {
            SolidityFunction func = getFunctionOrThrow(method);
            Class<?> returnType = getGenericType(method.getGenericReturnType());
            if (proxy.isVoidType(returnType)) {
                return null;
            }

            return func.decode(receipt.executionResult, returnType);
        });
    }

    public CompletableFuture<CallDetails> callFunctionAndGetDetails(HpbValue value, Method method, Object... args) {
        return getFunction(method)
                .map(func -> callFunctionAndGetDetails(value, func, args))
                .orElseThrow(() -> new ApiException("function " + method.getName() + " cannot be found. available:" + getAvailableFunctions()));
    }

    public CompletableFuture<CallDetails> callFunctionAndGetDetails(HpbValue value, SolidityFunction func, Object... args) {
        HpbData functionCallBytes = func.encode(args);
        return proxy.sendTx(value, functionCallBytes, account, address);
    }

    private String getAvailableFunctions() {
        return getFunctions().stream()
                .map(SolidityFunction::getName)
                .collect(Collectors.toList()).toString();
    }

    public HpbAddress getAddress() {
        return address;
    }

    private Optional<SolidityFunction> getFunction(Method method) {
        return getFunctions().stream()
                .filter(function -> method.getName().equals(function.getName()) && function.matchParams(method.getParameterTypes()))
                .findFirst();
    }

    Optional<SolidityFunction> getConstructor(Object[] args) {
        return getConstructors().stream()
                .filter(func -> func.matchParams(args)).findFirst();
    }

    private Class<?> getGenericType(Type genericType) {
        return (Class<?>) ((ParameterizedType) genericType).getActualTypeArguments()[0];
    }
}
