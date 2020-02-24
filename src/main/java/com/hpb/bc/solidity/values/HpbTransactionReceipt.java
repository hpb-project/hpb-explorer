package com.hpb.bc.solidity.values;

import com.hpb.bc.model.EventData;
import com.hpb.bc.model.HpbData;
import com.hpb.bc.model.HpbHash;

import java.util.List;

/**
 * Created by davidroon on 03.02.17.
 * This code is released under Apache 2 license
 */
public class HpbTransactionReceipt {
    public final HpbHash hash;
    public final HpbHash blockHash;
    public final HpbAddress sender;
    public final HpbAddress receiveAddress;
    public final HpbAddress contractAddress;
    public final HpbData callData;
    public final String error;
    public final HpbData executionResult;
    public final boolean isSuccessful;
    public final List<EventData> events;
    public final HpbValue ethValue;

    public HpbTransactionReceipt(HpbHash hash, HpbHash blockHash, HpbAddress sender, HpbAddress receiveAddress, HpbAddress contractAddress, HpbData callData, String error, HpbData executionResult, boolean isSuccessful, List<EventData> events, HpbValue ethValue) {
        this.hash = hash;
        this.blockHash = blockHash;
        this.sender = sender;
        this.receiveAddress = receiveAddress;
        this.contractAddress = contractAddress;
        this.callData = callData;
        this.error = error;
        this.executionResult = executionResult;
        this.isSuccessful = isSuccessful;
        this.events = events;
        this.ethValue = ethValue;
    }
}
