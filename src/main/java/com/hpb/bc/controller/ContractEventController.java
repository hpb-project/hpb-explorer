package com.hpb.bc.controller;

import com.github.pagehelper.PageInfo;
import com.hpb.bc.entity.TxTransferRecord;
import com.hpb.bc.entity.result.Result;
import com.hpb.bc.entity.result.ResultCode;
import com.hpb.bc.model.*;
import com.hpb.bc.propeller.HpbFacadeHelper;
import com.hpb.bc.propeller.model.EvmdiffLog;
import com.hpb.bc.propeller.model.StateLog;
import com.hpb.bc.service.ContractEventService;
import com.hpb.bc.service.TxTransferRecordService;
import com.hpb.bc.solidity.values.SoliditySource;
import com.hpb.bc.util.GsonUtil;
import io.hpb.web3.abi.datatypes.Uint;
import io.hpb.web3.utils.Convert;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

@RestController
@EnableAutoConfiguration
@RequestMapping("/contract-event")
public class ContractEventController extends BaseController {

    @Autowired
    ContractEventService contractEventService;

    @Autowired
    HpbFacadeHelper hpbFacadeHelper;

    @ApiOperation(value = "查询根据智能合约地址查询eventLogs", notes = "查询根据智能合约地址查询eventLogs")
    @PostMapping("/eventLogs")
    public List<Object> getEventLog(@RequestBody TxTransferRecordModel model) {
        PageInfo<EventData> pageInfo = contractEventService.queryPageEventDataByContractAddress(model.getContractAddress(), model.getCurrentPage(), model.getPageSize());
        Result<PageInfo<EventData>> result = new Result<>(ResultCode.SUCCESS, pageInfo);
        List<Object> objectList = result.getValue();
        System.out.println("eventLogsresult ====" + result.getValue());
//		GsonUtil.gsonToListMaps()
        return objectList;
    }

    @ApiOperation(value = "根据交易HASH从数据库中获取事件日志", notes = "根据交易HASH从数据库中获取事件日志")
    @PostMapping("/txHash/db/eventLogs")
    public List<Object> getEventLogByHash(@RequestBody TxTransferHashModel model) {
        TxTransferRecordModel tempModel = new TxTransferRecordModel();
        tempModel.setTxHash(model.getTxHash());
        PageInfo<EventData> pageInfo = contractEventService.queryPageEventDataByTxHash(model.getTxHash(), model.getCurrentPage(), model.getPageSize());
        Result<PageInfo<EventData>> result = new Result<>(ResultCode.SUCCESS, pageInfo);

        List<Object> objectList = result.getValue();
        System.out.println("eventLogsresult ====" + result.getValue());
        return objectList;
        //return result.getValue();
    }


    @ApiOperation(value = "根据交易HASH从chain中获取事件日志", notes = "根据交易HASH从数据库中获取事件日志")
    @PostMapping("/txHash/chain/eventData")
    public List<Object> getEventDataByHashFromChain(@RequestBody TxTransferHashModel model) {
        TxTransferRecordModel tempModel = new TxTransferRecordModel();
        tempModel.setTxHash(model.getTxHash());
        PageInfo<EventData> pageInfo = contractEventService.queryPageEventDataByTxHash(model.getTxHash(), model.getCurrentPage(), model.getPageSize());
        Result<PageInfo<EventData>> result = new Result<>(ResultCode.SUCCESS, pageInfo);
        List<Object> objectList = result.getValue();
        System.out.println("eventLogsresult ====" + result.getValue());
        System.out.println("eventLogsresult ====" + GsonUtil.gsonString(result.getValue()));
        Result<String> resultString = new Result<>(ResultCode.SUCCESS, GsonUtil.gsonString(result.getValue()));

        PageInfo<EventInfo> pageEventInfo = contractEventService.queryPageEventInfoByTxHash(model.getTxHash(), model.getCurrentPage(), model.getPageSize());
        return objectList;
    }

    //

    @ApiOperation(value = "根据交易HASH从chain中获取事件日志hpbEventModel", notes = "根据交易HASH从数据库中获取事件日志hpbEventModel")
    @PostMapping("/txHash/chain/hpbEventModel")
    public List<Object> getHpbEventModelByHashFromChain(@RequestBody TxTransferHashModel model) {
        TxTransferRecordModel tempModel = new TxTransferRecordModel();
        tempModel.setTxHash(model.getTxHash());
        PageInfo<HpbEventModel> pageInfo = contractEventService.queryPageHpbEventModelByTxHash(model.getTxHash(), model.getCurrentPage(), model.getPageSize());
        Result<PageInfo<HpbEventModel>> result = new Result<>(ResultCode.SUCCESS, pageInfo);
        List<Object> objectList = result.getValue();
        System.out.println("eventLogsresult ====" + result.getValue());
        System.out.println("eventLogsresult ====" + GsonUtil.gsonString(result.getValue()));
        return objectList;
    }


