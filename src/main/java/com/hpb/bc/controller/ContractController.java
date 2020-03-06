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

package com.hpb.bc.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hpb.bc.entity.ContractErcStandardInfo;
import com.hpb.bc.entity.result.ResultCode;
import com.hpb.bc.model.ContractErcStandardInfoModel;
import com.hpb.bc.model.ContractInfoModel;
import com.hpb.bc.model.ContractVerifyModel;
import com.hpb.bc.service.ContractErcStandardInfoService;
import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.github.pagehelper.PageInfo;
import com.hpb.bc.constant.BcConstant;
import com.hpb.bc.constant.ContractConstant;
import com.hpb.bc.entity.ContractEventInfo;
import com.hpb.bc.entity.ContractInfo;
import com.hpb.bc.entity.ContractMethodInfo;
import com.hpb.bc.entity.result.Result;
import com.hpb.bc.service.ContractService;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/smart-contract")
public class ContractController extends BaseController {
    @Autowired
    private ContractService contractService;
    @Autowired
    private ContractErcStandardInfoService contractErcStandardInfoService;

    @ApiOperation(value = "获取智能合约日志方法信息",
            notes = "根据智能合约日志事件的HASH获取该日志方法信息, reqStrList [参数1：智能合约日志事件的HASH]")
    @PostMapping("/GetContractEventInfoByEventHash")
    public List<Object> getContractEventInfoByEventHash(@RequestBody List<String> reqStrList, HttpServletRequest request, HttpServletResponse response) throws Exception {
        List<String> paramNames = new ArrayList<String>();
        paramNames.add(ContractConstant.EVENT_HASH);
        Map<String, String> reqParam = this.parseReqStrList(reqStrList, paramNames);
        String eventHash = MapUtils.getString(reqParam, ContractConstant.EVENT_HASH);
        Result<List<ContractEventInfo>> result = contractService.getContractEventInfoByEventHash(eventHash);
        return result.getValue();
    }

    @ApiOperation(value = "根据智能合约账户地址获取对应日志方法信息",
            notes = "根据智能合约账户地址获取该日志方法信息, reqStrList [参数1：智能合约地址]")
    @PostMapping("/GetContractEventInfoByContractAddr")
    public List<Object> getContractEventInfoByContractAddr(@RequestBody List<String> reqStrList,
                                                           HttpServletRequest request, HttpServletResponse response) throws Exception {
        List<String> paramNames = new ArrayList<String>();
        paramNames.add(ContractConstant.CONTRACT_ADDR);
        Map<String, String> reqParam = this.parseReqStrList(reqStrList, paramNames);
        String contractAddr = MapUtils.getString(reqParam, ContractConstant.CONTRACT_ADDR);
        Result<List<ContractEventInfo>> result = contractService.getContractEventInfoByContractAddr(contractAddr);
        return result.getValue();
    }

    @ApiOperation(value = "根据智能合约账户地址获取对应方法信息",
            notes = "根据智能合约账户地址获取该方法信息, reqStrList [参数1：智能合约地址]")
    @PostMapping("/GetContractMethodInfoByContractAddr")
    public List<Object> getContractMethodInfoByContractAddr(@RequestBody List<String> reqStrList,
                                                            HttpServletRequest request, HttpServletResponse response) throws Exception {
        List<String> paramNames = new ArrayList<String>();
        paramNames.add(ContractConstant.CONTRACT_ADDR);
        Map<String, String> reqParam = this.parseReqStrList(reqStrList, paramNames);
        String contractAddr = MapUtils.getString(reqParam, ContractConstant.CONTRACT_ADDR);
        Result<List<ContractMethodInfo>> result = contractService.getContractMethodInfoByContractAddr(contractAddr);
        return result.getValue();
    }

    @ApiOperation(value = "根据智能合约methodId获取对应方法信息",
            notes = "根据智能合约methodId获取该方法信息, reqStrList [参数1：智能合约方法ID]")
    @PostMapping("/GetContractMethodInfoByMethodId")
    public List<Object> getContractMethodInfoByMethodId(@RequestBody List<String> reqStrList,
                                                        HttpServletRequest request, HttpServletResponse response) throws Exception {
        List<String> paramNames = new ArrayList<String>();
        paramNames.add(ContractConstant.METHOD_ID);
        Map<String, String> reqParam = this.parseReqStrList(reqStrList, paramNames);
        String methodId = MapUtils.getString(reqParam, ContractConstant.METHOD_ID);
        Result<List<ContractMethodInfo>> result = contractService.getContractMethodInfoByMethodId(methodId);
        return result.getValue();
    }

    @ApiOperation(value = "根据智能合约账户地址获取对应智能合约详细信息",
            notes = "根据智能合约账户地址获取对应智能合约详细信息, reqStrList [参数1：智能合约地址]")
    @PostMapping("/GetContractInfoByContractAddr")
    public List<Object> getContractInfoByContractAddr(@RequestBody List<String> reqStrList,
                                                      HttpServletRequest request, HttpServletResponse response) throws Exception {
        List<String> paramNames = new ArrayList<String>();
        paramNames.add(ContractConstant.CONTRACT_ADDR);
        Map<String, String> reqParam = this.parseReqStrList(reqStrList, paramNames);
        String contractAddr = MapUtils.getString(reqParam, ContractConstant.CONTRACT_ADDR);
        Result<ContractInfo> result = contractService.getContractInfoByContractAddr(contractAddr);
        return result.getValue();
    }

