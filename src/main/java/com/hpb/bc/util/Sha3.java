package com.hpb.bc.util;


import com.hpb.bc.exception.HpbApiException;
import com.hpb.bc.model.HpbData;

public class Sha3 {
    public final byte[] hash;

    public HpbData toData() {
        return HpbData.of(hash);
    }

    public Sha3(byte[] hash) {
        if (hash.length != 32) {
            throw new HpbApiException("SHA3 hash length should be 32");
        }
        this.hash = hash;
    }
}
