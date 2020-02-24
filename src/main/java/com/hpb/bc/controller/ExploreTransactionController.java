package com.hpb.bc.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.github.pagehelper.PageInfo;
import com.hpb.bc.model.TransactionDetailModel;
import com.hpb.bc.model.TransactionQueryModel;
import io.swagger.annotations.ApiImplicitParam;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hpb.bc.constant.BlockConstant;
import com.hpb.bc.entity.result.Result;
import com.hpb.bc.service.BlockService;
import com.hpb.bc.service.TransactionService;
import io.hpb.web3.protocol.core.methods.response.Transaction;
import io.hpb.web3.protocol.core.methods.response.TransactionReceipt;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/transaction")
@Api(value = "/transaction", description = "浏览器查询交易")
public class ExploreTransactionController extends BaseController {

    @Autowired
    private TransactionService transactionService;

    @ApiOperation(value = "根据交易HASH获取交易信息", notes = "根据交易hash，查询交易信息, reqStrList [参数1：交易HASH]")
    @ApiImplicitParam(name = "reqStrList", value = " hash ['hash']", required = true, dataType = "List<String>")
    @PostMapping("/getTransactionByHash")
    public List<Object> getTransactionByHash(@RequestBody List<String> reqStrList,
                                             HttpServletRequest request, HttpServletResponse response) throws Exception {
        List<String> paramNames = new ArrayList<String>();
        paramNames.add(BlockConstant.TRANSACTION_HASH);
        Map<String, String> reqParam = this.parseReqStrList(reqStrList, paramNames);
        String transactionHash = MapUtils.getString(reqParam, BlockConstant.TRANSACTION_HASH);
        Result<TransactionReceipt> result = transactionService.getTransactionByHash(transactionHash);
        return result.getValue();
    }

    @ApiOperation(value = "根据交易HASH获取交易信息", notes = "根据交易hash，查询交易信息, reqStrList [参数1：交易HASH]")
    @ApiImplicitParam(name = "reqStrList", value = " hash ['hash']", required = true, dataType = "List<String>")
    @PostMapping("/getTransactionDetailByHash")
    public List<Object> getTransactionDetailByHash(@RequestBody List<String> reqStrList,
                                                   HttpServletRequest request, HttpServletResponse response) throws Exception {
        List<String> paramNames = new ArrayList<String>();
        paramNames.add(BlockConstant.TRANSACTION_HASH);
        Map<String, String> reqParam = this.parseReqStrList(reqStrList, paramNames);
        String transactionHash = MapUtils.getString(reqParam, BlockConstant.TRANSACTION_HASH);
        Result<TransactionDetailModel> result = transactionService.getTransactionDetailModelByHash(transactionHash);
        return result.getValue();
    }

    @ApiOperation(value = "获取最新交易top n 列表信息", notes = "获取最新top n 交易列表信息")
    @PostMapping("/getLatestTransactionList")
    public List<Object> getLatestTransactionList(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Result<List<TransactionDetailModel>> result = transactionService.getLatestTopNTransactionList(BlockConstant.FRONT_PAGE_TRANSACTION_SIZE);
        return result.getValue();
    }

    @ApiOperation(value = "获取分页的交易信息列表", notes = "获取分页的交易信息列表")
    @ApiImplicitParam(name = "reqStrList", value = "分页查询参数,['页码','每页数目','查询时区块号']", required = true, dataType = "List<String>")
    @PostMapping("/getPageTransactionList")
    public List<Object> getPageTransactionList(@RequestBody List<String> reqStrList,
                                               HttpServletRequest request, HttpServletResponse response) throws Exception {
        List<String> paramNames = new ArrayList<String>();
        paramNames.add(BlockConstant.CURRENT_PAGE);
        paramNames.add(BlockConstant.PAGE_SIZE);
        paramNames.add(BlockConstant.BLOCK_NUMBER);
        Map<String, String> reqParam = this.parseReqStrList(reqStrList, paramNames);
        String currentPage = MapUtils.getString(reqParam, BlockConstant.CURRENT_PAGE);
        String pageSize = MapUtils.getString(reqParam, BlockConstant.PAGE_SIZE);
        String blockNumber = MapUtils.getString(reqParam, BlockConstant.BLOCK_NUMBER);
        Result<PageInfo<TransactionDetailModel>> result = transactionService.getPageTransactionList(blockNumber, currentPage, pageSize);
        return result.getValue();
    }


