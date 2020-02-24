package com.hpb.bc.exception;

public class HpbApiException extends RuntimeException {

    public HpbApiException(String s) {
        super(s);
    }

    public HpbApiException(String s, Throwable t) {
        super(s, t);
    }
}
