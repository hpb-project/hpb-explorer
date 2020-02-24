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
