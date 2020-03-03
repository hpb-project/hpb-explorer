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

import com.github.pagehelper.PageInfo;
import com.hpb.bc.comparable.DescTransactionComparator;
import com.hpb.bc.configure.BlockLimitProperties;
import com.hpb.bc.constant.BcConstant;
import com.hpb.bc.constant.BlockConstant;
import com.hpb.bc.entity.Addrs;
import com.hpb.bc.entity.BlockAddrs;
import com.hpb.bc.entity.BlockInfo;
import com.hpb.bc.entity.result.Result;
import com.hpb.bc.entity.result.ResultCode;
import com.hpb.bc.mapper.BlockAddrsMapper;
import com.hpb.bc.model.TransactionDetailModel;
import com.hpb.bc.model.TransactionQueryModel;
import com.hpb.bc.page.HpbPageHelper;
import com.hpb.bc.service.AddrsService;
import com.hpb.bc.service.BlockAddrsService;
import com.hpb.bc.service.BlockFacetService;
import com.hpb.bc.service.TransactionService;
import io.hpb.web3.protocol.admin.Admin;
import io.hpb.web3.protocol.core.DefaultBlockParameterNumber;
import io.hpb.web3.protocol.core.methods.response.HpbBlock;
import io.hpb.web3.protocol.core.methods.response.Transaction;
import io.hpb.web3.protocol.core.methods.response.TransactionReceipt;
import io.hpb.web3.utils.Convert;
import io.hpb.web3.utils.Numeric;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class TransactionServiceImpl implements TransactionService {

    public static final int TIME_OUT = 2;
    public Logger log = LoggerFactory.getLogger(this.getClass());
    @Autowired
    BlockAddrsMapper blockAddrsMapper;
    @Autowired
    BlockAddrsService blockAddrsService;
    @Autowired
    AddrsService addrsService;
    @Autowired
    BlockLimitProperties blockLimitProperties;
    @Autowired
    BlockFacetService blockFacetService;
    @Autowired
    private Admin admin;
    @Autowired
    private HpbPageHelper hpbPageHelper;

    @Override
    public Result<TransactionReceipt> getTransactionByHash(String transactionHash) {
        TransactionReceipt receipt = null;
        try {
            receipt = hpbPageHelper.getTransactionReceipt(transactionHash);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new Result<TransactionReceipt>(ResultCode.SUCCESS, receipt);

    }

    @Override
    public Result<TransactionDetailModel> getTransactionDetailModelByHash(String transactionHash) {
        TransactionDetailModel detailModel = null;
        TransactionReceipt receipt = null;
        try {
            detailModel = hpbPageHelper.getTransactionDetailModelByHash(transactionHash);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new Result<TransactionDetailModel>(ResultCode.SUCCESS, detailModel);
    }

    @Override
    public Result<List<TransactionDetailModel>> getLatestTopNTransactionList(long topNNum) {
        List<Transaction> transactionList = Collections.synchronizedList(new ArrayList<Transaction>());
        try {
            List<HpbBlock.TransactionResult> transactionResultList = hpbPageHelper.getTopNTransactionInfosFromNode(topNNum);
            for (HpbBlock.TransactionResult t : transactionResultList) {
                Transaction o = (Transaction) t.get();
                transactionList.add(o);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        List<TransactionDetailModel> transactionDetailModelList = new ArrayList<>();
        try {
            transactionDetailModelList = hpbPageHelper.getTransactionDetailModelFromTransaction(transactionList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new Result<List<TransactionDetailModel>>(ResultCode.SUCCESS, transactionDetailModelList);
    }

    @Override
    public Result<PageInfo<TransactionDetailModel>> getPageTransactionList(String blockNumber, String currentPage, String pageSize) {
        List<Transaction> startTransactionPageList = Collections.synchronizedList(new ArrayList<Transaction>());
        List<Transaction> endTransactionPageList = Collections.synchronizedList(new ArrayList<Transaction>());
        List<Transaction> middleTransactionPageList = Collections.synchronizedList(new ArrayList<Transaction>());
        List<Transaction> allTransactionPageList = Collections.synchronizedList(new ArrayList<Transaction>());
        Integer totalRecord = this.getTotalTransaction();
        log.info("getPageTransactionList  blockNumber [{}], currentPage [{}],pageSize [{}],totalRecord [{}] ", blockNumber, currentPage, pageSize, totalRecord);

        Integer totalPage = 0;
        Integer pSize = Integer.valueOf(pageSize);

        if (totalRecord % pSize == 0) {
            totalPage = totalRecord / pSize;
        } else {
            totalPage = totalRecord / pSize + 1;
        }
        log.info("result ===" + totalRecord);
        Integer currentPageFirst = (Integer.valueOf(currentPage) - 1) * Integer.valueOf(pageSize) + 1;
        Integer currentPageLast = 0;
        if (Integer.valueOf(currentPage) < totalPage) {
            currentPageLast = Integer.valueOf(currentPage).intValue() * Integer.valueOf(pageSize);
        }
        if (totalPage.equals(Integer.valueOf(currentPage))) {
            currentPageLast = totalRecord;
        }
        Integer tempTotalTransactionNumber = 0;
        Long longBlockNum = Long.valueOf(blockNumber);
        Integer pageNum = 1;
        while (tempTotalTransactionNumber < currentPageFirst) {

            tempTotalTransactionNumber += blockAddrsService.selectSumTxCountPerPage(longBlockNum, pageNum, pSize);
            pageNum += 1;
        }

        Integer firstTransactionPageNum = pageNum;
        Integer totalTransactionNumberInFirstTransactionPage = tempTotalTransactionNumber;

        log.info("当前页的第一条交易所在的BlockAddrs页数{},截止到当前页总交易数目{}", pageNum, tempTotalTransactionNumber);
        PageInfo<BlockAddrs> pageInfoResult = blockAddrsService.getPageBlockAddrsLessThanBlockNumberByExample(longBlockNum, pageNum, pSize);

        Integer totalSumBlockAddrsResultBeforeCurrentPage = tempTotalTransactionNumber - blockAddrsService.selectSumTxCountPerPage(longBlockNum, pageNum, pSize);


        List<BlockAddrs> blockAddrsList = pageInfoResult.getList();

        HpbBlock firstPageHpbBlock = null;
        for (BlockAddrs blockAddrs1 : blockAddrsList) {
            try {
                firstPageHpbBlock = admin.hpbGetBlockByNumber(new DefaultBlockParameterNumber(blockAddrs1.getbNumber()), true).send();

            } catch (IOException e) {
                e.printStackTrace();
            }

            HpbBlock.Block bb = firstPageHpbBlock.getBlock();
            List<HpbBlock.TransactionResult> transactionList = bb.getTransactions();


            if (totalSumBlockAddrsResultBeforeCurrentPage + blockAddrs1.getTxcount() < currentPageFirst) {
                totalSumBlockAddrsResultBeforeCurrentPage = totalSumBlockAddrsResultBeforeCurrentPage + blockAddrs1.getTxcount();

            } else if (totalSumBlockAddrsResultBeforeCurrentPage + blockAddrs1.getTxcount() == currentPageFirst) {

                Transaction transactionIndexZero = (Transaction) transactionList.get(0);
                log.info("transactionIndexZero index ===" + transactionIndexZero.getTransactionIndex());
                for (HpbBlock.TransactionResult t : transactionList) {
                    Transaction transaction = (Transaction) t;
                    if (transaction.getTransactionIndex().equals(BigInteger.ZERO)) {
                        startTransactionPageList.add(transaction);
                        break;
                    }
                }
                break;
            } else if (totalSumBlockAddrsResultBeforeCurrentPage + blockAddrs1.getTxcount() > currentPageFirst) {


                Integer t = currentPageFirst - totalSumBlockAddrsResultBeforeCurrentPage;

                int indexT = t.intValue();
                BigInteger integerTb = BigInteger.valueOf(indexT);
                for (HpbBlock.TransactionResult tx : transactionList) {
                    Transaction transaction = (Transaction) tx;
                    if (transaction.getTransactionIndex().compareTo(integerTb) <= 0) {
                        startTransactionPageList.add(transaction);
                    }
                }
                break;
            }
        }


        log.info("当前页的第一条交易所在的BlockAddrs页数{},截止到当前页总交易数目{}", firstTransactionPageNum, totalTransactionNumberInFirstTransactionPage);

        while (totalTransactionNumberInFirstTransactionPage < currentPageFirst) {

            totalTransactionNumberInFirstTransactionPage += blockAddrsService.selectSumTxCountPerPage(longBlockNum, firstTransactionPageNum, pSize);
            firstTransactionPageNum += 1;
        }
        log.info(" 获取transaction 页面当前页最后一笔交易 所在的区块所在的BlockAddrs 所在的页 {}", firstTransactionPageNum);


        PageInfo<BlockAddrs> pageInfoResultLastTransactionInPage = blockAddrsService.getPageBlockAddrsLessThanBlockNumberByExample(longBlockNum, firstTransactionPageNum, pSize);
        List<BlockAddrs> blockAddrsPageInfoResultLastTransactionInPage = pageInfoResultLastTransactionInPage.getList();

        HpbBlock lastTransactionPageHpbBlock = null;
        for (BlockAddrs blockAddrs1 : blockAddrsPageInfoResultLastTransactionInPage) {

            try {
                lastTransactionPageHpbBlock = admin.hpbGetBlockByNumber(new DefaultBlockParameterNumber(blockAddrs1.getbNumber()), true).send();


            } catch (IOException e) {
                e.printStackTrace();
            }

            HpbBlock.Block bb = lastTransactionPageHpbBlock.getBlock();
            List<HpbBlock.TransactionResult> transactionList = bb.getTransactions();


            if (totalSumBlockAddrsResultBeforeCurrentPage + blockAddrs1.getTxcount() < currentPageLast) {
                totalSumBlockAddrsResultBeforeCurrentPage = totalSumBlockAddrsResultBeforeCurrentPage + blockAddrs1.getTxcount();

            } else if (totalSumBlockAddrsResultBeforeCurrentPage + blockAddrs1.getTxcount() == currentPageLast) {

                Transaction transactionIndexZero = (Transaction) transactionList.get(0);
                log.info("transactionIndexZero index ===" + transactionIndexZero.getTransactionIndex());
                for (HpbBlock.TransactionResult t : transactionList) {
                    Transaction transaction = (Transaction) t;
                    if (transaction.getTransactionIndex().equals(BigInteger.ZERO)) {
                        endTransactionPageList.add(transaction);
                        break;
                    }
                }
                break;
            } else if (totalSumBlockAddrsResultBeforeCurrentPage + blockAddrs1.getTxcount() > currentPageLast) {


                Integer t = currentPageFirst - totalSumBlockAddrsResultBeforeCurrentPage;

                int indexT = t.intValue();
                BigInteger integerTb = BigInteger.valueOf(indexT);
                for (HpbBlock.TransactionResult tx : transactionList) {
                    Transaction transaction = (Transaction) tx;
                    if (transaction.getTransactionIndex().compareTo(integerTb) >= 0) {
                        endTransactionPageList.add(transaction);
                    }
                }
                break;
            }
        }

        BigInteger mStart = firstPageHpbBlock.getBlock().getNumber().add(BigInteger.ONE);
        BigInteger mEnd = lastTransactionPageHpbBlock.getBlock().getNumber().subtract(BigInteger.ONE);

        List<BlockAddrs> blockAddrsListMiddle = blockAddrsService.getBlockAddrsListBetweenStartBlockAndEndBlock(mStart.longValue(), mEnd.longValue());

        try {
            middleTransactionPageList = hpbPageHelper.getTransactionInfosFromBlockAddrsList(blockAddrsListMiddle);
        } catch (Exception e) {
            e.printStackTrace();
        }

        log.info("从首区块{}，尾区块{}交易查询所在的区块间查询所有交易：size {}", mStart, mEnd, middleTransactionPageList.size());

        allTransactionPageList.addAll(startTransactionPageList);
        allTransactionPageList.addAll(middleTransactionPageList);
        allTransactionPageList.addAll(endTransactionPageList);
        log.info("startTransactionPageList.size : {}", startTransactionPageList.size());
        log.info("middleTransactionPageList.size : {}", middleTransactionPageList.size());
        log.info("endTransactionPageList.size : {}", endTransactionPageList.size());
        log.info("allTransactionPageList.size : {}", allTransactionPageList.size());

        Collections.sort(allTransactionPageList, new DescTransactionComparator());

        List<TransactionDetailModel> transactionDetailModelList = null;
        try {
            transactionDetailModelList = hpbPageHelper.getTransactionDetailModelFromTransaction(allTransactionPageList);
        } catch (Exception e) {
            e.printStackTrace();
        }

        PageInfo<TransactionDetailModel> pageInfo = new PageInfo<TransactionDetailModel>(transactionDetailModelList);
        pageInfo.setPageNum(pageNum);
        pageInfo.setPageSize(pSize);
        pageInfo.setTotal(totalRecord);
        pageInfo.setPages(totalPage);
        return new Result<PageInfo<TransactionDetailModel>>(ResultCode.SUCCESS, pageInfo);
    }


    @Override
    public Integer getTotalTransaction() {
        return blockAddrsMapper.getTotalTransactionNumber();
    }


    @Override
    public Result<PageInfo<TransactionDetailModel>> getPageTransactionListByFromAccount(Long blockNumber, String address, Long pageNum, Long pageSize) {

        List<Transaction> transactionList = Collections.synchronizedList(new ArrayList<Transaction>());
        Addrs addrs = addrsService.getAddrsByAddress(address);
        if (addrs != null) {
            {
                List<BlockAddrs> blockAddrsList = new ArrayList<>();
                blockAddrsList = blockAddrsService.getBlockAddrsListFromStartBlockToEndBlockByAddrs(addrs, blockNumber);

                BigInteger x = BigInteger.valueOf(addrs.getNumber());
                String s = Numeric.toHexStringNoPrefix(x);
                log.info("number IntToHex == " + s);
                List<BlockAddrs> finalList = blockAddrsList.stream().filter(e -> Arrays.asList(e.getAddrs().split(",")).contains(s)).collect(Collectors.toList());
                log.info(" finalList.size ===" + finalList.size());

                try {
                    transactionList = hpbPageHelper.getTransactionInfosFromBlockAddrsByFromAddress(finalList, address);
                } catch (Exception e) {
                    log.info(" getPageTransactionListByFromAddress    address:=={} , blockAddrsList.size ==  {} ; transactionList.size == {}", address, finalList.size(), transactionList.size());
                    e.printStackTrace();
                }
            }

        }
        PageInfo<TransactionDetailModel> pageInfo = formatTransactionPageInfo(pageNum, pageSize, transactionList);
        return new Result<PageInfo<TransactionDetailModel>>(ResultCode.SUCCESS, pageInfo);
    }

    private PageInfo<TransactionDetailModel> formatTransactionPageInfo(Long pageNum, Long pageSize, List<Transaction> transactionList) {
        if (transactionList.isEmpty()) {
            PageInfo<TransactionDetailModel> pageInfo = new PageInfo<TransactionDetailModel>(new ArrayList<TransactionDetailModel>());
            return pageInfo;
        }
        int totalPage = 0;
        int totalRecord = transactionList.size();
        int pSize = pageSize.intValue();

        if (totalRecord % pSize == 0) {
            totalPage = totalRecord / pSize;
        } else {
            totalPage = totalRecord / pSize + 1;
        }

        int currentPageFirst = (pageNum.intValue() - 1) * pageSize.intValue() + 1;
        int currentPageLast = 0;

        if (pageNum.intValue() < totalPage) {
            currentPageLast = Integer.valueOf(pageNum.intValue()).intValue() * Integer.valueOf(pageSize.intValue());
        }

        if (totalPage == pageNum.intValue()) {
            currentPageLast = totalRecord;
        }

        List<Transaction> pageTransactionList = new ArrayList<>();

        pageTransactionList = transactionList.subList(currentPageFirst - 1, currentPageLast);


        List<TransactionDetailModel> pageTransactionModelList = new ArrayList<>();

        try {
            pageTransactionModelList = hpbPageHelper.getTransactionDetailModelFromTransaction(pageTransactionList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Collections.sort(pageTransactionModelList, new DescTransactionComparator());
        PageInfo<TransactionDetailModel> pageInfo = new PageInfo<TransactionDetailModel>(pageTransactionModelList);
        pageInfo.setTotal(transactionList.size());
        pageInfo.setPageSize(pageSize.intValue());
        pageInfo.setPageNum(pageNum.intValue());
        pageInfo.setPages(totalPage);
        return pageInfo;
    }

    @Override
    public Result<PageInfo<TransactionDetailModel>> getPageTransactionListByToAccount(Long blockNumber, String address, Long pageNum, Long pageSize) {

        List<Transaction> transactionList = Collections.synchronizedList(new ArrayList<Transaction>());
        Addrs addrs = addrsService.getAddrsByAddress(address);


        if (addrs != null) {
            {
                List<BlockAddrs> blockAddrsList = new ArrayList<>();
                List<BlockAddrs> finalBlockAddrsList = new ArrayList<>();
                blockAddrsList = blockAddrsService.getBlockAddrsListFromStartBlockToEndBlockByAddrs(addrs, blockNumber);

                BigInteger x = BigInteger.valueOf(addrs.getNumber());
                String s = Numeric.toHexStringNoPrefix(x);
                log.info("number IntToHex == " + s);
                List<BlockAddrs> finalList = blockAddrsList.stream().filter(e -> Arrays.asList(e.getAddrs().split(",")).contains(s)).collect(Collectors.toList());
                log.info(" finalList.size ===" + finalList.size());

                try {
                    transactionList = hpbPageHelper.getTransactionInfosFromBlockAddrsByToAddress(finalList, address);
                } catch (Exception e) {
                    log.info(" getPageTransactionListByToAddress    address:=={} , blockAddrsList.size ==  {} ; transactionList.size == {}", address, blockAddrsList.size(), transactionList.size());
                    e.printStackTrace();
                }
            }

        }
        PageInfo<TransactionDetailModel> pageInfo = formatTransactionPageInfo(pageNum, pageSize, transactionList);
        return new Result<PageInfo<TransactionDetailModel>>(ResultCode.SUCCESS, pageInfo);
    }

    @Override
    public Result<PageInfo<TransactionDetailModel>> getPageTransactionListByAccount(Long blockNumber, String address, Long pageNum, Long pageSize) {
        try {
            Addrs addrs = addrsService.getAddrsByAddress(address);
            if (addrs != null) {
                List<BlockAddrs> blockAddrsList = new ArrayList<>();
                blockAddrsList = blockAddrsService.getBlockAddrsListFromStartBlockToEndBlockByAddrs(addrs, blockNumber);

                BigInteger x = BigInteger.valueOf(addrs.getNumber());
                String s = Numeric.toHexStringNoPrefix(x);
                log.info("number IntToHex == " + s);
                List<BlockAddrs> finalList = blockAddrsList.stream().filter(e -> Arrays.asList(e.getAddrs().split(",")).contains(s)).collect(Collectors.toList());
                log.info(" finalList.size ===" + finalList.size());
                int totalTxAmount = 0;
                if (addrs.getToCount() != null) {
                    totalTxAmount += addrs.getToCount().intValue();
                }
                if (addrs.getFromCount() != null) {
                    totalTxAmount += addrs.getFromCount().intValue();
                }
                PageInfo<TransactionDetailModel> pageInfo = hpbPageHelper.getTransactionInfosFromBlockAddrsByAddressForCurrentPageJustForEnoughPage(finalList, address, pageNum.intValue(), pageSize.intValue(), totalTxAmount);
                return new Result<PageInfo<TransactionDetailModel>>(ResultCode.SUCCESS, pageInfo);
            }
            {
                return new Result<PageInfo<TransactionDetailModel>>(ResultCode.FAIL);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new Result<PageInfo<TransactionDetailModel>>(ResultCode.FAIL);
    }

    @Override
    public Result<Object> getPageTransactionByBlockNumber(Long blockNumber, Long pageNum, Long pageSize) {
        try {
            long blockNum = Long.valueOf(blockNumber).longValue();
            BigInteger currentBlockNumber = admin.hpbBlockNumber().send().getBlockNumber();
            if (blockNum > currentBlockNumber.longValue()) {
                String msg = "参数" + blockNum + "超过当前最大区块" + currentBlockNumber.longValue();
                Map map = new HashMap();
                map.put("msg", msg);
                return new Result<>(ResultCode.SUCCESS, map);
            }
            BigInteger transactionCount = admin.hpbGetBlockTransactionCountByNumber(new DefaultBlockParameterNumber(blockNumber)).send().getTransactionCount();
            if (transactionCount == null || BigInteger.ZERO.compareTo(transactionCount) == 0) {
                return new Result<Object>(ResultCode.SUCCESS);
            }

            HpbBlock.Block block = null;
            BlockInfo blockInfo = null;
            if (transactionCount.longValue() > blockLimitProperties.getNotSelectBlockTimeMiniAmount()) {
                if (blockLimitProperties.isIfSelectBiggerBlock()) {
                    blockInfo = blockFacetService.getBlockInfoByBlockNumberByMapFromRedis(blockNumber);
                }
            } else {
                HpbBlock hpbBlock = admin.hpbGetBlockByNumber(new DefaultBlockParameterNumber(blockNumber), false).sendAsync().get(TIME_OUT, TimeUnit.MINUTES);
                block = hpbBlock.getBlock();
                blockInfo = new BlockInfo(block, admin);
            }


            List<TransactionDetailModel> transactionDetailModelList = new ArrayList<>();
            BigInteger startIndex = BigInteger.ZERO;
            BigInteger endIndex = BigInteger.ZERO;
            int totalPage = 0;
            int totalRecord = transactionCount.intValue();
            int pSize = pageSize.intValue();
            if (totalRecord % pSize == 0) {
                totalPage = totalRecord / pSize;
            } else {
                totalPage = totalRecord / pSize + 1;
            }
            if (pageNum == 1) {
                if (transactionCount.intValue() > pageSize) {
                    endIndex = BigInteger.valueOf(pageSize - 1);
                } else {
                    endIndex = transactionCount.subtract(BigInteger.ONE);
                }
            } else {
                long first = (pageNum - 1) * pageSize;
                startIndex = BigInteger.valueOf(first);

                if (totalPage == pageNum) {
                    endIndex = transactionCount.subtract(BigInteger.ONE);
                } else {
                    long last = pageNum * pageSize - 1;
                    endIndex = BigInteger.valueOf(last);
                }
            }

            transactionDetailModelList = hpbPageHelper.getPageTransactionDetailModelByBlockNumberAndTransactionIndex(blockInfo, startIndex, endIndex);
            Collections.sort(transactionDetailModelList, new DescTransactionComparator());
            PageInfo<TransactionDetailModel> pageInfo = new PageInfo<TransactionDetailModel>(transactionDetailModelList);
            pageInfo.setTotal(transactionCount.intValue());
            pageInfo.setPageSize(pageSize.intValue());
            pageInfo.setPageNum(pageNum.intValue());
            pageInfo.setPages(totalPage);
            return new Result<Object>(ResultCode.SUCCESS, pageInfo);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new Result<Object>(ResultCode.FAIL);
    }


    @Override
    public Result<Object> getPageTransactionByBlockHash(String hash, Long pageNum, Long pageSize) {
        try {

            BigInteger transactionCount = admin.hpbGetBlockTransactionCountByHash(hash).send().getTransactionCount();
            if (transactionCount == null) {
                Map map = new HashMap();
                map.put("msg", "根据hash查找不到对应区块");
                return new Result<>(ResultCode.SUCCESS, map);
            }

            BlockInfo blockInfo = blockFacetService.getBlockInfoByBlockHashFromRedis(hash);

            List<TransactionDetailModel> transactionDetailModelList = new ArrayList<>();
            Map map = calacStartIndexAndEndIndex(transactionCount, pageSize, pageNum);
            BigInteger startIndex = BigInteger.valueOf(Long.valueOf(map.get("startIndex").toString()));
            BigInteger endIndex = BigInteger.valueOf(Long.valueOf(map.get("endIndex").toString()));
            int totalPage = Integer.valueOf(map.get("totalPage").toString());

            transactionDetailModelList = hpbPageHelper.getPageTransactionDetailModelByBlockHashAndTransactionIndex(hash, blockInfo, startIndex, endIndex);

            Collections.sort(transactionDetailModelList, new DescTransactionComparator());
            PageInfo<TransactionDetailModel> pageInfo = new PageInfo<TransactionDetailModel>(transactionDetailModelList);
            pageInfo.setTotal(transactionCount.intValue());
            pageInfo.setPageSize(pageSize.intValue());
            pageInfo.setPageNum(pageNum.intValue());
            pageInfo.setPages(totalPage);
            return new Result<Object>(ResultCode.SUCCESS, pageInfo);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    private Map calacStartIndexAndEndIndex(BigInteger transactionCount, Long pageSize, Long pageNum) {
        Map<String, Object> map = new HashMap();

        BigInteger startIndex = BigInteger.ZERO;
        BigInteger endIndex = BigInteger.ZERO;
        int totalPage = 0;
        int totalRecord = transactionCount.intValue();
        int pSize = pageSize.intValue();
        if (totalRecord % pSize == 0) {
            totalPage = totalRecord / pSize;
        } else {
            totalPage = totalRecord / pSize + 1;
        }
        if (pageNum == 1) {
            if (transactionCount.intValue() > pageSize) {
                endIndex = BigInteger.valueOf(pageSize - 1);
            } else {
                endIndex = transactionCount.subtract(BigInteger.ONE);
            }
        } else {
            long first = (pageNum - 1) * pageSize;
            startIndex = BigInteger.valueOf(first);

            if (totalPage == pageNum) {
                endIndex = transactionCount.subtract(BigInteger.ONE);
            } else {
                long last = pageNum * pageSize - 1;
                endIndex = BigInteger.valueOf(last);
            }
        }
        map.put("startIndex", startIndex);
        map.put("endIndex", endIndex);
        map.put("totalPage", totalPage);
        return map;
    }


    @Override
    public Result<PageInfo<TransactionDetailModel>> getPageTransactionListByCurrentBlockNumberOfLastTransaction(String currentPage, String pageSize, String blockNumber, String txHash, String pageFlag) {
        try {

            Integer totalRecord = this.getTotalTransaction();
            log.info("getPageTransactionListByCurrentBlockNumberOfLastTransaction  blockNumber [{}], currentPage [{}],pageSize [{}],totalRecord [{}],txHash [{}] ,pageFlag [{}]", blockNumber, currentPage, pageSize, totalRecord);
            Integer totalPage = 0;
            Integer pSize = Integer.valueOf(pageSize);
            if (totalRecord % pSize == 0) {
                totalPage = totalRecord / pSize;
            } else {
                totalPage = totalRecord / pSize + 1;
            }
            log.info("result ===" + totalRecord);


            Integer currentPageFirst = (Integer.valueOf(currentPage) - 1) * Integer.valueOf(pageSize) + 1;
            Integer currentPageLast = 0;

            if (Integer.valueOf(currentPage) < totalPage) {
                currentPageLast = Integer.valueOf(currentPage).intValue() * Integer.valueOf(pageSize);
            }

            if (totalPage.equals(Integer.valueOf(currentPage))) {
                currentPageLast = totalRecord;
            }

            List<TransactionDetailModel> pageTransactionDetailModelList = Collections.synchronizedList(new ArrayList<TransactionDetailModel>());


            Integer elasticPageSize = blockLimitProperties.getElasticPageSize();

            if (1 == Integer.valueOf(currentPage)) {


                List<BlockAddrs> blockAddrsList = blockAddrsMapper.selectPageBlockAddrsListOfPageSizeCountByDescType(elasticPageSize, Long.valueOf(blockNumber), BlockConstant.DESC);
                pageTransactionDetailModelList = hpbPageHelper.getTransactionDetailModelFromBlockAddrsListAndPageSize(blockAddrsList, pSize);

                Collections.sort(pageTransactionDetailModelList, new DescTransactionComparator());
                if (pageTransactionDetailModelList.size() >= pSize) {
                    pageTransactionDetailModelList = pageTransactionDetailModelList.subList(0, pSize);
                }

            }


            if (Integer.valueOf(totalPage).equals(Integer.valueOf(currentPage))) {

                int totalTxCount = currentPageLast - currentPageFirst + 1;
                List<BlockAddrs> blockAddrsList = blockAddrsMapper.selectPageBlockAddrsListOfPageSizeCountByDescType(elasticPageSize, Long.valueOf(blockNumber), BlockConstant.ASC);
                pageTransactionDetailModelList = hpbPageHelper.getTransactionDetailModelFromBlockAddrsListAndPageSize(blockAddrsList, pSize);

                Collections.sort(pageTransactionDetailModelList, new DescTransactionComparator());

                if (pageTransactionDetailModelList.size() - totalTxCount > 0) {
                    pageTransactionDetailModelList = pageTransactionDetailModelList.subList(pageTransactionDetailModelList.size() - totalTxCount, pageTransactionDetailModelList.size());
                }
            }


            if (BlockConstant.NEXT.equals(pageFlag)) {
                TransactionReceipt receipt = hpbPageHelper.getTransactionReceipt(txHash);
                BigInteger currentTxIndex = receipt.getTransactionIndex();
                BigInteger blockNumberOfLastTx = receipt.getBlockNumber();

                BigInteger transactionCount = admin.hpbGetBlockTransactionCountByNumber(new DefaultBlockParameterNumber(blockNumberOfLastTx)).send().getTransactionCount();


                List<BlockAddrs> blockAddrsList = new ArrayList<>();

                if (transactionCount.intValue() == 1) {
                    blockAddrsList = blockAddrsMapper.selectBlockAddrsListSmallerThanBlockNumberAndLimitNByDescType(blockNumberOfLastTx.longValue() - 1, elasticPageSize, BlockConstant.DESC);
                    pageTransactionDetailModelList = hpbPageHelper.getTransactionDetailModelFromBlockAddrsListAndPageSize(blockAddrsList, pSize);
                }

                if (transactionCount.intValue() > 1) {

                    List<TransactionDetailModel> transactionListOfTargetBlockNumber = hpbPageHelper.getTransactionDetailModelByBlockNumberAndPageSizeAndTxIndex(blockNumberOfLastTx.longValue(), pSize, currentTxIndex, pageFlag, transactionCount);

                    List<BlockAddrs> preBlockAddrsList = new ArrayList<>();

                    pageTransactionDetailModelList.addAll(transactionListOfTargetBlockNumber);
                    if (pageTransactionDetailModelList.size() < pSize) {
                        preBlockAddrsList = blockAddrsMapper.selectBlockAddrsListSmallerThanBlockNumberAndLimitNByDescType(blockNumberOfLastTx.longValue() - 1, elasticPageSize, BlockConstant.DESC);
                        List<TransactionDetailModel> preTransactionPageList = Collections.synchronizedList(new ArrayList<TransactionDetailModel>());
                        preTransactionPageList = hpbPageHelper.getTransactionDetailModelFromBlockAddrsListAndPageSize(preBlockAddrsList, pSize);
                        pageTransactionDetailModelList.addAll(preTransactionPageList);
                    }
                }
                Collections.sort(pageTransactionDetailModelList, new DescTransactionComparator());
                if (pageTransactionDetailModelList.size() >= pSize) {
                    pageTransactionDetailModelList = pageTransactionDetailModelList.subList(0, pSize);
                }

            }


            if (BlockConstant.PRE.equals(pageFlag)) {
                TransactionReceipt receipt = hpbPageHelper.getTransactionReceipt(txHash);
                BigInteger currentTxIndex = receipt.getTransactionIndex();
                BigInteger blockNumberOfLastTx = receipt.getBlockNumber();
                BigInteger transactionCount = admin.hpbGetBlockTransactionCountByNumber(new DefaultBlockParameterNumber(blockNumberOfLastTx)).send().getTransactionCount();


                List<BlockAddrs> blockAddrsList = new ArrayList<>();
                if (transactionCount.intValue() == 1) {
                    blockAddrsList = blockAddrsMapper.selectBlockAddrsListBiggerThanBlockNumberAndLimitNByDescType(blockNumberOfLastTx.longValue() + 1, Long.valueOf(blockNumber), elasticPageSize, BlockConstant.ASC);
                    pageTransactionDetailModelList = hpbPageHelper.getTransactionDetailModelFromBlockAddrsListAndPageSize(blockAddrsList, pSize);

                }


                if (transactionCount.intValue() > 1) {
                    List<BlockAddrs> currentBlockAddrsList = new ArrayList<>();
                    BlockAddrs blockAddrs = new BlockAddrs();
                    blockAddrs.setbNumber(blockNumberOfLastTx.longValue());

                    List<TransactionDetailModel> transactionListOfTargetBlockNumber = hpbPageHelper.getTransactionDetailModelByBlockNumberAndPageSizeAndTxIndex(blockNumberOfLastTx.longValue(), pSize, currentTxIndex, pageFlag, transactionCount);
                    pageTransactionDetailModelList.addAll(transactionListOfTargetBlockNumber);
                    List<BlockAddrs> preBlockAddrsList = new ArrayList<>();
                    if (pageTransactionDetailModelList.size() < pSize) {
                        preBlockAddrsList = blockAddrsMapper.selectBlockAddrsListBiggerThanBlockNumberAndLimitNByDescType(blockNumberOfLastTx.longValue() + 1, Long.valueOf(blockNumber), Integer.valueOf(2 * pSize), BlockConstant.ASC);
                        List<TransactionDetailModel> preTransactionPageList = Collections.synchronizedList(new ArrayList<TransactionDetailModel>());
                        preTransactionPageList = hpbPageHelper.getTransactionDetailModelFromBlockAddrsListAndPageSize(blockAddrsList, pSize);
                        pageTransactionDetailModelList.addAll(preTransactionPageList);
                    }
                }

                Collections.sort(pageTransactionDetailModelList, new DescTransactionComparator());
                if (pageTransactionDetailModelList.size() > pSize) {
                    pageTransactionDetailModelList = pageTransactionDetailModelList.subList(pageTransactionDetailModelList.size() - pSize, pageTransactionDetailModelList.size());
                }
            }

            pageTransactionDetailModelList = pageTransactionDetailModelList;
            PageInfo<TransactionDetailModel> pageInfo = new PageInfo<TransactionDetailModel>(pageTransactionDetailModelList);
            pageInfo.setTotal(totalRecord);
            pageInfo.setPageSize(Integer.valueOf(pageSize).intValue());
            pageInfo.setPageNum(Integer.valueOf(currentPage).intValue());
            pageInfo.setPages(totalPage);

            return new Result<PageInfo<TransactionDetailModel>>(ResultCode.SUCCESS, pageInfo);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new Result<PageInfo<TransactionDetailModel>>(ResultCode.FAIL, null);
    }

    @Override
    public BigDecimal getTransactionAmountByFromAccount(String address, BigInteger blockNumber) {
        BigDecimal totalAmount = BigDecimal.ZERO;
        Addrs addrs = addrsService.getAddrsByAddress(address);
        if (addrs != null) {
            List<BlockAddrs> blockAddrsList = new ArrayList<>();
            blockAddrsList = blockAddrsService.getBlockAddrsListFromStartBlockToEndBlockByAddrs(addrs, blockNumber.longValue());
            BigInteger x = BigInteger.valueOf(addrs.getNumber());
            String s = Numeric.toHexStringNoPrefix(x);
            log.info("number IntToHex == " + s);
            List<BlockAddrs> finalList = blockAddrsList.stream().filter(e -> Arrays.asList(e.getAddrs().split(",")).contains(s)).collect(Collectors.toList());
            log.info(" finalList.size ===" + finalList.size());
            try {
                BigInteger totalInteger = hpbPageHelper.getTotalTransactionAmountFromBlockAddrsByFromAddress(finalList, address);
                totalAmount = Convert.fromWei(totalInteger.toString(), Convert.Unit.HPB);
            } catch (Exception e) {
                log.info(" getPageTransactionListByToAddress    address:=={} , blockAddrsList.size ==  {} ; ", address, blockAddrsList.size());
                e.printStackTrace();
            }
        }
        return totalAmount;
    }

    @Override
    public BigDecimal getTransactionAmountByToAccount(String address, BigInteger blockNumber) {
        BigDecimal totalAmount = BigDecimal.ZERO;
        Addrs addrs = addrsService.getAddrsByAddress(address);
        if (addrs != null) {
            List<BlockAddrs> blockAddrsList = new ArrayList<>();
            blockAddrsList = blockAddrsService.getBlockAddrsListFromStartBlockToEndBlockByAddrs(addrs, blockNumber.longValue());
            BigInteger x = BigInteger.valueOf(addrs.getNumber());
            String s = Numeric.toHexStringNoPrefix(x);
            log.info("number IntToHex == " + s);
            List<BlockAddrs> finalList = blockAddrsList.stream().filter(e -> Arrays.asList(e.getAddrs().split(",")).contains(s)).collect(Collectors.toList());
            log.info(" finalList.size ===" + finalList.size());
            try {
                BigInteger totalInteger = hpbPageHelper.getTotalTransactionAmountFromBlockAddrsByToAddress(finalList, address);
                totalAmount = Convert.fromWei(totalInteger.toString(), Convert.Unit.HPB);
            } catch (Exception e) {
                log.info(" getPageTransactionListByToAddress    address:=={} , blockAddrsList.size ==  {} ;", address, blockAddrsList.size());
                e.printStackTrace();
            }
        }
        return totalAmount;
    }


    @Override
    public Result<PageInfo<TransactionDetailModel>> getPageTransactionListByAccountOfLastTransaction(Long blockNumber, String address, Integer currentPage, Integer pageSize, String txHash, String pageFlag) {
        try {

            Addrs addrs = addrsService.getAddrsByAddress(address);

            if (addrs == null) {
                List<TransactionDetailModel> pageTransactionModelList = new ArrayList<>();
                PageInfo<TransactionDetailModel> pageInfo = new PageInfo<TransactionDetailModel>(pageTransactionModelList);
                pageInfo.setTotal(0);
                pageInfo.setPageSize(Integer.valueOf(pageSize).intValue());
                pageInfo.setPageNum(Integer.valueOf(currentPage).intValue());
                pageInfo.setPages(0);
                return new Result<PageInfo<TransactionDetailModel>>(ResultCode.SUCCESS, pageInfo);
            }

            int totalRecord = 0;
            if (addrs.getToCount() != null) {
                totalRecord += addrs.getToCount().intValue();
            }

            if (addrs.getFromCount() != null) {
                totalRecord += addrs.getFromCount().intValue();
            }


            log.info("getPageTransactionListByCurrentBlockNumberOfLastTransaction  blockNumber [{}], currentPage [{}],pageSize [{}],totalRecord [{}],txHash [{}] ,pageFlag [{}]", blockNumber, currentPage, pageSize, totalRecord);
            Integer totalPage = 0;

            if (totalRecord % pageSize == 0) {
                totalPage = totalRecord / pageSize;
            } else {
                totalPage = totalRecord / pageSize + 1;
            }
            log.info("result ===" + totalRecord);


            Integer currentPageFirst = (currentPage - 1) * pageSize + 1;
            Integer currentPageLast = 0;

            if (currentPage < totalPage) {
                currentPageLast = currentPage * pageSize;
            }

            if (totalPage.equals(Integer.valueOf(currentPage))) {
                currentPageLast = totalRecord;
            }

            List<TransactionDetailModel> pageTransactionModelList = new ArrayList<>();
            List<Transaction> transactionPageList = Collections.synchronizedList(new ArrayList<Transaction>());


            List<BlockAddrs> finalList = getBlockAddrsWithOfTheAddress(addrs.getStartBlock(), addrs.getLastestBlock(), addrs);


            Integer elasticPageSize = blockLimitProperties.getElasticPageSize();


            if (1 == Integer.valueOf(currentPage)) {


                log.info(" finalList.size ===" + finalList.size());
                List<BlockAddrs> tempBlockAddrsList = new ArrayList<>();
                if (finalList.size() > elasticPageSize) {
                    tempBlockAddrsList = finalList.subList(0, elasticPageSize);
                } else {
                    tempBlockAddrsList = finalList;
                }
                transactionPageList = hpbPageHelper.getTransactionInfosFromBlockAddrsByAddress(tempBlockAddrsList, address);
                Collections.sort(transactionPageList, new DescTransactionComparator());
                if (transactionPageList.size() > pageSize) {
                    transactionPageList = transactionPageList.subList(0, pageSize);
                } else {
                    if (totalRecord < transactionPageList.size()) {
                        transactionPageList = transactionPageList.subList(0, totalRecord);
                    } else {
                        totalRecord = transactionPageList.size();
                    }
                }

            } else if (Integer.valueOf(totalPage).equals(Integer.valueOf(currentPage))) {

                int totalTxCount = currentPageLast - currentPageFirst + 1;


                Collections.reverse(finalList);
                if (finalList.size() > elasticPageSize) {
                    finalList = finalList.subList(0, elasticPageSize);
                }
                transactionPageList = hpbPageHelper.getTransactionInfosFromBlockAddrsByAddress(finalList, address);

                Collections.sort(transactionPageList, new DescTransactionComparator());

                transactionPageList = transactionPageList.subList(transactionPageList.size() - totalTxCount, transactionPageList.size());
            } else if (BlockConstant.NEXT.equals(pageFlag)) {
                TransactionReceipt receipt = hpbPageHelper.getTransactionReceipt(txHash);
                BigInteger currentTxIndex = receipt.getTransactionIndex();
                BigInteger blockNumberOfLastTx = receipt.getBlockNumber();
                HpbBlock hpbBlock = admin.hpbGetBlockByNumber(new DefaultBlockParameterNumber(blockNumberOfLastTx), true).sendAsync().get(TIME_OUT, TimeUnit.MINUTES);

                HpbBlock.Block block = hpbBlock.getBlock();


                List<BlockAddrs> blockAddrsList = new ArrayList<>();

                if (block.getTransactions().size() == 1) {
                    Long x = block.getNumber().longValue() - 1;

                    blockAddrsList = getBlockAddrsWithOfTheAddress(addrs.getStartBlock(), x, addrs);

                    if (blockAddrsList.size() > elasticPageSize) {
                        blockAddrsList = blockAddrsList.subList(0, elasticPageSize);
                    }
                    transactionPageList = hpbPageHelper.getTransactionInfosFromBlockAddrsByAddress(blockAddrsList, address);
                }

                if (block.getTransactions().size() > 1) {
                    List<BlockAddrs> currentBlockAddrsList = new ArrayList<>();
                    BlockAddrs blockAddrs = new BlockAddrs();
                    blockAddrs.setbNumber(block.getNumber().longValue());
                    currentBlockAddrsList.add(blockAddrs);

                    List<Transaction> transactionListOfTargetBlockNumber = hpbPageHelper.getTransactionInfosFromBlockAddrsByAddress(currentBlockAddrsList, address);

                    for (Transaction tx : transactionListOfTargetBlockNumber) {
                        if (tx.getTransactionIndex().compareTo(currentTxIndex) < 0) {
                            if (address.equals(tx.getFrom()) || address.equals(tx.getTo())) {
                                transactionPageList.add(tx);
                            }

                        }
                    }
                    List<BlockAddrs> preBlockAddrsList = new ArrayList<>();

                    if (transactionPageList.size() < pageSize) {
                        Long x = block.getNumber().longValue() - 1;

                        preBlockAddrsList = getBlockAddrsWithOfTheAddress(addrs.getStartBlock(), x, addrs);

                        if (preBlockAddrsList.size() > elasticPageSize) {
                            preBlockAddrsList = preBlockAddrsList.subList(0, elasticPageSize);
                        }
                        List<Transaction> preTransactionPageList = Collections.synchronizedList(new ArrayList<Transaction>());
                        preTransactionPageList = hpbPageHelper.getTransactionInfosFromBlockAddrsByAddress(preBlockAddrsList, address);
                        transactionPageList.addAll(preTransactionPageList);
                    }


                }
                Collections.sort(transactionPageList, new DescTransactionComparator());
                if (transactionPageList.size() - pageSize > 0) {
                    transactionPageList = transactionPageList.subList(0, pageSize);
                }

            } else if (BlockConstant.PRE.equals(pageFlag)) {
                TransactionReceipt receipt = hpbPageHelper.getTransactionReceipt(txHash);
                BigInteger currentTxIndex = receipt.getTransactionIndex();
                BigInteger blockNumberOfLastTx = receipt.getBlockNumber();
                HpbBlock hpbBlock = admin.hpbGetBlockByNumber(new DefaultBlockParameterNumber(blockNumberOfLastTx), true).sendAsync().get(TIME_OUT, TimeUnit.MINUTES);
                HpbBlock.Block block = hpbBlock.getBlock();


                List<BlockAddrs> blockAddrsList = new ArrayList<>();
                if (block.getTransactions().size() == 1) {


                    blockAddrsList = getBlockAddrsWithOfTheAddress(block.getNumber().longValue() + 1, addrs.getLastestBlock(), addrs);

                    Collections.reverse(blockAddrsList);


                    if (blockAddrsList.size() > 2 * pageSize) {
                        blockAddrsList = blockAddrsList.subList(0, 2 * pageSize);
                    }
                    transactionPageList = hpbPageHelper.getTransactionInfosFromBlockAddrsByAddress(blockAddrsList, address);

                }


                if (block.getTransactions().size() > 1) {
                    List<BlockAddrs> currentBlockAddrsList = new ArrayList<>();
                    BlockAddrs blockAddrs = new BlockAddrs();
                    blockAddrs.setbNumber(block.getNumber().longValue());
                    List<Transaction> transactionListOfTargetBlockNumber = hpbPageHelper.getTransactionInfosFromBlockAddrsByAddress(currentBlockAddrsList, address);
                    for (Transaction tx : transactionListOfTargetBlockNumber) {
                        if (tx.getTransactionIndex().compareTo(currentTxIndex) > 0) {
                            transactionPageList.add(tx);
                        }
                    }

                    List<BlockAddrs> preBlockAddrsList = new ArrayList<>();
                    if (transactionPageList.size() < pageSize) {


                        preBlockAddrsList = getBlockAddrsWithOfTheAddress(block.getNumber().longValue() + 1, addrs.getLastestBlock(), addrs);

                        Collections.reverse(preBlockAddrsList);


                        if (preBlockAddrsList.size() > elasticPageSize) {
                            preBlockAddrsList = preBlockAddrsList.subList(0, elasticPageSize);
                        }
                        List<Transaction> preTransactionPageList = Collections.synchronizedList(new ArrayList<Transaction>());
                        preTransactionPageList = hpbPageHelper.getTransactionInfosFromBlockAddrsByAddress(preBlockAddrsList, address);
                        transactionPageList.addAll(preTransactionPageList);
                    }
                }
                Collections.sort(transactionPageList, new DescTransactionComparator());
                if (transactionPageList.size() - pageSize > 0) {
                    transactionPageList = transactionPageList.subList(transactionPageList.size() - pageSize, transactionPageList.size());
                }

            }
            pageTransactionModelList = hpbPageHelper.getTransactionDetailModelFromTransaction(transactionPageList);
            PageInfo<TransactionDetailModel> pageInfo = new PageInfo<TransactionDetailModel>(pageTransactionModelList);
            pageInfo.setTotal(totalRecord);
            pageInfo.setPageSize(Integer.valueOf(pageSize).intValue());
            pageInfo.setPageNum(Integer.valueOf(currentPage).intValue());
            pageInfo.setPages(totalPage);

            return new Result<PageInfo<TransactionDetailModel>>(ResultCode.SUCCESS, pageInfo);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new Result<PageInfo<TransactionDetailModel>>(ResultCode.SUCCESS, null);
    }

    private List<BlockAddrs> getBlockAddrsWithOfTheAddress(Long startBlockNumber, Long endBlockNumber, Addrs addrs) {
        List<BlockAddrs> blockAddrsList = new ArrayList<>();
        List<BlockAddrs> finalList = new ArrayList<>();


        blockAddrsList = blockAddrsService.getBlockAddrsListBetweenStartBlockAndEndBlock(startBlockNumber, endBlockNumber + 1000);
        BigInteger x = BigInteger.valueOf(addrs.getNumber());
        String s = Numeric.toHexStringNoPrefix(x);
        log.info("number IntToHex == " + s);
        finalList = blockAddrsList.stream().filter(e -> Arrays.asList(e.getAddrs().split(",")).contains(s)).collect(Collectors.toList());
        return finalList;
    }


    @Override
    public Result<Object> getPageTransactionByBlockHashAndTxType(TransactionQueryModel model) {
        try {
            BigInteger transactionCount = admin.hpbGetBlockTransactionCountByHash(model.getBlockHash()).send().getTransactionCount();
            if (transactionCount == null) {
                Map map = new HashMap();
                map.put("msg", "根据hash查找不到对应区块");
                return new Result<>(ResultCode.SUCCESS, map);
            }
            BlockInfo blockInfo = blockFacetService.getBlockInfoByBlockHashFromRedis(model.getBlockHash());
            List<TransactionDetailModel> transactionDetailModelList = new ArrayList<>();

            Map<String, List<TransactionDetailModel>> txListMap = hpbPageHelper.getPageTransactionDetailModelByBlockHashAndTransactionIndexAndTxType(model.getBlockHash(), model.getTxType(), blockInfo, BigInteger.ZERO, transactionCount);
            if (BcConstant.TX_SMART_CONTRACT_TYPE.equals(model.getTxType())) {
                transactionDetailModelList = txListMap.get(BcConstant.SMART_CONTRACT_TX_LIST);
            }
            if (BcConstant.TX_COMMON_TYPE.equals(model.getTxType())) {
                transactionDetailModelList = txListMap.get(BcConstant.COMMON_CONTRACT_TX_LIST);
            }
            transactionCount = BigInteger.valueOf(transactionDetailModelList.size());

            Map map = calacStartIndexAndEndIndex(transactionCount, Long.valueOf(model.getPageSize()), Long.valueOf(model.getCurrentPage()));
            int totalPage = Integer.valueOf(map.get("totalPage").toString());

            Collections.sort(transactionDetailModelList, new DescTransactionComparator());
            PageInfo<TransactionDetailModel> pageInfo = new PageInfo<TransactionDetailModel>(transactionDetailModelList);
            pageInfo.setTotal(transactionCount.intValue());
            pageInfo.setPageSize(Integer.valueOf(model.getPageSize()).intValue());
            pageInfo.setPageNum(Integer.valueOf(model.getCurrentPage()).intValue());
            pageInfo.setPages(totalPage);
            return new Result<Object>(ResultCode.SUCCESS, pageInfo);
        } catch (IOException e) {
            e.printStackTrace();
            return new Result<Object>(ResultCode.SUCCESS, null);
        }
    }
}






















































































