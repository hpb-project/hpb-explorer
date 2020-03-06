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

import com.github.pagehelper.PageInfo;
import com.hpb.bc.constant.BlockConstant;
import com.hpb.bc.entity.BlockInfo;
import com.hpb.bc.entity.result.Result;
import com.hpb.bc.entity.result.ResultCode;
import com.hpb.bc.service.BlockFacetService;
import com.hpb.bc.service.BlockService;
import com.hpb.bc.util.AddressBalanceHelper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/block")
@Api(value = "/block", description = "浏览器/首页")
public class ExploreFrontPageController extends BaseController {
    @Autowired
    AddressBalanceHelper addressBalanceHelper;
    @Autowired
    BlockFacetService blockFacetService;
    @Autowired
    private BlockService blockService;

    @ApiOperation(value = "获取区块信息", notes = "根据区块号获取区块信息, reqStrList [参数1：区块HASH]")
    @ApiImplicitParam(name = "reqStrList", value = "hash列表,['hash']", required = true, dataType = "List<String>")
    @PostMapping("/getBlockInfoByHash")
    public List<Object> getBlockInfoByHash(@RequestBody List<String> reqStrList,
                                           HttpServletRequest request, HttpServletResponse response) throws Exception {
        List<String> paramNames = new ArrayList<String>();
        paramNames.add(BlockConstant.BLOCK_HASH);
        Map<String, String> reqParam = this.parseReqStrList(reqStrList, paramNames);
        String blockHash = MapUtils.getString(reqParam, BlockConstant.BLOCK_HASH);
        Result<BlockInfo> result = blockService.getBlockInfoByHash(blockHash);
        return result.getValue();
    }

    @ApiOperation(value = "获取区块信息", notes = "根据区块号获取区块信息, reqStrList [参数1：区块号]")
    @PostMapping("/getBlockInfoByBlockNumber")
    public List<Object> getBlockInfoByBlockNumber(@RequestBody List<String> reqStrList, HttpServletRequest request, HttpServletResponse response) throws Exception {
        List<String> paramNames = new ArrayList<String>();
        paramNames.add(BlockConstant.BLOCK_NUMBER);
        Map<String, String> reqParam = this.parseReqStrList(reqStrList, paramNames);
        String blockNumber = MapUtils.getString(reqParam, BlockConstant.BLOCK_NUMBER);
        Result<Object> result = blockService.getBlockInfoByBlockNumber(blockNumber);
        return result.getValue();
    }

    @ApiOperation(value = "获取最新区块列表信息,首页20条信息", notes = "获取最新区块列表信息")
    @PostMapping("/getLatestBlockInfoList")
    public List<Object> getLatestBlockInfo(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Result<List<BlockInfo>> result = blockService.getLatestBlockInfoList();
        return result.getValue();
    }

    @ApiOperation(value = "分页获取区块列表信息", notes = "分页获取最新区块列表信息")
    @ApiImplicitParam(name = "reqStrList", value = "分页查询参数,['页码','每页数目','查询时区块号']", required = true, dataType = "List<String>")
    @PostMapping("/getPageBlockInfoList")
    public List<Object> getPageBlockInfoList(@RequestBody List<String> reqStrList,
                                             HttpServletRequest request, HttpServletResponse response) throws Exception {
        List<String> paramNames = new ArrayList<String>();
        paramNames.add(BlockConstant.CURRENT_PAGE);
        paramNames.add(BlockConstant.PAGE_SIZE);
        paramNames.add(BlockConstant.BLOCK_NUMBER);
        Map<String, String> reqParam = this.parseReqStrList(reqStrList, paramNames);
        String currentPage = MapUtils.getString(reqParam, BlockConstant.CURRENT_PAGE);
        String pageSize = MapUtils.getString(reqParam, BlockConstant.PAGE_SIZE);
        String blockNumber = MapUtils.getString(reqParam, BlockConstant.BLOCK_NUMBER);

        Result<PageInfo<BlockInfo>> result = blockService.selectPageBlockInfoList(currentPage, pageSize, blockNumber);
        return result.getValue();
    }

    @ApiOperation(value = "获取最大tps", notes = "获取最大tps")
    @PostMapping("/getMaxTps")
    public List<Object> getMaxTps(HttpServletRequest request, HttpServletResponse response) throws Exception {
        List<String> result = blockService.getMaxTps();
        Result<List<String>> listResult = new Result<>(ResultCode.SUCCESS, result);
        return listResult.getValue();
    }

    @PostMapping("/getMaxBlockNumber")
    @ApiOperation(value = "查询获取最高区块", notes = "无参数，查询获取最高区块")
    public List<Object> getMaxBlockNumber(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Result<BigInteger> result = blockService.getMaxBlockNumber();
        return result.getValue();
    }


    @ApiOperation(value = "获取区块概览", notes = "获取区块概览,maxBlockNumbe  区块高度\n" +
            "averageTime  平均时间\t\n" +
            "blockMaxSize  总区块大小\n" +
            "averageBlockSize   平均区块大小")
    @PostMapping("/getBlockOverview")
    public List<Object> getBlockOverview(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<String, Object> map = blockService.getBlockOverview();
        Result<Map<String, Object>> result = new Result<>(ResultCode.SUCCESS, map);
        return result.getValue();
    }

    @ApiOperation(value = "根据区块hash获取区块平均价格,单位 Wei ")
    @PostMapping("/queryAveragePriceByBlockHash")
    public List<Object> queryAveragePriceByBlockHash(@RequestBody List<String> reqStrList) {
        try {
            List<String> paramNames = new ArrayList<String>();
            paramNames.add(BlockConstant.BLOCK_HASH);
            Map<String, String> reqParam = this.parseReqStrList(reqStrList, paramNames);
            String blockHash = MapUtils.getString(reqParam, BlockConstant.BLOCK_HASH);
            String averagePrice = blockService.queryAveragePriceByBlockHash(blockHash);
            Map<String, Object> map = new HashMap<>();
            map.put("averagePrice", averagePrice);
            Result<Map<String, Object>> result = new Result<>(ResultCode.SUCCESS, map);
            return result.getValue();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new Result<>(ResultCode.SUCCESS).getValue();
    }
}