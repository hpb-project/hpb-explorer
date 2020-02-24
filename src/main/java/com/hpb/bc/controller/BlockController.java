package com.hpb.bc.controller;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hpb.bc.constant.BlockConstant;
import com.hpb.bc.entity.BlockInfo;
import com.hpb.bc.entity.result.Result;
import com.hpb.bc.service.BlockService;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/")
public class BlockController extends BaseController {
    @Autowired
    private BlockService blockService;

    @ApiOperation(value = "获取区块信息", notes = "根据区块号获取区块信息, reqStrList [参数1：区块HASH]")
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


    @ApiOperation(value = "获取最新区块列表信息", notes = "获取最新区块列表信息")
    @PostMapping("/getLatestBlockInfoList")
    public List<Object> getLatestBlockInfo(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Result<List<BlockInfo>> result = blockService.getLatestBlockInfoList();
        return result.getValue();
    }

    @ApiOperation(value = "获取区块列表信息", notes = "获取区块列表信息")
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


    @ApiOperation(value = "获取最新的区块信息", notes = "根据区块号获取区块信息, reqStrList [参数1：]")
    @PostMapping("/getLatestOneBlockInfo")
    public List<Object> getLatestOneBlockInfo(HttpServletRequest request, HttpServletResponse response) throws Exception {
        List<String> paramNames = new ArrayList<String>();
        Result<Map<String, Object>> result = blockService.getLatestOneBlockInfo();
        return result.getValue();
    }

    //overview

}