    @ApiOperation(value = "根据交易HASH从chain中获取事件日志hpbEventModel", notes = "根据交易HASH从数据库中获取事件日志hpbEventModel")
    @PostMapping("/address/chain/hpbEventModel")
    public List<Object> getHpbEventModelByAddresssFromChain(@RequestBody TxTransferHashModel model) {
        TxTransferRecordModel tempModel = new TxTransferRecordModel();
        tempModel.setTxHash(model.getTxHash());
        PageInfo<HpbEventModel> pageInfo = contractEventService.queryPageHpbEventModelByAddress(model.getAddress(), model.getCurrentPage(), model.getPageSize());
        Result<PageInfo<HpbEventModel>> result = new Result<>(ResultCode.SUCCESS, pageInfo);
        List<Object> objectList = result.getValue();
        System.out.println("eventLogsresult ====" + result.getValue());
        System.out.println("eventLogsresult ====" + GsonUtil.gsonString(result.getValue()));
        return objectList;
    }


    @ApiOperation(value = "根据交易HASH从chain中获evmdiffLog", notes = "根据交易HASH从chain中获evmdiffLog")
    @PostMapping("/txHash/chain/evmdiffLog")
    public List<Object> getEventLogByHashFromChain(@RequestBody TxTransferHashModel model) {
        PageInfo<EvmdiffLog> pageInfo = contractEventService.queryPageEvmdiffLogByTxHash(model);
        Result<PageInfo<EvmdiffLog>> result = new Result<>(ResultCode.SUCCESS, pageInfo);
        List<Object> objectList = result.getValue();
        return objectList;
    }

    @ApiOperation(value = "根据交易HASH获取stateDiff状态变化", notes = "根据交易HASH获取stateDiff状态变化")
    @PostMapping("/txHash/chain/stateDiff")
    public List<Object> getStateDiffByHashFromChain(@RequestBody TxTransferHashModel model) {

        PageInfo<StateLog> pageInfo = contractEventService.queryPageStateLogByTxHash(model);
        List<StateLog> stateLogList =  pageInfo.getList();
        List<StateLog> resultLogList = new ArrayList<>();
        for(int i =0; i< stateLogList.size();i++){
            StateLog tempLog = stateLogList.get(i);
            tempLog.setAfter(String.valueOf(Convert.fromWei(tempLog.getAfter(), Convert.Unit.HPB)));
            tempLog.setBefore(String.valueOf(Convert.fromWei(tempLog.getBefore(), Convert.Unit.HPB)));
            resultLogList.add(tempLog);
        }
        pageInfo.setList(resultLogList);
        Result<PageInfo<StateLog>> result = new Result<>(ResultCode.SUCCESS, pageInfo);

        List<Object> objectList = result.getValue();
        System.out.println("eventLogsresult ====" + result.getValue());
        return objectList;
    }


    @ApiOperation(value = "根据交易HASH获取事件日志和状态变化", notes = "根据交易HASH获取状态变化")
    @PostMapping("/txHash/chain/eventLogAndStateDiff")
    public List<Object> getEventLogAndStateDiff(@RequestBody TxTransferHashModel model) {
        TxTransferRecordModel tempModel = new TxTransferRecordModel();
        tempModel.setTxHash(model.getTxHash());
        PageInfo<EventData> pageInfo = contractEventService.queryPageEventDataByTxHash(model.getTxHash(), model.getCurrentPage(), model.getPageSize());
        Result<PageInfo<EventData>> result = new Result<>(ResultCode.SUCCESS, pageInfo);
        // queryEventLogAndStateDiff;

        List<Object> objectList = result.getValue();
        System.out.println("eventLogsresult ====" + result.getValue());
        return objectList;
    }


    @ApiOperation(value = "testEventLog", notes = "testEventLog")
    @PostMapping("/testEventLog")
    public List<Object> testEventLog(HttpServletRequest request) {
        Result<String> result = new Result<>(ResultCode.SUCCESS, "Su");
        hpbFacadeHelper.testPropeller();
        return result.getValue();
    }





}