    @ApiOperation(value = "获取智能合约详细信息",
            notes = "根据智能合约名称获取智能合约详细信息, reqStrList [参数1：智能合约名称]")
    @PostMapping("/GetContractInfoByName")
    public List<Object> getContractInfoByName(@RequestBody List<String> reqStrList,
                                              HttpServletRequest request, HttpServletResponse response) throws Exception {
        List<String> paramNames = new ArrayList<String>();
        paramNames.add(ContractConstant.CONTRACT_NAME);
        Map<String, String> reqParam = this.parseReqStrList(reqStrList, paramNames);
        String contractName = MapUtils.getString(reqParam, ContractConstant.CONTRACT_NAME);
        int pageNum = MapUtils.getIntValue(reqParam, BcConstant.PAGENUM, 1);
        int pageSize = MapUtils.getIntValue(reqParam, BcConstant.PAGESIZE, BcConstant.PAGESIZE_DEFAULT);
        Result<PageInfo<ContractInfo>> result = contractService.getContractInfoByName(contractName, pageNum, pageSize);
        return result.getValue();
    }

    @ApiOperation(value = "验证并开源智能合约代码",
            notes = "验证并开源智能合约代码, reqStrList [参数1：智能合约地址,参数2：合约源代码,参数3：合约ABI,参数3：合约BIN]")
    @PostMapping("/ValidateContractInfo")
    public List<Object> validateContractInfo(@RequestBody List<String> reqStrList, HttpServletRequest request, HttpServletResponse response) throws Exception {
        List<String> paramNames = new ArrayList<String>();
        paramNames.add(ContractConstant.TX_HASH);
        paramNames.add(ContractConstant.CONTRACT_ADDR);
        paramNames.add(ContractConstant.CONTRACT_NAME);
        paramNames.add(ContractConstant.CONTRACT_COMPILER_TYPE);
        paramNames.add(ContractConstant.CONTRACT_COMPILER_VERSION);

        paramNames.add(ContractConstant.CONTRACT_SRC);
        paramNames.add(ContractConstant.CONTRACT_ABI);
        paramNames.add(ContractConstant.CONTRACT_BIN);
        paramNames.add(ContractConstant.OPTIMIZE_FLAG);
        paramNames.add(ContractConstant.CONTRACT_LIBRARY_NAME_ADDRESS_LIST);
        Map<String, String> reqParam = this.parseReqStrList(reqStrList, paramNames);
        String txHash = MapUtils.getString(reqParam, ContractConstant.TX_HASH);
        String contractAddr = MapUtils.getString(reqParam, ContractConstant.CONTRACT_ADDR);
        String contractSrc = MapUtils.getString(reqParam, ContractConstant.CONTRACT_NAME);
        String contractCompilerType = MapUtils.getString(reqParam, ContractConstant.CONTRACT_COMPILER_TYPE);
        String contractCompilerVersion = MapUtils.getString(reqParam, ContractConstant.CONTRACT_COMPILER_VERSION);
        String contractName = MapUtils.getString(reqParam, ContractConstant.CONTRACT_SRC);
        String contractAbi = MapUtils.getString(reqParam, ContractConstant.CONTRACT_ABI);
        String contractBin = MapUtils.getString(reqParam, ContractConstant.CONTRACT_BIN);
        String optimizeFlag = MapUtils.getString(reqParam, ContractConstant.OPTIMIZE_FLAG);
        String contractLibraryAddress = MapUtils.getString(reqParam, ContractConstant.CONTRACT_LIBRARY_NAME_ADDRESS_LIST);
        Result<ContractInfo> result = contractService.validateContractInfo(txHash, contractAddr, contractName, contractSrc, contractAbi, contractBin, optimizeFlag, contractCompilerType, contractCompilerVersion);
        return result.getValue();
    }


    @ApiOperation(value = "发布智能合约的交易，根据发布合约的交易hash，获取智能合约地址", notes = "to address 为空的时候，调用该方法")
    @GetMapping("/contractAddress/{address}")
    public List<Object> getContractAddress(@PathVariable("address") String address) {
        ContractErcStandardInfo contractErcStandardInfo = contractErcStandardInfoService.getContractErcStandardInfoByContractAddress(address);
        Result<ContractErcStandardInfo> result = new Result<>(ResultCode.SUCCESS, contractErcStandardInfo);
        return result.getValue();
    }


    @ApiOperation(value = "ERC20合约列表", notes = "查询ERC20列表信息")
    @PostMapping("/contractAddressList")
    public List<Object> contractAddressList(@RequestBody ContractErcStandardInfoModel model) {
        ContractErcStandardInfo record = new ContractErcStandardInfo();
        BeanUtils.copyProperties(model, record);
        PageInfo<ContractErcStandardInfo> pageInfo = contractErcStandardInfoService.queryPageContractErcStandardInfo(record, model.getCurrentPage(), model.getPageSize());
        Result<PageInfo<ContractErcStandardInfo>> result = new Result<>(ResultCode.SUCCESS, pageInfo);
        return result.getValue();
    }


    @ApiOperation(value = "已经验证合约列表", notes = "已经验证合约列表")
    @PostMapping("/validated/contractAddressList")
    public List<Object> validatedContractAddressList(@RequestBody ContractInfoModel model) {
        PageInfo<ContractInfo> pageInfo = contractService.queryPageContractInfoList(model);
        Result<PageInfo<ContractInfo>> result = new Result<>(ResultCode.SUCCESS, pageInfo);
        return result.getValue();
    }


}