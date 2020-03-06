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

package com.hpb.bc.service.impl;

import com.hpb.bc.entity.result.Result;
import com.hpb.bc.entity.result.ResultCode;
import com.hpb.bc.service.ProxyService;
import io.hpb.web3.protocol.admin.Admin;
import io.hpb.web3.protocol.core.DefaultBlockParameter;
import io.hpb.web3.protocol.core.methods.response.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigInteger;

@Service
public class ProxyServiceImpl implements ProxyService {
    @Autowired
    private Admin admin;

    @Value("${web3.account.globalAccount}")
    private String from;

    @Override
    public BigInteger hpbBlockNumber() {
        try {
            HpbBlockNumber hpbBlockNumber = admin.hpbBlockNumber().send();
            return hpbBlockNumber.getBlockNumber();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Result<HpbBlock.Block> hpbGetBlockByNumber(Object blockNumber, boolean full) {
        try {
            if (blockNumber instanceof String) {
                HpbBlock hpbBlock = admin
                        .hpbGetBlockByNumber(DefaultBlockParameter.valueOf(blockNumber.toString()), full)
                        .send();
                return new Result<HpbBlock.Block>(ResultCode.SUCCESS, hpbBlock.getBlock());
            } else {
                HpbBlock hpbBlock = admin
                        .hpbGetBlockByNumber(DefaultBlockParameter.valueOf((BigInteger) blockNumber), full)
                        .send();
                return new Result<HpbBlock.Block>(ResultCode.SUCCESS, hpbBlock.getBlock());
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Result<TransactionReceipt> hpbGetTransactionReceipt(String txHash) {
        try {
            HpbGetTransactionReceipt receipt = admin.hpbGetTransactionReceipt(txHash).send();
            return new Result<>(ResultCode.SUCCESS, receipt.getTransactionReceipt().get());
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public String hpbCall(String to, String data, Object blockNumber) {
        try {
            if (blockNumber instanceof String) {
                HpbCall hpbCall = admin
                        .hpbCall(io.hpb.web3.protocol.core.methods.request.Transaction.createHpbCallTransaction(from, to, data), DefaultBlockParameter.valueOf(blockNumber.toString()))
                        .send();
                return hpbCall.getValue();
            } else {
                HpbCall hpbCall = admin
                        .hpbCall(io.hpb.web3.protocol.core.methods.request.Transaction.createHpbCallTransaction(from, to, data), DefaultBlockParameter.valueOf((BigInteger) blockNumber))
                        .send();
                return hpbCall.getValue();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
