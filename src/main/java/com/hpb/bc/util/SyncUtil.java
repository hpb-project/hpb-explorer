package com.hpb.bc.util;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.hpb.web3.protocol.admin.Admin;
import io.hpb.web3.protocol.core.DefaultBlockParameterName;
import io.hpb.web3.protocol.core.Response.Error;
import io.hpb.web3.protocol.core.methods.response.HpbGetBalance;
import io.hpb.web3.protocol.core.methods.response.HpbGetTransactionCount;
import io.hpb.web3.protocol.core.methods.response.HpbSendTransaction;
import io.hpb.web3.protocol.core.methods.response.HpbTransaction;

@Component
public class SyncUtil {
    private static final String ERROR_CODE = "999999";
    private static final String SUCCESS_CODE = "000000";
    public static final String status_send = "0";
    public static final String status_init = "1";
    public static final String status_process = "2";
    public static final String status_complete = "3";
    public static final String status_fail = "4";
    public static final String status_check = "5";
    public static final int DEFAULT_POLLING_ATTEMPTS_PER_TX_HASH = 40;
    public static final long DEFAULT_POLLING_FREQUENCY = 15 * 1000;
    private static final Logger log = LoggerFactory.getLogger(SyncUtil.class);
    private static final long WEB3_TIMEOUT = 10;
    @Autowired
    private Admin admin;


    public List<Object> hpbSendRawTransaction(String signedTransactionData) {
        List<Object> result = new ArrayList<Object>();
        try {
            HpbSendTransaction hpbSendTransaction = admin.hpbSendRawTransaction(signedTransactionData).
                    sendAsync().get(WEB3_TIMEOUT, TimeUnit.MINUTES);
            Error error = hpbSendTransaction.getError();
            if (error != null) {
                String message = error.getMessage();
                result.add(ERROR_CODE);
                result.add(error.getCode());
                result.add(message);
            } else {
                result.add(SUCCESS_CODE);
                result.add(hpbSendTransaction.getTransactionHash());
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            e.printStackTrace();
        }
        return result;
    }

    public List<Object> queryTradeByHash(String hash) {
        List<Object> result = new ArrayList<Object>();
        try {

            HpbTransaction hpbTransaction = admin.hpbGetTransactionByHash(hash).sendAsync().get(WEB3_TIMEOUT, TimeUnit.MINUTES);
            Error error = hpbTransaction.getError();
            if (error != null) {
                String message = error.getMessage();
                result.add(ERROR_CODE);
                result.add(error.getCode());
                result.add(message);
            } else {
                result.add(SUCCESS_CODE);
                result.add(hpbTransaction.getResult());
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            e.printStackTrace();
        }
        return result;
    }

    public List<Object> getNonce(String accountId) {
        List<Object> result = new ArrayList<Object>();
        try {

            HpbGetTransactionCount count = admin.hpbGetTransactionCount(accountId, DefaultBlockParameterName.PENDING)
                    .sendAsync().get(WEB3_TIMEOUT, TimeUnit.MINUTES);
            Error error = count.getError();
            if (error != null) {
                String message = error.getMessage();
                result.add(ERROR_CODE);
                result.add(error.getCode());
                result.add(message);
            } else {
                result.add(SUCCESS_CODE);
                result.add(count.getTransactionCount());
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            e.printStackTrace();
        }
        return result;
    }

    public List<Object> queryBalance(String accountId) {
        List<Object> result = new ArrayList<Object>();
        try {
            HpbGetBalance balance = admin.hpbGetBalance(accountId, DefaultBlockParameterName.LATEST).sendAsync().get(WEB3_TIMEOUT, TimeUnit.MINUTES);
            Error error = balance.getError();
            if (error != null) {
                String message = error.getMessage();
                result.add(ERROR_CODE);
                result.add(error.getCode());
                result.add(message);
            } else {
                result.add(SUCCESS_CODE);
                result.add(balance.getBalance());
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            e.printStackTrace();
        }
        return result;
    }

}