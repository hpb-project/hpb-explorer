package com.hpb.bc.rpc;


import com.hpb.bc.exception.ApiException;
import com.hpb.bc.model.HpbData;
import com.hpb.bc.propeller.converts.future.CompletableFutureConverter;
import com.hpb.bc.propeller.converts.future.FutureConverter;
import com.hpb.bc.solidity.SolidityContractDetails;
import com.hpb.bc.solidity.SolidityFunction;
import com.hpb.bc.solidity.values.CallDetails;
import com.hpb.bc.solidity.values.HpbAccount;
import com.hpb.bc.solidity.values.HpbAddress;
import com.hpb.bc.solidity.values.SmartContractInfo;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.hpb.bc.solidity.values.HpbValue;

import static com.hpb.bc.solidity.values.HpbValue.wei;


/**
 * Created by davidroon on 31.03.16.
 * This code is released under Apache 2 license
 */
class HpbContractInvocationHandler implements InvocationHandler {

    private final Map<HpbAddress, Map<HpbAccount, SmartContract>> contracts = new HashMap<>();
    private final HpbProxy hpbProxy;
    private final Map<Object, SmartContractInfo> info = new HashMap<>();
    private final List<FutureConverter> futureConverters = new ArrayList<>();


    HpbContractInvocationHandler(HpbProxy hpbProxy) {
        this.hpbProxy = hpbProxy;
        this.futureConverters.add(new CompletableFutureConverter());
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (isDefaultObjectClassMethod(method)) {
            return handleDefaultObjectClassMethod(proxy, method, args);
        }

        SmartContractInfo contractInfo = info.get(proxy);
        SmartContract contract = contracts.get(contractInfo.getAddress()).get(contractInfo.getAccount());
        Object[] arguments = Optional.ofNullable(args).orElse(new Object[0]);

        if (method.getReturnType().equals(Void.TYPE)) {
            try {
                contract.callFunction(method, arguments).get();
            } catch (ExecutionException e) {
                throw e.getCause();
            }

            return Void.TYPE;
        } else {
            return findConverter(method.getReturnType()).map(converter -> {
                if (converter.isFutureType(method.getReturnType())) {
                    return converter.convert(contract.callFunction(method, arguments));
                }
                if (converter.isPayableType(method.getReturnType())) {
                    return converter.getPayable(contract, arguments, method);
                }

                if (converter.isFutureTypeWithDetails(method.getReturnType())) {
                    try {
                        CallDetails details = contract.callFunctionAndGetDetails(wei(0), method, args).get();
                        CompletableFuture<?> futureResult = contract.transformDetailsToResult(details, method);
                        return converter.convertWithDetails(details, futureResult);
                    } catch (InterruptedException | ExecutionException e) {
                        throw new ApiException(e.getMessage(), e);
                    }
                }

                if (converter.isPayableTypeWithDetails(method.getReturnType())) {
                    return converter.getPayableWithDetails(contract, arguments, method);
                }


                throw new ApiException("serious bug! the type \"" + method.getReturnType().getSimpleName() + "\" is define has a type from FutureConverter but none found!");

            }).orElseGet(() -> contract.callConstFunction(method, wei(0), arguments));
        }
    }

    private Object handleDefaultObjectClassMethod(Object proxy, Method method, Object[] args) {
        switch (method.getName()) {
            case "toString":
                SmartContractInfo contractInfo = info.get(proxy);
                return "Smart contract proxy \naccount:" + contractInfo.getAccount().getAddress().withLeading0x() + "\ncontract address:" + contractInfo.getAddress().withLeading0x();
            case "equals":
                return proxy == args[0];
            case "hashCode":
                return 0;
            default:
                throw new ApiException("unhandled default Object method " + method.getName() + ". please fill an issue if you need this method");
        }
    }

    private boolean isDefaultObjectClassMethod(Method method) {
        return method.getDeclaringClass().equals(Object.class);
    }

    private Optional<FutureConverter> findConverter(Class type) {
        return futureConverters.stream()
                .filter(converter -> converter.isAnyFutureDependentType(type))
                .findFirst();
    }

    <T> void register(T proxy, Class<T> contractInterface, SolidityContractDetails contract, HpbAddress address, HpbAccount account) {
        if (address.isEmpty()) {
            throw new ApiException("the contract address cannot be empty");
        }
        SmartContract smartContract = hpbProxy.getSmartContract(contract, address, account);
        verifyContract(smartContract, contractInterface);

        info.put(proxy, new SmartContractInfo(address, account));
        Map<HpbAccount, SmartContract> proxies = contracts.getOrDefault(address, new HashMap<>());
        proxies.put(account, smartContract);
        contracts.put(address, proxies);
    }

    private void verifyContract(SmartContract smartContract, Class<?> contractInterface) {
        Set<Method> interfaceMethods = new HashSet<>(Arrays.asList(contractInterface.getMethods()));
        Set<SolidityFunction> solidityFunctions = new HashSet<>(smartContract.getFunctions());

        Map<Method, Optional<SolidityFunction>> matches = interfaceMethods.stream()
                .collect(Collectors.toMap(Function.identity(), method -> solidityFunctions.stream()
                        .filter(solidityMethod -> solidityMethod.getName().equals(method.getName()) && solidityMethod.matchParams(method.getParameterTypes())).findFirst()));

        matches.forEach((key, optValue) -> optValue
                .map(value -> validateReturnValue(key, value))
                .orElseThrow(() -> new ApiException(generateUnmatchedMethodsError(solidityFunctions, interfaceMethods))));
    }

    private boolean validateReturnValue(Method method, SolidityFunction value) {
        findConverter(method.getReturnType())
                .map(converter -> {
                    value.decode(HpbData.empty(), getGenericType(method.getGenericReturnType()));
                    return true;
                })
                .orElseGet(() -> {
                    value.decode(HpbData.empty(), method.getGenericReturnType());
                    return true;
                });
        return true;
    }

    private String generateUnmatchedMethodsError(Set<SolidityFunction> solidityFunctions, Set<Method> interfaceMethods) {
        String unmatchedSolidityMethods = solidityFunctions.stream().filter(solidityMethod -> interfaceMethods.stream()
                .noneMatch(method -> solidityMethod.getName().equals(method.getName()) && solidityMethod.matchParams(method.getParameterTypes())))
                .map(func -> "- " + func.toString())
                .collect(Collectors.joining("\n"));

        List<Method> unmatched = interfaceMethods.stream().filter(method -> solidityFunctions.stream()
                .noneMatch(solidityMethod -> solidityMethod.getName().equals(method.getName()) && solidityMethod.matchParams(method.getParameterTypes())))
                .collect(Collectors.toList());

        String functions = unmatched.stream()
                .map(method -> "- " + method.getName() + "(" + Arrays.stream(method.getParameterTypes()).map(Class::getSimpleName).collect(Collectors.joining(", ")) + ")")
                .collect(Collectors.joining("\n"));

        return "*** unmatched *** \nsolidity:\n" + unmatchedSolidityMethods +
                "\njava:\n" + functions;
    }

    void addFutureConverter(final FutureConverter futureConverter) {
        futureConverters.add(futureConverter);
    }

    private Class<?> getGenericType(Type genericType) {
        return (Class<?>) ((ParameterizedType) genericType).getActualTypeArguments()[0];
    }
}
