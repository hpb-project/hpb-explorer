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
