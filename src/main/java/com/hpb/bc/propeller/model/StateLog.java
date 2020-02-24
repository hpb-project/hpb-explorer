package com.hpb.bc.propeller.model;

import java.io.Serializable;

public class StateLog implements Serializable {

    private String addr;

    private String after;

    private String before;

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public String getAfter() {
        return after;
    }

    public void setAfter(String after) {
        this.after = after;
    }

    public String getBefore() {
        return before;
    }

    public void setBefore(String before) {
        this.before = before;
    }
}
