package com.hpb.bc.contracts.abi;

import io.hpb.web3.abi.EventValues;
import io.hpb.web3.abi.datatypes.Type;
import io.hpb.web3.protocol.core.methods.response.Log;

/**
 * 获取EVENT
 */
import java.util.List;

public class EventValuesWithLog {
    public  EventValues eventValues;
    public  Log log;
    public EventValuesWithLog(EventValues eventValues, Log log) {
        this.eventValues = eventValues;
        this.log = log;
    }



    public List<Type> getIndexedValues() {
        return this.eventValues.getIndexedValues();
    }

    public List<Type> getNonIndexedValues() {
        return this.eventValues.getNonIndexedValues();
    }

    public Log getLog() {
        return this.log;
    }

}
