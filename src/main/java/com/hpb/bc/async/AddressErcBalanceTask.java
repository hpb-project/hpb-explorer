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

import com.hpb.bc.entity.ContractErcStandardInfo;
import com.hpb.bc.mapper.TxTransferRecordMapper;
import com.hpb.bc.model.Erc20TokenModel;
import com.hpb.bc.util.Erc20Helper;
import io.hpb.web3.protocol.admin.Admin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.concurrent.CountDownLatch;

@Component
public class AddressErcBalanceTask {

    public static final int TIME_OUT = 2;
    @Autowired
    private Erc20Helper erc20Helper;

    @Autowired
    Admin admin;

    @Autowired
    TxTransferRecordMapper txTransferRecordMapper;

    public Logger log = LoggerFactory.getLogger(AddressErcBalanceTask.class);

    @Async
    public void getERC20BalanceByAddress(CountDownLatch countDownLatch, ContractErcStandardInfo contractErcStandardInfo, List<Erc20TokenModel> resultErc20TokenModelList, String address) {
        try {
            BigInteger tokenBalance = erc20Helper.getTokenBalance(address, contractErcStandardInfo.getContractAddress());
            Double maxUnit = 0.00;
            if (contractErcStandardInfo == null) {
                maxUnit = Math.pow(10, 18);
            } else {
                maxUnit = Math.pow(10, contractErcStandardInfo.getDecimals());
            }
            BigDecimal balance = BigDecimal.valueOf(tokenBalance.divide(BigInteger.valueOf(maxUnit.longValue())).longValue());
            Erc20TokenModel model = new Erc20TokenModel();
            model.setContractAddress(contractErcStandardInfo.getContractAddress());
            model.setTokenName(contractErcStandardInfo.getTokenName());
            model.setTokenSymbolImageUrl(contractErcStandardInfo.getTokenSymbolImageUrl());
            model.setContractType(contractErcStandardInfo.getContractType());
            model.setBalance(balance);
            if (balance.compareTo(BigDecimal.ZERO) > 0) {
                resultErc20TokenModelList.add(model);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            countDownLatch.countDown();
        }
    }

}
