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
import com.hpb.bc.solidity.values.HpbTransactionInfo;
import com.hpb.bc.solidity.values.HpbTransactionStatus;
import io.hpb.web3.protocol.core.methods.response.HpbBlock;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by davidroon on 30.01.17.
 * This code is released under Apache 2 license
 */
public class HpbRpcEventGenerator {
    private final List<HpbEventHandler> ethereumEventHandlers = new ArrayList<>();
    private final HpbRpc hpbRpc;

    public HpbRpcEventGenerator(Web3JFacade web3JFacade, HpbRpcConfig config, HpbRpc hpbRpc) {
        this.hpbRpc = hpbRpc;
       /* if (config.isPollBlocks()) {
            web3JFacade.observeBlocksPolling(config.getPollingFrequence()).subscribe(this::observeBlocks);
        } else {
            web3JFacade.observeBlocks().subscribe(this::observeBlocks);
        }*/
        web3JFacade.observeBlocks().subscribe(this::observeBlocks);
    }

    private void observeBlocks(HpbBlock hpbBlock) {
        ethereumEventHandlers.forEach(HpbEventHandler::onReady);
        HpbBlockInfo param = hpbRpc.toBlockInfo(hpbBlock);
        ethereumEventHandlers.forEach(handler -> handler.onBlock(param));

        ethereumEventHandlers
                .forEach(handler -> param.receipts
                        .stream().map(tx -> new HpbTransactionInfo(tx.hash, tx, HpbTransactionStatus.Executed, tx.blockHash))
                        .forEach(handler::onTransactionExecuted));
    }

    public void addListener(HpbEventHandler ethereumEventHandler) {
        this.ethereumEventHandlers.add(ethereumEventHandler);
    }
}