    @ApiOperation(value = "根据fromAccount查询交易列表信息（打款）", notes = "fromAccount 打款账号  ['currentPage','pageSize','blockNumber','address'] ")
    @ApiImplicitParam(name = "reqStrList", value = " 分页参数 ['currentPage','pageSize','blockNumber','address']", required = true, dataType = "List<String>")
    @PostMapping("/getTransactionListByFromAccount")
    public List<Object> getTransactionListByFromAccount(@RequestBody List<String> reqStrList, HttpServletRequest request, HttpServletResponse response) throws Exception {
        List<String> paramNames = new ArrayList<String>();
        paramNames.add(BlockConstant.CURRENT_PAGE);
        paramNames.add(BlockConstant.PAGE_SIZE);
        paramNames.add(BlockConstant.BLOCK_NUMBER);
        paramNames.add(BlockConstant.ADDRESS);

        Map<String, String> reqParam = this.parseReqStrList(reqStrList, paramNames);
        String currentPage = MapUtils.getString(reqParam, BlockConstant.CURRENT_PAGE);
        String pageSize = MapUtils.getString(reqParam, BlockConstant.PAGE_SIZE);
        String blockNumber = MapUtils.getString(reqParam, BlockConstant.BLOCK_NUMBER);
        String address = MapUtils.getString(reqParam, BlockConstant.ADDRESS);
        // Long blockNumber,String address,Long pageNum, Long pageSize

        Result<PageInfo<TransactionDetailModel>> result = transactionService.getPageTransactionListByFromAccount(Long.valueOf(blockNumber), address.trim().toLowerCase(), Long.valueOf(currentPage), Long.valueOf(pageSize));
        return result.getValue();
    }


    @ApiOperation(value = "根据toAccount查询交易列表信息（收款）", notes = " ['currentPage','pageSize','blockNumber','address'] ")
    @ApiImplicitParam(name = "reqStrList", value = " 分页参数 ['currentPage','pageSize','blockNumber','address']", required = true, dataType = "List<String>")
    @PostMapping("/getTransactionListByToAccount")
    public List<Object> getTransactionListByToAccount(@RequestBody List<String> reqStrList, HttpServletRequest request, HttpServletResponse response) throws Exception {

        List<String> paramNames = new ArrayList<String>();
        paramNames.add(BlockConstant.CURRENT_PAGE);
        paramNames.add(BlockConstant.PAGE_SIZE);
        paramNames.add(BlockConstant.BLOCK_NUMBER);
        paramNames.add(BlockConstant.ADDRESS);

        Map<String, String> reqParam = this.parseReqStrList(reqStrList, paramNames);
        String currentPage = MapUtils.getString(reqParam, BlockConstant.CURRENT_PAGE);
        String pageSize = MapUtils.getString(reqParam, BlockConstant.PAGE_SIZE);
        String address = MapUtils.getString(reqParam, BlockConstant.ADDRESS);
        String blockNumber = MapUtils.getString(reqParam, BlockConstant.BLOCK_NUMBER);

        Result<PageInfo<TransactionDetailModel>> result = transactionService.getPageTransactionListByToAccount(Long.valueOf(blockNumber), address.trim().toLowerCase(), Long.valueOf(currentPage), Long.valueOf(pageSize));
        return result.getValue();
    }


    @ApiOperation(value = "根据account查询所有交易列表信息", notes = " ['currentPage','pageSize','blockNumber','address'] ")
    @ApiImplicitParam(name = "reqStrList", value = " 分页参数 ['currentPage','pageSize','blockNumber','address']", required = true, dataType = "List<String>")
    @PostMapping("/getTransactionListByAccount")
    public List<Object> getTransactionListByAccount(@RequestBody List<String> reqStrList, HttpServletRequest request, HttpServletResponse response) throws Exception {

        List<String> paramNames = new ArrayList<String>();
        paramNames.add(BlockConstant.CURRENT_PAGE);
        paramNames.add(BlockConstant.PAGE_SIZE);
        paramNames.add(BlockConstant.BLOCK_NUMBER);
        paramNames.add(BlockConstant.ADDRESS);

        Map<String, String> reqParam = this.parseReqStrList(reqStrList, paramNames);
        String currentPage = MapUtils.getString(reqParam, BlockConstant.CURRENT_PAGE);
        String pageSize = MapUtils.getString(reqParam, BlockConstant.PAGE_SIZE);
        String address = MapUtils.getString(reqParam, BlockConstant.ADDRESS);
        String blockNumber = MapUtils.getString(reqParam, BlockConstant.BLOCK_NUMBER);

        Result<PageInfo<TransactionDetailModel>> result = transactionService.getPageTransactionListByAccount(Long.valueOf(blockNumber), address.trim().toLowerCase(), Long.valueOf(currentPage), Long.valueOf(pageSize));
        return result.getValue();
    }


    @ApiOperation(value = "根据block hash 查询所有交易列表信息", notes = "['currentPage','pageSize','hash'] ")
    @ApiImplicitParam(name = "reqStrList", value = " 分页参数 ['currentPage','pageSize','hash']", required = true, dataType = "List<String>")
    @PostMapping("/getPageTransactionByBlockHash")
    public List<Object> getPageTransactionByBlockHash(@RequestBody List<String> reqStrList, HttpServletRequest request, HttpServletResponse response) throws Exception {

        List<String> paramNames = new ArrayList<String>();
        paramNames.add(BlockConstant.CURRENT_PAGE);
        paramNames.add(BlockConstant.PAGE_SIZE);
        paramNames.add(BlockConstant.BLOCK_HASH);

        Map<String, String> reqParam = this.parseReqStrList(reqStrList, paramNames);
        String currentPage = MapUtils.getString(reqParam, BlockConstant.CURRENT_PAGE);
        String pageSize = MapUtils.getString(reqParam, BlockConstant.PAGE_SIZE);
        String blockHash = MapUtils.getString(reqParam, BlockConstant.BLOCK_HASH);

        Result<Object> result = transactionService.getPageTransactionByBlockHash(blockHash, Long.valueOf(currentPage), Long.valueOf(pageSize));
        return result.getValue();
    }

