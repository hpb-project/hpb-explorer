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

package com.hpb.bc.event;

import com.hpb.bc.solidity.values.HpbTransactionInfo;
import io.reactivex.Observable;

import java.util.concurrent.CompletableFuture;

/**
 * Created by davidroon on 27.04.16.
 * This code is released under Apache 2 license
 */
public class HpbEventHandler {
    private final CompletableFuture<Void> ready = new CompletableFuture<>();
    private final OnBlockHandler onBlockHandler;
    private final OnTransactionHandler onTransactionHandler;
    private long currentBlockNumber;

    public HpbEventHandler() {
        this.onBlockHandler = new OnBlockHandler();
        this.onTransactionHandler = new OnTransactionHandler();
    }

    public void onBlock(HpbBlockInfo block) {
        onBlockHandler.newElement(block);
        currentBlockNumber = block.blockNumber;
    }

    public void onTransactionExecuted(HpbTransactionInfo tx) {
        onTransactionHandler.on(tx);
    }

    public void onTransactionDropped(HpbTransactionInfo tx) {
        onTransactionHandler.on(tx);
    }

    public void onReady() {
        ready.complete(null);
    }

    public CompletableFuture<Void> ready() {
        return ready;
    }

    public long getCurrentBlockNumber() {
        return currentBlockNumber;
    }

    public Observable<HpbBlockInfo> observeBlocks() {
        return onBlockHandler.observable;
    }

    public Observable<HpbTransactionInfo> observeTransactions() {
        return onTransactionHandler.observable;
    }
}
