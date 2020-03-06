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
