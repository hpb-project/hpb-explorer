package com.hpb.bc.async;

import com.hpb.bc.mapper.AddrsMapper;
import io.hpb.web3.protocol.admin.Admin;
import io.hpb.web3.protocol.core.DefaultBlockParameterName;
import io.hpb.web3.protocol.core.methods.response.HpbGetBalance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.math.BigInteger;
import java.util.List;
import java.util.concurrent.CountDownLatch;

@Component
public class AsyncGetBalanceOrderTask {


    @Autowired
    AddrsMapper addrsMapper;

    @Autowired
    Admin admin;

    @Async
    public void getBalanceOfAddress(CountDownLatch countDownLatch, List<BigInteger> balanceList, String address) {
        try {
            BigInteger balance = admin.hpbGetBalance(address, DefaultBlockParameterName.LATEST).send().getBalance();
            balanceList.add(balance);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            countDownLatch.countDown();
        }

    }


    @Async
    public void getBalanceByAddress(String address, CountDownLatch countDownLatch, List<BigInteger> bigIntegerList) {
        try {
            HpbGetBalance send = admin.hpbGetBalance(address, DefaultBlockParameterName.LATEST).send();
            BigInteger b = send.getBalance();
            bigIntegerList.add(b);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            countDownLatch.countDown();
        }
    }
}
