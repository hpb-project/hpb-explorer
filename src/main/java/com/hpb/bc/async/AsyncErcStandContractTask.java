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
import com.hpb.bc.entity.ContractInfo;
import com.hpb.bc.entity.TxTransferRecord;
import com.hpb.bc.mapper.AddressErcHolderMapper;
import com.hpb.bc.mapper.ContractErcStandardInfoMapper;
import com.hpb.bc.mapper.TxTransferRecordMapper;
import com.hpb.bc.util.Erc20Helper;
import com.hpb.bc.util.LeftJoinUtil;
import io.hpb.web3.protocol.admin.Admin;
import io.hpb.web3.protocol.core.DefaultBlockParameter;
import io.hpb.web3.protocol.core.DefaultBlockParameterName;
import io.hpb.web3.protocol.core.methods.response.HpbGetBalance;
import org.apache.commons.lang3.time.DurationFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@Component
public class AsyncErcStandContractTask {

    public static final int TIME_OUT = 2;

    @Autowired
    Admin admin;
    public Logger log = LoggerFactory.getLogger(AsyncErcStandContractTask.class);

    @Autowired
    AddressErcHolderMapper addressErcHolderMapper;

    @Autowired
    TxTransferRecordMapper txTransferRecordMapper;

    @Async
    public void setHolderNum(List<ContractErcStandardInfo> list, List<String> contractAddressList, CountDownLatch latch) {
        try {
            List<ContractErcStandardInfo> holderNumList = addressErcHolderMapper.selectHoldersCountByContractAddressList(contractAddressList);
            LeftJoinUtil.lj(list, holderNumList, ContractErcStandardInfo::getContractAddress, ContractErcStandardInfo::getContractAddress, ContractErcStandardInfo::getHolders, ContractErcStandardInfo::setHolders);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            latch.countDown();
        }
    }

    @Async
    public void setTransferNum(List<ContractErcStandardInfo> list, String contractAddress, CountDownLatch latch) {
        try {
            ContractErcStandardInfo standardInfo = txTransferRecordMapper.selectTotalTransferNumByContractAddressList(contractAddress);
            if (standardInfo != null) {
                LeftJoinUtil.lj(list, Arrays.asList(standardInfo), ContractErcStandardInfo::getContractAddress, ContractErcStandardInfo::getContractAddress, ContractErcStandardInfo::getTransfersNum, ContractErcStandardInfo::setTransfersNum);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            latch.countDown();
        }
    }
}
