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


import com.hpb.bc.event.HpbEventHandler;
import com.hpb.bc.solidity.HpbSolidityCompiler;
import com.hpb.bc.solidity.converters.SolidityTypeGroup;
import com.hpb.bc.solidity.converters.decoders.*;
import com.hpb.bc.solidity.converters.decoders.list.*;
import com.hpb.bc.solidity.converters.encoders.*;
import com.hpb.bc.solidity.converters.encoders.list.ArrayEncoder;
import com.hpb.bc.solidity.converters.encoders.list.ListEncoder;
import com.hpb.bc.solidity.converters.encoders.list.SetEncoder;

/**
 * Created by davidroon on 27.04.16.
 * This code is released under Apache 2 license
 */
public final class CoreHpbFacadeProvider {

    private CoreHpbFacadeProvider() {
    }

    public static HpbFacade create(HpbBackend backend, HpbConfig config) {
        return create(backend, new HpbEventHandler(), config);
    }

    public static HpbFacade create(HpbBackend backend, HpbEventHandler eventHandler, HpbConfig config) {
        HpbProxy proxy = new HpbProxy(backend, eventHandler, config);
        proxy.addVoidClass(Void.class);
        registerDefaultEncoders(proxy);
        registerDefaultDecoders(proxy);
        registerDefaultListDecoder(proxy);
        registerDefaultListEncoder(proxy);
        return new HpbFacade(proxy, HpbSolidityCompiler.getInstance());
    }

    private static void registerDefaultDecoders(HpbProxy proxy) {

        proxy.addDecoder(SolidityTypeGroup.Number, new NumberDecoder())
                .addDecoder(SolidityTypeGroup.Bool, new BooleanDecoder())
                .addDecoder(SolidityTypeGroup.String, new StringDecoder())
                .addDecoder(SolidityTypeGroup.Address, new AddressDecoder())
                .addDecoder(SolidityTypeGroup.Number, new DateDecoder())
                .addDecoder(SolidityTypeGroup.Number, new EnumDecoder())
                .addDecoder(SolidityTypeGroup.Number, new HpbValueDecoder())
                .addDecoder(SolidityTypeGroup.Raw, new HpbDataDecoder());
    }

    private static void registerDefaultEncoders(HpbProxy proxy) {
        proxy.addEncoder(SolidityTypeGroup.Number, new NumberEncoder())
                .addEncoder(SolidityTypeGroup.Number, new EnumEncoder())
                .addEncoder(SolidityTypeGroup.Bool, new BooleanEncoder())
                .addEncoder(SolidityTypeGroup.String, new StringEncoder())
                .addEncoder(SolidityTypeGroup.Address, new AddressEncoder())
                .addEncoder(SolidityTypeGroup.Address, new AccountEncoder())
                .addEncoder(SolidityTypeGroup.Number, new DateEncoder())
                .addEncoder(SolidityTypeGroup.Number, new HpbValueEncoder())
                .addEncoder(SolidityTypeGroup.Raw, new HpbDataEncoder())
                .addEncoder(SolidityTypeGroup.Raw, new SignatureEncoder());
    }


    private static void registerDefaultListDecoder(HpbProxy proxy) {
        proxy
                .addListDecoder(ListDecoder.class)
                .addListDecoder(SetDecoder.class)
                .addListDecoder(ArrayDecoder.class)
                .addListDecoder(HpbDataListDecoder.class)
                .addListDecoder(SignatureDecoder.class);
    }

    private static void registerDefaultListEncoder(HpbProxy proxy) {
        proxy
                .addListEncoder(ListEncoder.class)
                .addListEncoder(SetEncoder.class)
                .addListEncoder(ArrayEncoder.class);
    }
}
