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

import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import com.hpb.bc.async.AsyncTask;
import com.hpb.bc.comparable.DescBlockNumComparator;
import com.hpb.bc.configure.BlockLimitProperties;
import com.hpb.bc.configure.RedisExpireTimeProperties;
import com.hpb.bc.configure.Web3Properties;
import com.hpb.bc.constant.RedisKeyConstant;
import com.hpb.bc.entity.BlockInfo;
import com.hpb.bc.entity.BlockMaxSize;
import com.hpb.bc.entity.result.Result;
import com.hpb.bc.entity.result.ResultCode;
import com.hpb.bc.page.HpbPageHelper;
import com.hpb.bc.service.*;
import com.hpb.bc.util.Erc20Helper;
import io.hpb.web3.protocol.admin.Admin;
import io.hpb.web3.protocol.core.DefaultBlockParameterNumber;
import io.hpb.web3.protocol.core.methods.response.HpbBlock;
import io.hpb.web3.protocol.core.methods.response.HpbBlock.Block;
import io.hpb.web3.protocol.core.methods.response.Transaction;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigInteger;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Service
public class BlockServiceImpl implements BlockService {
    public static final int TIME_OUT = 2;
    public Logger log = LoggerFactory.getLogger(this.getClass());
    @Autowired
    RedisService redisService;
    @Autowired
    AsyncTask asyncTask;
    @Autowired
    RedisExpireTimeProperties redisExpireTimeProperties;
    @Autowired
    BlockFacetService blockFacetService;
    @Autowired
    BlockLimitProperties blockLimitProperties;
    @Autowired
    Erc20Helper erc20Helper;
    @Autowired
    AddrsService addrsService;
    @Autowired
    private Admin admin;
    @Autowired
    private HpbPageHelper hpbPageHelper;
    @Autowired
    private BlockMaxSizeService blockMaxSizeService;
    @Autowired
    private Web3Properties web3Properties;

    @Override
    public Result<BlockInfo> getBlockInfoByHash(String blockHash) {
        try {
            if (StringUtils.isBlank(blockHash) || blockHash.length() != 66) {
                return new Result<BlockInfo>(ResultCode.SUCCESS);
            }
            HpbBlock hpbBlock = admin.hpbGetBlockByHash(blockHash, false).send();
            Block block = hpbBlock.getBlock();
            BlockInfo blockInfo = new BlockInfo(block, admin);
            asyncQueryBlockDetailInfo(block, blockInfo);
            return new Result<BlockInfo>(ResultCode.SUCCESS, blockInfo);
        } catch (Exception e) {
            log.info(e.getMessage(), e);
            return new Result<BlockInfo>(ResultCode.SUCCESS);
        }

    }

    @Override
    public Result<List<BlockInfo>> getLatestBlockInfoList() {
        try {
            BigInteger blockNumber = admin.hpbBlockNumber().send().getBlockNumber();
            long endBlock = blockNumber.longValue();
            List<BlockInfo> list = hpbPageHelper.getBlockInfosFromNode(endBlock - 10 + 1, endBlock);
            Collections.sort(list, new DescBlockNumComparator());
            return new Result<List<BlockInfo>>(ResultCode.SUCCESS, list);
        } catch (Exception e) {
            log.info(e.getMessage(), e);
            return new Result<List<BlockInfo>>(ResultCode.SUCCESS);
        }
    }


    @Override
    public Result<PageInfo<BlockInfo>> selectPageBlockInfoList(String pageNum, String pageSize, String blockNumber) {
        try {
            BigInteger currentBlockNumber = admin.hpbBlockNumber().send().getBlockNumber();
            BigInteger blockNumberPara = BigInteger.ZERO;
            if (StringUtils.isBlank(blockNumber) || StringUtils.isEmpty(blockNumber)) {
                blockNumberPara = currentBlockNumber;
            } else {
                blockNumberPara = BigInteger.valueOf(Integer.valueOf(blockNumber));
                if (blockNumberPara.compareTo(currentBlockNumber) > 0) {
                    blockNumberPara = currentBlockNumber;
                }
            }
            Page<BlockInfo> page = new Page<BlockInfo>(new Integer(pageNum), new Integer(pageSize));
            page.setTotal(blockNumberPara.longValue());
            page.setOrderBy(" number desc ");
            Long startBlock = new Long(page.getStartRow());
            Long endBlock = new Long(page.getEndRow());
            long factEndIndex = blockNumberPara.longValue() - startBlock;
            long factStartIndex = blockNumberPara.longValue() - endBlock + 1;
            List<BlockInfo> list = hpbPageHelper.getBlockInfosFromNode(factStartIndex, factEndIndex);
            Collections.sort(list, new DescBlockNumComparator());
            page.close();

            PageInfo<BlockInfo> p = new PageInfo<>();
            p.setList(list);
            p.setTotal(page.getTotal());
            p.setPageNum(page.getPageNum());
            p.setPageSize(page.getPageSize());
            p.setPages(page.getPages());

            return new Result<PageInfo<BlockInfo>>(ResultCode.SUCCESS, p);
        } catch (Exception e) {
            log.info(e.getMessage(), e);
            return new Result<PageInfo<BlockInfo>>(ResultCode.SUCCESS);
        }
    }