    @ApiOperation(value = "根据block hash 查询所有交易列表信息", notes = "['currentPage','pageSize','hash'] ")
    @ApiImplicitParam(name = "reqStrList", value = " 分页参数 ['currentPage','pageSize','hash']", required = true, dataType = "List<String>")
    @PostMapping("/getPageTransactionByBlockHashAndTxType")
    public List<Object> getPageTransactionByBlockHashAndTxType(@RequestBody TransactionQueryModel model) throws Exception {


        Result<Object> result = transactionService.getPageTransactionByBlockHashAndTxType(model);
        return result.getValue();
    }

    @ApiOperation(value = "", notes = "['currentPage','pageSize','maxBlockNumber','txHash','pageFlag'] ")
    @ApiImplicitParam(name = "reqStrList", value = " 分页参数 ['currentPage','pageSize','maxBlockNumber（查询时的区块）','txHash(上一页，当前页第一条记录；下一页，当前页最后一条记录)','pageFlag(PRE上一页，NEXT 下一页) ']", required = true, dataType = "List<String>")
    @PostMapping("/getPageTransactionListByCurrentBlockNumberOfLastTransaction")
    public List<Object> getPageTransactionListByCurrentBlockNumberOfLastTransaction(@RequestBody List<String> reqStrList, HttpServletRequest request, HttpServletResponse response) throws Exception {

        List<String> paramNames = new ArrayList<String>();
        paramNames.add(BlockConstant.CURRENT_PAGE);
        paramNames.add(BlockConstant.PAGE_SIZE);
        paramNames.add(BlockConstant.BLOCK_NUMBER);
        paramNames.add(BlockConstant.TRANSACTION_HASH);
        paramNames.add(BlockConstant.PAGE_FLAG);

        Map<String, String> reqParam = this.parseReqStrList(reqStrList, paramNames);
        String currentPage = MapUtils.getString(reqParam, BlockConstant.CURRENT_PAGE);
        String pageSize = MapUtils.getString(reqParam, BlockConstant.PAGE_SIZE);
        String blockNumber = MapUtils.getString(reqParam, BlockConstant.BLOCK_NUMBER);
        String transactionHash = MapUtils.getString(reqParam, BlockConstant.TRANSACTION_HASH);
        String pageFlag = MapUtils.getString(reqParam, BlockConstant.PAGE_FLAG);

        Result<PageInfo<TransactionDetailModel>> result = transactionService.getPageTransactionListByCurrentBlockNumberOfLastTransaction(currentPage, pageSize, blockNumber, transactionHash, pageFlag);
        return result.getValue();
    }


    @ApiOperation(value = "根据account查询所有交易列表信息", notes = " ['currentPage','pageSize','blockNumber','address'] ")
    @ApiImplicitParam(name = "reqStrList", value = " 分页参数 ['currentPage','pageSize','blockNumber','address','txHash','pageFlag']", required = true, dataType = "List<String>")
    @PostMapping("/getTransactionListByAccountOfLastTransaction")
    public List<Object> getTransactionListByAccountOfLastTransaction(@RequestBody List<String> reqStrList, HttpServletRequest request, HttpServletResponse response) throws Exception {
        List<String> paramNames = new ArrayList<String>();
        paramNames.add(BlockConstant.CURRENT_PAGE);
        paramNames.add(BlockConstant.PAGE_SIZE);
        paramNames.add(BlockConstant.BLOCK_NUMBER);
        paramNames.add(BlockConstant.ADDRESS);
        paramNames.add(BlockConstant.TRANSACTION_HASH);
        paramNames.add(BlockConstant.PAGE_FLAG);

        Map<String, String> reqParam = this.parseReqStrList(reqStrList, paramNames);
        String currentPage = MapUtils.getString(reqParam, BlockConstant.CURRENT_PAGE);
        String pageSize = MapUtils.getString(reqParam, BlockConstant.PAGE_SIZE);
        String address = MapUtils.getString(reqParam, BlockConstant.ADDRESS);
        String blockNumber = MapUtils.getString(reqParam, BlockConstant.BLOCK_NUMBER);
        String transactionHash = MapUtils.getString(reqParam, BlockConstant.TRANSACTION_HASH);
        String pageFlag = MapUtils.getString(reqParam, BlockConstant.PAGE_FLAG);

        Result<PageInfo<TransactionDetailModel>> result = transactionService.getPageTransactionListByAccountOfLastTransaction(Long.valueOf(blockNumber), address.trim().toLowerCase(), Integer.valueOf(currentPage), Integer.valueOf(pageSize), transactionHash, pageFlag);
        return result.getValue();
    }

}