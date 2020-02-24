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
