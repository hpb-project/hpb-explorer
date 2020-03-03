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

package com.hpb.bc.rpc;

import com.hpb.bc.exception.ApiException;
import com.hpb.bc.model.EventData;
import com.hpb.bc.model.HpbData;
import com.hpb.bc.solidity.HpbSolidityCompiler;
import com.hpb.bc.solidity.SolidityEvent;
import com.hpb.bc.solidity.SolidityType;
import com.hpb.bc.solidity.abi.AbiParam;
import com.hpb.bc.solidity.converters.decoders.SolidityTypeDecoder;
import com.hpb.bc.solidity.converters.encoders.SolidityTypeEncoder;
import com.hpb.bc.solidity.values.HpbAccount;
import com.hpb.bc.solidity.values.HpbAddress;
import com.hpb.bc.solidity.values.HpbValue;
import com.hpb.bc.solidity.values.Nonce;
import io.hpb.web3.protocol.core.DefaultBlockParameter;
import io.hpb.web3.protocol.core.DefaultBlockParameterName;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;


public class HpbFacade {
    private final HpbContractInvocationHandler handler;
    private final HpbProxy hpbProxy;

    private final HpbSolidityCompiler solidityCompiler;

    HpbFacade(HpbProxy hpbProxy, HpbSolidityCompiler solidityCompiler) {

        this.solidityCompiler = solidityCompiler;
        this.handler = new HpbContractInvocationHandler(hpbProxy);
        this.hpbProxy = hpbProxy;
    }


    public HpbValue getBalance(HpbAddress addr) {
        return hpbProxy.getBalance(addr);
    }


    public HpbValue getBalance(HpbAccount account) {
        return hpbProxy.getBalance(account.getAddress());
    }


    public Nonce getNonce(HpbAddress address) {
        return hpbProxy.getNonce(address);
    }


    public List<EventData> getLogs(Optional<DefaultBlockParameter> fromBlock, Optional<DefaultBlockParameter> toBlock, SolidityEvent eventDefiniton, HpbAddress address, String... optionalTopics) {
        return hpbProxy.getLogs(fromBlock.orElse(DefaultBlockParameterName.EARLIEST), toBlock.orElse(DefaultBlockParameterName.LATEST), eventDefiniton, address, optionalTopics);
    }


    public HpbData encode(Object arg, SolidityType solidityType) {
        return Optional.of(arg).map(argument -> {
            SolidityTypeEncoder encoder = hpbProxy.getEncoders(new AbiParam(false, "", solidityType.name()))
                    .stream().filter(enc -> enc.canConvert(arg.getClass()))
                    .findFirst().orElseThrow(() -> new ApiException("cannot convert the type " + argument.getClass() + " to the solidty type " + solidityType));

            return encoder.encode(arg, solidityType);
        }).orElseGet(HpbData::empty);
    }

    public <T> T decode(HpbData data, SolidityType solidityType, Class<T> cls) {
        return decode(0, data, solidityType, cls);
    }


    public <T> T decode(Integer index, HpbData data, SolidityType solidityType, Class<T> cls) {
        if (hpbProxy.isVoidType(cls)) {
            return null;
        }
        SolidityTypeDecoder decoder = hpbProxy.getDecoders(new AbiParam(false, "", solidityType.name()))
                .stream()
                .filter(dec -> dec.canDecode(cls))
                .findFirst().orElseThrow(() -> new ApiException("cannot decode " + solidityType.name() + " to " + cls.getTypeName()));

        return (T) decoder.decode(index, data, cls);
    }
}
