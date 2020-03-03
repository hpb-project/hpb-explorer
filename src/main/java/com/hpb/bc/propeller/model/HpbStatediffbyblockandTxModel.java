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

import com.hpb.bc.propeller.model.StateLog;

import java.util.List;

public class HpbStatediffbyblockandTxModel {

    private String evmdiff;

    private List<StateLog> state_diff;

    private String txhash;

    public HpbStatediffbyblockandTxModel() {
    }

    public HpbStatediffbyblockandTxModel(String evmdiff, List<StateLog> state_diff, String txhash) {
        this.evmdiff = evmdiff;
        this.state_diff = state_diff;
        this.txhash = txhash;
    }

    public String getEvmdiff() {
        return evmdiff;
    }

    public void setEvmdiff(String evmdiff) {
        this.evmdiff = evmdiff;
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
