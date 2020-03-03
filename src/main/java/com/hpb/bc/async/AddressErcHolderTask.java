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

import com.hpb.bc.entity.AddressErcHolder;
import com.hpb.bc.mapper.TxTransferRecordMapper;
import com.hpb.bc.util.Erc20Helper;
import com.hpb.bc.util.MathHpbUtil;
import io.hpb.web3.protocol.admin.Admin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.util.List;
import java.util.concurrent.CountDownLatch;

@Component
public class AddressErcHolderTask {

    public static final int TIME_OUT = 2;
    public Logger log = LoggerFactory.getLogger(AddressErcHolderTask.class);

    @Autowired
    TxTransferRecordMapper txTransferRecordMapper;
    @Autowired
    private Erc20Helper erc20Helper;

    @Async
    public void getBalancePercentRateAndTransferNumberByAddress(CountDownLatch countDownLatch, AddressErcHolder addressErcHolder, List<AddressErcHolder> resultContractInfosList) {
        try {
            BigInteger totalSupply = erc20Helper.getTokenTotalSupply(addressErcHolder.getContractAddress());
            BigInteger tokenBalance = erc20Helper.getTokenBalance(addressErcHolder.getAddress(), addressErcHolder.getContractAddress());
            int tokenDecimals = erc20Helper.getTokenDecimals(addressErcHolder.getContractAddress());

            if (tokenBalance != null && totalSupply != null) {
                addressErcHolder.setBalancePercertRateOfCurrentErcToken(MathHpbUtil.ratePercent(tokenBalance, totalSupply));
            }
            Integer txTransferRecordNum = txTransferRecordMapper.selectTotalTransferNumByContractAddressHolderAddress(addressErcHolder.getContractAddress(), addressErcHolder.getAddress());
            if (txTransferRecordNum != null) {
                addressErcHolder.setTransferNumberOfCurrentErcToken(txTransferRecordNum);
            } else {
                addressErcHolder.setTransferNumberOfCurrentErcToken(0);
            }

            try {
                BigInteger bigIntegerBasicUnit = BigInteger.valueOf(10).pow(tokenDecimals);
                BigInteger bigIntegerBalanceByBasicUnit = tokenBalance.divide(bigIntegerBasicUnit);
                addressErcHolder.setBalanceAmount(String.valueOf(bigIntegerBalanceByBasicUnit));
            } catch (Exception e) {
                e.printStackTrace();
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            countDownLatch.countDown();
        }
    }

}
