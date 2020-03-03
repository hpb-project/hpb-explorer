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

import com.hpb.bc.model.HpbData;
import org.spongycastle.util.encoders.Hex;

import java.nio.charset.StandardCharsets;
import java.util.Optional;

/**
 * Created by davidroon on 17.12.16.
 * This code is released under Apache 2 license
 */
public final class SmartContractByteCode {
    private static final int DATA_SIZE = 256;
    private static final int HASH_SIZE = 32;
    private static final int START_OF_LINK_INDEX = 7;
    private final byte[] code;

    private SmartContractByteCode(byte[] code) {
        this.code = code;
    }

    public static SmartContractByteCode of(HpbData code) {
        return new SmartContractByteCode(code.data);
    }

    public static SmartContractByteCode of(byte[] code) {
        return new SmartContractByteCode(code);
    }

    public static SmartContractByteCode of(String code) {
        return new SmartContractByteCode(Hex.decode(code));
    }

/*    public Optional<SwarmMetadaLink> getMetadaLink() {
        if (code.length == 0) {
            return Optional.empty();
        }

        byte length1 = code[code.length - 1];
        byte length2 = code[code.length - 2];
        int length = length1 + length2 * DATA_SIZE;
        if (length < code.length) {
            byte[] link = new byte[length];
            System.arraycopy(code, code.length - length, link, 0, length);
            return Optional.of(toMetaDataLink(link));
        }
        return Optional.empty();
    }*/

/*    private SwarmMetadaLink toMetaDataLink(byte[] link) {
        String strLink = new String(link, StandardCharsets.UTF_8);
        if (strLink.startsWith("bzzr0")) {
            return toSwarmMetadataLink(link);
        }
        throw new ApiException("unknown protocol forNetwork " + strLink);
    }

    private SwarmMetadaLink toSwarmMetadataLink(byte[] link) {
        byte[] hash = new byte[HASH_SIZE];
        System.arraycopy(link, START_OF_LINK_INDEX, hash, 0, HASH_SIZE);
        return new SwarmMetadaLink(SwarmHash.of(hash));
    }*/

    @Override
    public String toString() {
        return Hex.toHexString(code);
    }

    public boolean isEmpty() {
        return code.length == 0;
    }
}
