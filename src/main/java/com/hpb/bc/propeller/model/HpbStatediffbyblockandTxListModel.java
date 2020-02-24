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
