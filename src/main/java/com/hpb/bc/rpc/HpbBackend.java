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

import com.hpb.bc.event.HpbBlockInfo;
import com.hpb.bc.event.HpbEventHandler;
import com.hpb.bc.model.EventData;
import com.hpb.bc.model.HpbData;
import com.hpb.bc.model.HpbHash;
import com.hpb.bc.solidity.SmartContractByteCode;
import com.hpb.bc.solidity.SolidityEvent;
import com.hpb.bc.solidity.values.*;
import com.hpb.bc.solidity.values.ChainId;
import io.hpb.web3.protocol.core.DefaultBlockParameter;
import com.hpb.bc.solidity.values.Nonce;

import java.util.List;
import java.util.Optional;

/**
 * Created by davidroon on 20.01.17.
 * This code is released under Apache 2 license
 */
public interface HpbBackend {

    /*    GasPrice getGasPrice();*/

    HpbValue getBalance(HpbAddress address);

    boolean addressExists(HpbAddress address);

    HpbHash submit(HpbTransactionRequest request, Nonce nonce);

    GasUsage estimateGas(HpbAccount account, HpbAddress address, HpbValue value, HpbData data);

    Nonce getNonce(HpbAddress currentAddress);

    long getCurrentBlockNumber();

    Optional<HpbBlockInfo> getBlock(long blockNumber);

    Optional<HpbBlockInfo> getBlock(HpbHash blockNumber);

    SmartContractByteCode getCode(HpbAddress address);

    HpbData constantCall(HpbAccount account, HpbAddress address, HpbValue value, HpbData data);

    List<EventData> logCall(final DefaultBlockParameter fromBlock, final DefaultBlockParameter toBlock, final SolidityEvent eventDefinition, HpbAddress address, final String... optionalTopics);

    void register(HpbEventHandler eventHandler);

    Optional<HpbTransactionInfo> getTransactionInfo(HpbHash hash);

    ChainId getChainId();
}

