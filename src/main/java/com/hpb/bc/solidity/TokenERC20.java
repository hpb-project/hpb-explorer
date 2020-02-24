package com.hpb.bc.solidity;

import java.io.Serializable;

public class TokenERC20 implements Serializable {

    private String abi;
    private String bin;

    public String getAbi() {
        return abi;
    }

    public void setAbi(String abi) {
        this.abi = abi;
    }

    public String getBin() {
        return bin;
    }

    public void setBin(String bin) {
        this.bin = bin;
    }

    public TokenERC20() {
    }

    public TokenERC20(String abi, String bin) {
        this.abi = abi;
        this.bin = bin;
    }
}
