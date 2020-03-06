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

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hpb.bc.entity.result.ResultCode;
import io.swagger.annotations.ApiImplicitParam;
import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.github.pagehelper.PageInfo;
import com.hpb.bc.constant.BlockConstant;
import com.hpb.bc.entity.Addrs;
import com.hpb.bc.entity.result.Result;
import com.hpb.bc.service.AddrsService;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/addrs")
public class AddressController extends BaseController {
    @Autowired
    private AddrsService addrsService;

    @ApiOperation(value = "查询账户列表", notes = "查询账户列表,['address' 地址]")
    @ApiImplicitParam(name = "reqStrList", value = "分页查询参数,['页码','每页数目']", required = true, dataType = "List<String>")
    @PostMapping("/getPageAddrsList")
    public List<Object> getPageAddrsList(@RequestBody List<String> reqStrList,
                                         HttpServletRequest request, HttpServletResponse response) throws Exception {
        List<String> paramNames = new ArrayList<String>();
        paramNames.add(BlockConstant.CURRENT_PAGE);
        paramNames.add(BlockConstant.PAGE_SIZE);
        Map<String, String> reqParam = this.parseReqStrList(reqStrList, paramNames);
        String currentPage = MapUtils.getString(reqParam, BlockConstant.CURRENT_PAGE);
        String pageSize = MapUtils.getString(reqParam, BlockConstant.PAGE_SIZE);
        Integer cpage = Integer.parseInt(currentPage);
        Integer cpageSize = Integer.parseInt(pageSize);
        Addrs addrs = new Addrs();
        Integer accountType = getAccountType(addrs, reqStrList);
        addrs.setAccountType(accountType);
        Result<PageInfo<Addrs>> result = null;
        if (accountType == null) {
            result = addrsService.getPageAddrs(addrs, cpage.intValue(), cpageSize.intValue());
        }else{

            result = addrsService.getPageContractAddrs(addrs, cpage.intValue(), cpageSize.intValue());
        }
        return result.getValue();
    }

    private Integer getAccountType(Addrs addrs, List<String> reqStrList) {
        try {
            String accountType = reqStrList.get(3);
            return Integer.parseInt(accountType);
        }catch (Exception e){
            return null;
        }
    }

    @ApiOperation(value = "查询账户详情", notes = "查询账户列表,['address' 地址]")
    @ApiImplicitParam(name = "reqStrList", value = " 账户地址 ['address' 地址]", required = true, dataType = "List<String>")
    @PostMapping("/getAddressInfo")
    public List<Object> getAddressInfo(@RequestBody List<String> reqStrList,
                                       HttpServletRequest request, HttpServletResponse response) throws Exception {
        List<String> paramNames = new ArrayList<String>();

        paramNames.add(BlockConstant.ADDRESS);
        Map<String, String> reqParam = this.parseReqStrList(reqStrList, paramNames);

        String address = MapUtils.getString(reqParam, BlockConstant.ADDRESS);
        Addrs addrs = addrsService.getAddrsByAddress(address.trim().toLowerCase());
        Result<Addrs> result = new Result<>(ResultCode.SUCCESS, addrs);
        return result.getValue();
    }

    @ApiOperation(value = "查询账户详情及当前价格", notes = "查询账户列表,['address' 地址]")
    @ApiImplicitParam(name = "reqStrList", value = " 账户地址 ['address' 地址]", required = true, dataType = "List<String>")
    @PostMapping("/getAddressDetailInfo")
    public List<Object> getAddressDetailInfo(@RequestBody List<String> reqStrList, HttpServletRequest request, HttpServletResponse response) throws Exception {
        List<String> paramNames = new ArrayList<String>();

        paramNames.add(BlockConstant.ADDRESS);
        Map<String, String> reqParam = this.parseReqStrList(reqStrList, paramNames);

        String address = MapUtils.getString(reqParam, BlockConstant.ADDRESS);
        Map map = addrsService.getAddrsDetailInfo(address.trim().toLowerCase());
        Result<Map> result = new Result<>(ResultCode.SUCCESS, map);
        return result.getValue();
    }


