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

package com.hpb.bc.solidity;


import com.hpb.bc.exception.ApiException;
import com.hpb.bc.model.HpbData;
import com.hpb.bc.solidity.abi.AbiEntry;
import com.hpb.bc.solidity.abi.AbiParam;
import com.hpb.bc.solidity.converters.decoders.SolidityTypeDecoder;
import com.hpb.bc.solidity.converters.encoders.NumberEncoder;
import com.hpb.bc.solidity.converters.encoders.SolidityTypeEncoder;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.hpb.bc.model.HpbData.WORD_SIZE;


/**
 * Created by davidroon on 30.03.17.
 * This code is released under Apache 2 license
 */
public class SolidityFunction {
    private final NumberEncoder numberEncoder = new NumberEncoder();
    private final AbiEntry description;
    private final List<SolidityType> parameters;
    private final List<List<SolidityTypeEncoder>> encoders;
    private final List<List<SolidityTypeDecoder>> decoders;

    public SolidityFunction(AbiEntry abiEntry, List<SolidityType> parameters, List<List<SolidityTypeEncoder>> encoders, List<List<SolidityTypeDecoder>> decoders) {
        this.description = abiEntry;
        this.encoders = encoders;
        this.decoders = decoders;
        this.parameters = parameters;
    }

    public String getName() {
        return description.getName();
    }

    public AbiEntry getDescription() {
        return description;
    }

    public List<SolidityType> getParameters() {
        return parameters;
    }

    public boolean matchParams(Object[] args) {
        if (args.length != encoders.size()) {
            return false;
        }
        for (int i = 0; i < args.length; i++) {
            final Object arg = args[i];
            if (arg != null && encoders.get(i).stream().noneMatch(encoder -> encoder.canConvert(arg.getClass()))) {
                return false;
            }
        }
        return true;
    }

    public boolean matchParams(Class<?>... types) {
        if (types.length != encoders.size()) {
            return false;
        }
        for (int i = 0; i < types.length; i++) {
            final Class<?> type = types[i];
            if (encoders.get(i).stream().noneMatch(encoder -> encoder.canConvert(type))) {
                return false;
            }
        }
        return true;
    }

    public Optional<String> matchParam(Class<?> classType, int paramIndex) {
        if (encoders.get(paramIndex).stream().noneMatch(encoder -> encoder.canConvert(classType))) {
            return Optional.of("no encoder found for converting type " + classType.getSimpleName() + " to solidity type " + parameters.get(paramIndex).name());
        }
        return Optional.empty();
    }

    public HpbData encodeParam(Object arg, int index) {
        if (arg == null) {
            return HpbData.empty();
        }

        SolidityTypeEncoder encoder = getEncoder(index, arg);
        AbiParam param = description.getInputs().get(index);
        SolidityType solidityType = SolidityType.find(param.getType()).orElseThrow(() -> new ApiException("unknown solidity type " + description.getType()));

        return encoder.encode(arg, solidityType);

    }

    private SolidityTypeEncoder getEncoder(int index, Object arg) {
        return encoders.get(index).stream()
                .filter(enc -> enc.canConvert(arg.getClass()))
                .findFirst().orElseThrow(() -> new ApiException("encoder could not be found for \"" + arg.getClass().getSimpleName() + "\". Serious bug detected!!"));
    }

    public HpbData encode(Object... args) {
        HpbData result = description.signature();
        int dynamicIndex = args.length * WORD_SIZE;
        List<HpbData> dynamicData = new ArrayList<>();
        if (args.length != encoders.size()) {
            throw new ApiException("wrong number of arguments. you provided " + args.length + " arguments but should be " + encoders.size());
        }
        for (int i = 0; i < args.length; i++) {
            final Object arg = args[i];
            if (arg != null) {
                SolidityTypeEncoder encoder = getEncoder(i, arg);
                AbiParam param = description.getInputs().get(i);
                SolidityType solidityType = SolidityType.find(param.getType()).orElseThrow(() -> new ApiException("unknown solidity type " + description.getType()));
                if (solidityType.isDynamic || param.isDynamic()) {
                    result = result.merge(numberEncoder.encode(dynamicIndex, SolidityType.UINT));
                    HpbData dynamicEncode = encoder.encode(arg, solidityType);
                    dynamicIndex += dynamicEncode.length();
                    dynamicData.add(dynamicEncode);
                } else {
                    result = result.merge(encoder.encode(arg, solidityType));
                }
            }
        }

        return dynamicData.stream().reduce(result, HpbData::merge);
    }

    public boolean isConstant() {
        return description.isConstant();
    }

    public boolean isPayable() {
        return description.isPayable();
    }

    @Override
    public String toString() {
        return "(" + description.getOutputs().stream()
                .map(AbiParam::getType)
                .collect(Collectors.joining(", ")) + ") " + description.getName() + "(" + description.getInputs().stream()
                .map(AbiParam::getType).collect(Collectors.joining(", ")) + ")";
    }

    public Object decode(HpbData ethData, Type genericReturnType) {
        return description.decode(ethData, decoders, genericReturnType);
    }

    public List<List<SolidityTypeEncoder>> getEncoders() {
        return encoders;
    }

    public List<List<SolidityTypeDecoder>> getDecoders() {
        return decoders;
    }
}
