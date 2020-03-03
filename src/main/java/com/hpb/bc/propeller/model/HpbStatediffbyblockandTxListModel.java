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

package com.hpb.bc.propeller.model;

import java.util.List;

public class HpbStatediffbyblockandTxListModel {


    private List<EvmdiffLog> evmdiffLogList;
    private List<StateLog> state_diff;

    private String txhash;

    public List<EvmdiffLog> getEvmdiffLogList() {
        return evmdiffLogList;
    }

    public void setEvmdiffLogList(List<EvmdiffLog> evmdiffLogList) {
        this.evmdiffLogList = evmdiffLogList;
    }

    public List<StateLog> getState_diff() {
        return state_diff;
    }

    public void setState_diff(List<StateLog> state_diff) {
        this.state_diff = state_diff;
    }

    public String getTxhash() {
        return txhash;
    }

    public void setTxhash(String txhash) {
        this.txhash = txhash;
    }
}