    @ApiOperation(value = "获取账户概览", notes = "账户预览:totalAddressAmount  持币账号总数\n" +
            " totalAssetsAmount  账户总资产\t\n" +
            "mainNetAssetsAmount 主链资产\n" +
            "toBeTransferedAssetsAmount   待映射资产")
    @GetMapping("/getAddressOverview")
    public List<Object> getAddressOverview(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<String, Object> map = addrsService.getAddressOverview();
        Result<Map<String, Object>> result = new Result<>(ResultCode.SUCCESS, map);
        return result.getValue();
    }

    
    @PostMapping("/queryTxAmountByFromAccount")
    public List<Object> queryTxAmountByFromAccount(@RequestBody List<String> reqStrList) throws Exception {
        List<String> paramNames = new ArrayList<String>();
        paramNames.add(BlockConstant.ADDRESS);
        Map<String, String> reqParam = this.parseReqStrList(reqStrList, paramNames);
        String address = MapUtils.getString(reqParam, BlockConstant.ADDRESS);
        BigDecimal fromTotalAmount = addrsService.queryTxAmountByFromAccount(address.trim().toLowerCase());
        Result<Object> result = new Result<>(ResultCode.SUCCESS, fromTotalAmount);
        return result.getValue();
    }

    
    @PostMapping("/queryTxAmountByToAccount")
    public List<Object> queryTxAmountByToAccount(@RequestBody List<String> reqStrList) throws Exception {
        List<String> paramNames = new ArrayList<String>();
        paramNames.add(BlockConstant.ADDRESS);
        Map<String, String> reqParam = this.parseReqStrList(reqStrList, paramNames);
        String address = MapUtils.getString(reqParam, BlockConstant.ADDRESS);
        BigDecimal toTotalAmount = addrsService.queryTxAmountByToAccount(address.trim().toLowerCase());
        Result<Object> result = new Result<>(ResultCode.SUCCESS, toTotalAmount);
        return result.getValue();
    }

    @ApiOperation(value = "查询账户收入金额（totalToAmount），支出金额（totalFromAmount），总gas花费(totalGasSpent)，余额(balance)", notes = "查询账户列表,['address' 地址]")
    @ApiImplicitParam(name = "reqStrList", value = " 账户地址 ['address' 地址]", required = true, dataType = "List<String>")
    @PostMapping("/queryFromToAmountByAddress")
    public List<Object> queryFromToAmountByAddress(@RequestBody List<String> reqStrList, HttpServletRequest request, HttpServletResponse response) throws Exception {
        List<String> paramNames = new ArrayList<String>();
        paramNames.add(BlockConstant.ADDRESS);
        Map<String, String> reqParam = this.parseReqStrList(reqStrList, paramNames);
        String address = MapUtils.getString(reqParam, BlockConstant.ADDRESS);

        Map map = new HashMap();
        Result<Map> result = new Result<>(ResultCode.SUCCESS, map);
        return result.getValue();
    }


    @ApiOperation(value = "判断是不是智能合约", notes = "判断是不是智能合约")
    @ApiImplicitParam(name = "contractAddress", required = true, dataType = "String contractAddress")
    @PostMapping("/checkContractAddress/{contractAddress}")
    public List<Object> checkContractAddress(@PathVariable("contractAddress") String contractAddress) throws Exception {
        Map map = new HashMap();
        boolean flag = addrsService.checkContractAddress(contractAddress);
        map.put("isContract", flag);
        Result<Map> result = new Result<>(ResultCode.SUCCESS, map);
        return result.getValue();
    }


    @ApiOperation(value = "判断是不是ERC智能合约", notes = "判断是不是ERC智能合约")
    @ApiImplicitParam(name = "contractAddress", required = true, dataType = "String contractAddress")
    @PostMapping("/checkErcContractAddress/{contractAddress}")
    public List<Object> checkErcContractAddress(@PathVariable("contractAddress") String contractAddress) throws Exception {
        Map map = addrsService.checkErcContractAddress(contractAddress);
        Result<Map> result = new Result<>(ResultCode.SUCCESS, map);
        return result.getValue();
    }


    @ApiOperation(value = "查询账户列表", notes = "currentPage,pageSize")
    @GetMapping("/pageAddrsList")
    public List<Object> pageAddrsList(@RequestParam ("currentPage") int currentPage, @RequestParam ("pageSize") int pageSize) throws Exception {
        if(currentPage < 1){
            currentPage = 1;
        }
        if(pageSize <1){
            pageSize = 20;
        }
        Addrs addrs = new Addrs();

        if(pageSize > 1000){
            pageSize = 1000;
        }
        Result<PageInfo<Addrs>> result  = addrsService.getPageAddrs(addrs, currentPage,pageSize);
        return result.getValue();
    }
}