    @Override
    public Result<BigInteger> getMaxBlockNumber() {
        BigInteger blockNumber = null;
        try {
            blockNumber = admin.hpbBlockNumber().send().getBlockNumber();
            return new Result<>(ResultCode.SUCCESS, blockNumber);
        } catch (IOException e) {
            e.printStackTrace();
            return new Result<>(ResultCode.SUCCESS, BigInteger.valueOf(-1));
        }

    }


    @Override
    public Result<Object> getBlockInfoByBlockNumber(String blockNumber) {

        HpbBlock hpbBlock = null;
        try {
            long blockNum = Long.valueOf(blockNumber).longValue();
            long currentMaxBlockNumber = this.getMaxBlockNumber().getData().longValue();
            if (blockNum > currentMaxBlockNumber) {
                String msg = "参数" + blockNum + "超过当前最大区块" + currentMaxBlockNumber + "不能继续操作";
                Map map = new HashMap();
                map.put("msg", msg);
                return new Result<>(ResultCode.SUCCESS, map);
            }

            if (blockNum < 0) {
                String msg = "参数" + blockNum + "小于最小0区块" + currentMaxBlockNumber + " 不能继续操作";
                Map map = new HashMap();
                map.put("msg", msg);
                return new Result<>(ResultCode.SUCCESS, map);
            }
            Object infoRedis = redisService.getObject(RedisKeyConstant.BLOCK_INFO_PREFIX + blockNum);
            if (infoRedis != null) {
                return new Result<>(ResultCode.SUCCESS, infoRedis);
            }
            hpbBlock = admin.hpbGetBlockByNumber(new DefaultBlockParameterNumber(blockNum), false).sendAsync().get(TIME_OUT, TimeUnit.MINUTES);
            Block block = hpbBlock.getBlock();
            BlockInfo info = new BlockInfo(block, admin);
            asyncQueryBlockDetailInfo(block, info);
            if (block.getTransactions().size() > redisExpireTimeProperties.getRedisMinLimit()) {
                redisService.saveWithExpireTime(RedisKeyConstant.BLOCK_INFO_PREFIX + blockNum, info, redisExpireTimeProperties.getDayNumber(), TimeUnit.DAYS);
            }

            return new Result<>(ResultCode.SUCCESS, info);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
        return new Result<>(ResultCode.SUCCESS);

    }


    public void asyncQueryBlockDetailInfo(HpbBlock.Block block, BlockInfo info) {
        List<HpbBlock.TransactionResult> transactions = block.getTransactions();
        if (transactions.size() > 0) {
            Long transCount = 0L;
            Long contractCount = 0L;
            int totalTxCount = block.getTransactions().size();

            BigInteger totalGas = BigInteger.ZERO;
            BigInteger totalGasSpent = BigInteger.ZERO;

            List<BigInteger> totalGasList = Collections.synchronizedList(new ArrayList<BigInteger>());
            List<BigInteger> totalGasSpentList = Collections.synchronizedList(new ArrayList<BigInteger>());
            List<BigInteger> contractCountList = Collections.synchronizedList(new ArrayList<BigInteger>());
            List<BigInteger> transCountList = Collections.synchronizedList(new ArrayList<BigInteger>());

            try {

                try {
                    for (int i = 0; i < transactions.size(); i++) {
                        HpbBlock.TransactionHash transactionHash = (HpbBlock.TransactionHash) transactions.get(i);
                        Transaction transaction = admin.hpbGetTransactionByHash(transactionHash.get()).sendAsync().get(2, TimeUnit.MINUTES).getTransaction().get();
                        if (addrsService.checkContractAddress(transaction.getTo())) {
                            contractCount = contractCount + 1L;
                        }
                    }
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (TimeoutException e) {
                    e.printStackTrace();
                }

                transCount = Long.valueOf(transactions.size() - contractCount);
                if (transCount < 0) {
                    transCount = 0L;
                }

                if (transactions.size() > blockLimitProperties.getAveragePriceTxMinTxCount()) {
                    transactions = transactions.subList(totalTxCount - blockLimitProperties.getAveragePriceTxMinTxCount(), totalTxCount);
                }

                CountDownLatch countDownLatch = new CountDownLatch(transactions.size());
                for (int i = 0; i < transactions.size(); i++) {
                    HpbBlock.TransactionResult transactionResult = transactions.get(i);
                    asyncTask.queryBlockGasDetailInfoByTransactionResult(countDownLatch, totalGasList, totalGasSpentList, contractCountList, transCountList, transactionResult);
                }
                countDownLatch.await(2, TimeUnit.MINUTES);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            for (BigInteger b : totalGasList) {
                totalGas = totalGas.add(b);
            }
            for (BigInteger b : totalGasSpentList) {
                totalGasSpent = totalGasSpent.add(b);
            }

            info.setTransCount(transCount);
            info.setContractCount(contractCount);
            info.setBlockGasUsed(String.valueOf(block.getGasUsed()));
            info.setBlockGas(String.valueOf(block.getGasUsed()));
            info.setBlockGasSpent(String.valueOf(totalGasSpent));
            info.setAverageGasPrice(String.valueOf(totalGasSpent.divide(totalGas)));
        }
    }


    @Override
    public Result<Map<String, Object>> getLatestOneBlockInfo() {
        try {
            Map<String, Object> blockInfoMap = new HashMap<>();
            BigInteger currentBlockNumber = admin.hpbBlockNumber().send().getBlockNumber();
            BlockInfo blockInfo = getBlockInfoByBlockNumber(currentBlockNumber);
            BlockInfo blockInfo2 = getBlockInfoByBlockNumber(currentBlockNumber.subtract(BigInteger.ONE));
            BigInteger averageTime = blockInfo.getTimestamp().subtract(blockInfo2.getTimestamp());

            blockInfoMap.put("blockInfo", blockInfo);
            blockInfoMap.put("averageTime", averageTime);
            return new Result<>(ResultCode.SUCCESS, blockInfoMap);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
        return new Result<>(ResultCode.SUCCESS);
    }

    private BlockInfo getBlockInfoByBlockNumber(BigInteger currentBlockNumber) throws InterruptedException, ExecutionException, TimeoutException {
        HpbBlock hpbBlock = admin.hpbGetBlockByNumber(new DefaultBlockParameterNumber(currentBlockNumber), true).sendAsync().get(TIME_OUT, TimeUnit.MINUTES);
        Block block = hpbBlock.getBlock();
        return new BlockInfo(block, admin);
    }

    @Override
    public Map<String, Object> getBlockOverview() {
        Map<String, Object> blockInfoMap = null;
        BigInteger averageTime = null;
        try {
            blockInfoMap = new HashMap<>();
            BigInteger currentBlockNumber = admin.hpbBlockNumber().send().getBlockNumber();
            BlockInfo blockInfo = getBlockInfoByBlockNumber(currentBlockNumber);
            BlockInfo blockInfo2 = getBlockInfoByBlockNumber(currentBlockNumber.subtract(BigInteger.ONE));
            averageTime = blockInfo.getTimestamp().subtract(blockInfo2.getTimestamp());

            blockInfoMap.put("maxBlockNumber", currentBlockNumber);
            blockInfoMap.put("averageTime", averageTime);

            BlockMaxSize blockMaxSize = blockMaxSizeService.getBlockMaxSizeById(Integer.valueOf(1));
            blockInfoMap.put("blockMaxSize", blockMaxSize.getMaxSize().longValue() / (1024 * 1024));
            double averageBlockSize = blockMaxSize.getMaxSize().longValue() / (currentBlockNumber.longValue() * 1024);
            blockInfoMap.put("averageBlockSize", averageBlockSize);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }

        return blockInfoMap;
    }

    @Override
    public List<String> getMaxTps() {
        List<String> tps = new ArrayList<>();
        tps.add(web3Properties.getMaxTps());
        return tps;
    }

    @Override
    public String queryAveragePriceByBlockHash(String blockHash) {
        try {
            HpbBlock hpbBlock = admin.hpbGetBlockByHash(blockHash, false).send();
            Block block = hpbBlock.getBlock();
            BlockInfo info = new BlockInfo(block, admin);
            asyncQueryBlockDetailInfo(block, info);

            return info.getAverageGasPrice() + "";
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "0.00";

    }
}