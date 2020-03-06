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
import com.hpb.bc.async.AsyncContractEventTask;
import com.hpb.bc.contracts.abi.EventValuesWithLog;
import com.hpb.bc.entity.TxTransferRecord;
import com.hpb.bc.model.*;
import com.hpb.bc.propeller.model.EvmdiffLog;
import com.hpb.bc.propeller.model.StateLog;
import com.hpb.bc.propeller.solidity.HpbPropellerSolidityCompiler;
import com.hpb.bc.service.ContractEventService;
import com.hpb.bc.service.TxTransferRecordService;
import com.hpb.bc.solidity.CompilationResult;
import com.hpb.bc.solidity.RawSolidityEvent;
import com.hpb.bc.solidity.values.SoliditySource;
import io.hpb.web3.protocol.admin.Admin;
import io.hpb.web3.protocol.core.methods.response.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@Service
public class ContractEventServiceImpl implements ContractEventService {

    @Autowired
    TxTransferRecordService txTransferRecordService;

    @Autowired
    AsyncContractEventTask asyncContractEventTask;

    @Autowired
    Admin admin;
    public Logger log = LoggerFactory.getLogger(this.getClass());



    private final static long COUNTDOWNLATCH_AWAIT_MINUTES = 2;

    @Override
    public PageInfo<EventData> queryPageEventDataByContractAddress(String contractAddress, int pageNum, int pageSize) {

        PageHelper.startPage(pageNum, pageSize);
        PageInfo<TxTransferRecord> txTransferRecordPageInfo = txTransferRecordService.queryPageTxTransferRecordListByContractAddress(contractAddress, pageNum, pageSize);
        List<EventData> eventDataList = Collections.synchronizedList(new ArrayList<EventData>());
        List<TxTransferRecord> currentPageTx = txTransferRecordPageInfo.getList();

        PageInfo<EventData> pageInfo = null;
        try {
            CountDownLatch countDownLatch = new CountDownLatch(currentPageTx.size());
            for (int i = 0; i < currentPageTx.size(); i++) {
                TxTransferRecord record = currentPageTx.get(i);
                asyncContractEventTask.getEventDataLogByAddress(countDownLatch, eventDataList, record);
            }
            countDownLatch.await(COUNTDOWNLATCH_AWAIT_MINUTES, TimeUnit.MINUTES);

            pageInfo = new PageInfo<EventData>(eventDataList);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return pageInfo;
    }


    @Override
    public PageInfo<EventData> queryPageEventDataByTxHash(String txHash, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        TxTransferRecordModel model = new TxTransferRecordModel();
        model.setTxHash(txHash);
        List<EventData> eventDataList = Collections.synchronizedList(new ArrayList<EventData>());
        eventDataList = asyncContractEventTask.getEventDataLogByTxHash(txHash);
        PageInfo<EventData> pageInfo = null;
        try {
            pageInfo = new PageInfo<EventData>(eventDataList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return pageInfo;
    }

    @Override
    public PageInfo<EventInfo> queryPageEventInfoByTxHash(String txHash, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        TxTransferRecordModel model = new TxTransferRecordModel();
        model.setTxHash(txHash);
        List<EventData> eventDataList = Collections.synchronizedList(new ArrayList<EventData>());
        eventDataList = asyncContractEventTask.getEventDataLogByTxHash(txHash);
        List<EventInfo>  eventInfoList = new ArrayList<>();
        for(int i = 0; i< eventDataList.size();i++ ){
            EventData eventData = eventDataList.get(i);
        }
        PageInfo<EventInfo> pageInfo = null;
        try {
            pageInfo = new PageInfo<EventInfo>(eventInfoList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return pageInfo;
    }


    @Override
    public PageInfo<StateLog> queryPageStateLogByTxHash(TxTransferHashModel model) {
        PageHelper.startPage(model.getCurrentPage(), model.getPageSize());
        model.setTxHash(model.getTxHash());
        List<StateLog> stateLogList = Collections.synchronizedList(new ArrayList<StateLog>());
        stateLogList = asyncContractEventTask.getStateDiffByTxHash(model.getTxHash());
        PageInfo<StateLog> pageInfo = null;
        try {
            pageInfo = new PageInfo<StateLog>(stateLogList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return pageInfo;
    }

    @Override
    public PageInfo<EvmdiffLog> queryPageEvmdiffLogByTxHash(TxTransferHashModel model) {
        PageHelper.startPage(model.getCurrentPage(), model.getPageSize());
        model.setTxHash(model.getTxHash());
        List<EvmdiffLog> evmdiffLogList = Collections.synchronizedList(new ArrayList<EvmdiffLog>());
        evmdiffLogList = asyncContractEventTask.getEvmdiffLogByTxHash(model.getTxHash());
        PageInfo<EvmdiffLog> pageInfo = null;
        try {
            pageInfo = new PageInfo<EvmdiffLog>(evmdiffLogList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return pageInfo;
    }


    @Override
    public PageInfo<HpbEventModel> queryPageHpbEventModelByTxHash(String txHash, int pageNum, int pageSize) {
        List<HpbEventModel> eventValuesWithLogList = Collections.synchronizedList(new ArrayList<HpbEventModel>());
        PageHelper.startPage(pageNum, pageSize);
        PageInfo<HpbEventModel> pageInfo = null;
        try {
            List<HpbEventModel> logList =   asyncContractEventTask.getPageHpbEventModelByTxHash(txHash);
            eventValuesWithLogList.addAll(logList);
            pageInfo = new PageInfo<HpbEventModel>(eventValuesWithLogList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return pageInfo;
    }


    @Override
    public PageInfo<HpbEventModel> queryPageHpbEventModelByAddress(String address, int pageNum, int pageSize) {
        List<HpbEventModel> eventValuesWithLogList = Collections.synchronizedList(new ArrayList<HpbEventModel>());
        PageHelper.startPage(pageNum, pageSize);
        PageInfo<HpbEventModel> pageInfo = null;
        try {
            List<HpbEventModel> logList =   asyncContractEventTask.getPageHpbEventModelByAddress(address,pageNum,pageSize);
            eventValuesWithLogList.addAll(logList);
            pageInfo = new PageInfo<HpbEventModel>(eventValuesWithLogList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return pageInfo;
    }
}
