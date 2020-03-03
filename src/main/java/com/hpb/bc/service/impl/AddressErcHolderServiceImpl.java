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

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hpb.bc.async.AddressErcHolderTask;
import com.hpb.bc.entity.AddressErcHolder;
import com.hpb.bc.example.AddressErcHolderExample;
import com.hpb.bc.mapper.AddressErcHolderMapper;
import com.hpb.bc.service.AddressErcHolderService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@Service
public class AddressErcHolderServiceImpl implements AddressErcHolderService {
    @Autowired
    AddressErcHolderMapper addressErcHolderMapper;
    @Autowired
    AddressErcHolderTask addressErcHolderTask;

    @Override
    public PageInfo<AddressErcHolder> queryPageAddressErcHolder(AddressErcHolder addressErcHolder, int pageNum, int pageSize) {
        AddressErcHolderExample example = new AddressErcHolderExample();
        AddressErcHolderExample.Criteria criteria = example.createCriteria();
        if (StringUtils.isNotEmpty(addressErcHolder.getContractAddress())) {
            criteria.andContractAddressEqualTo(addressErcHolder.getContractAddress());
        }
        criteria.andBalanceAmountGreaterThan(BigDecimal.ZERO);
        example.setDistinct(true);
        example.setOrderByClause(" balance_amount desc  ");
        PageHelper.startPage(pageNum, pageSize);
        List<AddressErcHolder> list = addressErcHolderMapper.selectByExample(example);
        PageInfo<AddressErcHolder> pageInfo = new PageInfo<AddressErcHolder>(list);
        List<AddressErcHolder> contractErcStandardInfoList = pageInfo.getList();
        List<AddressErcHolder> resultAddressErcHolderList = Collections.synchronizedList(new ArrayList<AddressErcHolder>());
        resultAddressErcHolderList.addAll(contractErcStandardInfoList);
        try {
            CountDownLatch countDownLatch = new CountDownLatch(resultAddressErcHolderList.size());
            for (AddressErcHolder addressErcHolderTemp : resultAddressErcHolderList) {
                addressErcHolderTask.getBalancePercentRateAndTransferNumberByAddress(countDownLatch, addressErcHolderTemp, resultAddressErcHolderList);
            }
            countDownLatch.await(2, TimeUnit.MINUTES);
            pageInfo.setList(resultAddressErcHolderList);
            return pageInfo;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }
}
