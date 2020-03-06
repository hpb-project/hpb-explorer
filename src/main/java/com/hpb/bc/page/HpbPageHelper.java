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

package com.hpb.bc.page;

import com.github.pagehelper.PageInfo;
import com.hpb.bc.async.AsyncTask;
import com.hpb.bc.comparable.DescTransactionComparator;
import com.hpb.bc.configure.BlockLimitProperties;
import com.hpb.bc.constant.BcConstant;
import com.hpb.bc.constant.BlockConstant;
import com.hpb.bc.entity.BlockAddrs;
import com.hpb.bc.entity.BlockInfo;
import com.hpb.bc.entity.ContractInfo;
import com.hpb.bc.entity.ContractMethodInfo;
import com.hpb.bc.mapper.ContractInfoMapper;
import com.hpb.bc.mapper.ContractMethodInfoMapper;
import com.hpb.bc.model.TransactionDetailModel;
import io.hpb.web3.protocol.admin.Admin;
import io.hpb.web3.protocol.core.DefaultBlockParameterNumber;
import io.hpb.web3.protocol.core.methods.response.*;
import io.hpb.web3.protocol.core.methods.response.HpbBlock.TransactionResult;
import io.hpb.web3.utils.Convert;
import io.hpb.web3.utils.Numeric;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DurationFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;


@Component
public class HpbPageHelper {
    public static final int TIME_OUT = 2;
    private final static long COUNTDOWNLATCH_AWAIT_MINUTES = 10;
    public Logger log = LoggerFactory.getLogger(HpbPageHelper.class);
    @Autowired
    BlockLimitProperties blockLimitProperties;
    @Autowired
    ContractInfoMapper contractInfoMapper;
    @Autowired
    ContractMethodInfoMapper contractMethodInfoMapper;
    @Autowired
    private AsyncTask asyncTask;
    @Autowired
    private Admin admin;

    public List<TransactionDetailModel> formatTransactionPageInfo(Long pageNum, Long pageSize, List<Transaction> transactionList) {
        if (transactionList.isEmpty()) {
            return null;
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
            pageTransactionModelList = getTransactionDetailModelFromTransaction(pageTransactionList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return pageTransactionModelList;
    }


    public List<BlockInfo> getBlockInfosFromNode(long startBlock, long endBlock) throws Exception {
        List<BlockInfo> blockList = Collections.synchronizedList(new ArrayList<BlockInfo>());
        if (startBlock >= endBlock) {
            return blockList;
        }
        Long num = endBlock - startBlock + 1;
        Instant start = Instant.now();
        CountDownLatch countDownLatch = new CountDownLatch(num.intValue());
        for (Long i = 0L; i < num; i++) {
            long blockNumber = startBlock + i;
            asyncTask.getBlockInfoByNumberFromNode(countDownLatch, blockList, blockNumber);
        }
        countDownLatch.await(COUNTDOWNLATCH_AWAIT_MINUTES, TimeUnit.MINUTES);
        Duration spend = Duration.between(start, Instant.now());
        log.info("从区块链节点上获取{}个连续区块信息,花费时间：{}", num, DurationFormatUtils.formatDuration(spend.toMillis(), "ss.SSS"));
        return blockList;
    }


    @SuppressWarnings("rawtypes")
    public List<TransactionResult> getTransactionInfosFromNode(long startBlock, long endBlock) throws Exception {
        List<TransactionResult> transactionList = Collections.synchronizedList(new ArrayList<TransactionResult>());
        if (startBlock >= endBlock) {
            return transactionList;
        }
        Long num = endBlock - startBlock + 1;
        Instant start = Instant.now();
        CountDownLatch countDownLatch = new CountDownLatch(num.intValue());
        for (Long i = 0L; i < num; i++) {
            long blockNumber = startBlock + i;
            asyncTask.getTransactionInfoByNumberFromNode(countDownLatch, transactionList, blockNumber);
        }
        countDownLatch.await(COUNTDOWNLATCH_AWAIT_MINUTES, TimeUnit.MINUTES);
        Duration spend = Duration.between(start, Instant.now());
        log.info("从区块链节点上获取{}个连续区块的交易信息,花费时间：{}", num, DurationFormatUtils.formatDuration(spend.toMillis(), "ss.SSS"));
        return transactionList;
    }


    public List<TransactionResult> getTopNTransactionInfosFromNode(long topNnum) throws Exception {
        List<TransactionResult> transactionList = Collections.synchronizedList(new ArrayList<TransactionResult>());

        BigInteger blockNumber = admin.hpbBlockNumber().send().getBlockNumber();
        long endBlock = blockNumber.longValue();

        Instant start = Instant.now();
        while (transactionList.size() < topNnum) {
            long startBlock = endBlock - BlockConstant.BLOCK_STEP_500;
            List<TransactionResult> transactionResultList = this.getTransactionInfosFromNode(startBlock, endBlock);
            log.info("从起始区块{},终止区块{} 获取交易信息{}条", startBlock, endBlock, transactionResultList.size());
            transactionList.addAll(transactionResultList);
            endBlock = endBlock - BlockConstant.BLOCK_STEP_500 - 1;
        }

        transactionList = transactionList.subList(0, (int) topNnum);
        Duration spend = Duration.between(start, Instant.now());
        log.info("从区块链节点上获取{}个连续区块的交易信息,花费时间：{}", topNnum, DurationFormatUtils.formatDuration(spend.toMillis(), "ss.SSS"));
        return transactionList;
    }

    public TransactionReceipt getTransactionReceipt(String transactionHash) throws Exception {
        HpbGetTransactionReceipt receipt = admin.hpbGetTransactionReceipt(transactionHash).sendAsync().get(AsyncTask.TIME_OUT, TimeUnit.MINUTES);
        return receipt.getResult();
    }

    public TransactionDetailModel getTransactionDetailModelByHash(String transactionHash) throws Exception {
        TransactionDetailModel detailModel = new TransactionDetailModel();
        HpbGetTransactionReceipt hpbGetTransactionReceipt = admin.hpbGetTransactionReceipt(transactionHash).sendAsync().get(AsyncTask.TIME_OUT, TimeUnit.MINUTES);
        TransactionReceipt receipt = hpbGetTransactionReceipt.getResult();
        HpbTransaction hpbTransaction = admin.hpbGetTransactionByHash(transactionHash).sendAsync().get(AsyncTask.TIME_OUT, TimeUnit.MINUTES);
        Transaction transaction = hpbTransaction.getResult();

        HpbBlock hpbBlock = admin.hpbGetBlockByNumber(new DefaultBlockParameterNumber(transaction.getBlockNumber()), true)
                .sendAsync().get(TIME_OUT, TimeUnit.MINUTES);
        HpbBlock.Block block = hpbBlock.getBlock();

        detailModel.setTransactionHash(receipt.getTransactionHash());
        detailModel.setTransactionStatus(Numeric.decodeQuantity(receipt.getStatus()) + "");
        detailModel.setBlockNumber(transaction.getBlockNumber() + "");
        if (StringUtils.isNotEmpty(receipt.getContractAddress())) {
            detailModel.setTxType(BcConstant.TX_SMART_CONTRACT_TYPE);
        }


        detailModel.setBlockHash(transaction.getBlockHash());

        detailModel.setValue(Numeric.encodeQuantity(transaction.getValue()) + "");

        BigDecimal transactionHpbValue = Convert.fromWei(transaction.getValue() + "", Convert.Unit.HPB);
        detailModel.setValueStr(transactionHpbValue.setScale(8, BigDecimal.ROUND_HALF_EVEN).stripTrailingZeros().toPlainString());

        detailModel.setGasLimit(transaction.getGas().toString());
        detailModel.setGasPrice(Numeric.encodeQuantity(transaction.getGasPrice()) + "");
        detailModel.setGas(Numeric.encodeQuantity(transaction.getGas()));

        detailModel.setGasUsed(receipt.getGasUsed() + "");
        BigInteger gasSpent = receipt.getGasUsed().multiply(transaction.getGasPrice());
        BigDecimal gasSpentHpbValue = Convert.fromWei(String.valueOf(gasSpent), Convert.Unit.HPB);
        detailModel.setGasSpent(gasSpentHpbValue.setScale(8, BigDecimal.ROUND_HALF_EVEN).stripTrailingZeros().toPlainString());

        detailModel.setNonce(Numeric.encodeQuantity(transaction.getNonce()) + "");
        detailModel.setFrom(transaction.getFrom());
        if (StringUtils.isNotEmpty(transaction.getTo())) {
            detailModel.setTo(transaction.getTo());
        } else {
            if (StringUtils.isNotEmpty(receipt.getContractAddress())) {
                detailModel.setTo(receipt.getContractAddress());
            }
        }

        detailModel.setInput(transaction.getInput());
        detailModel.setR(transaction.getR());
        detailModel.setS(transaction.getS());
        detailModel.setV(transaction.getV());
        detailModel.setTransactionTimestamp(block.getTimestamp() + "");
        detailModel.setTransactionIndex(Numeric.encodeQuantity(transaction.getTransactionIndex()));
        detailModel.setLogs(receipt.getLogs());

        if (StringUtils.isNotEmpty(receipt.getTo())) {
            try {
                detailModel.setDecodeInputData(decodeInputData(receipt.getTo(), transaction.getInput()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        return detailModel;
    }


    public List<Transaction> getTransactionInfosFromBlockAddrsByFromAddress(List<BlockAddrs> blockAddrsList, String fromAddress) throws Exception {
        List<Transaction> transactionList = Collections.synchronizedList(new ArrayList<Transaction>());
        if (blockAddrsList.isEmpty()) {
            return transactionList;
        }
        Instant start = Instant.now();
        CountDownLatch countDownLatch = new CountDownLatch(blockAddrsList.size());
        for (BlockAddrs blockAddrs : blockAddrsList) {
            long blockNumber = blockAddrs.getbNumber();
            asyncTask.getTransactionInfoByNumberAndFromAddressFromNode(countDownLatch, transactionList, blockNumber, fromAddress);
        }
        countDownLatch.await(COUNTDOWNLATCH_AWAIT_MINUTES, TimeUnit.MINUTES);
        Duration spend = Duration.between(start, Instant.now());
        log.info("从区块链节点上获取{}个连续区块的交易信息,花费时间：{}", blockAddrsList.size(), DurationFormatUtils.formatDuration(spend.toMillis(), "ss.SSS"));
        return transactionList;
    }


    public List<Transaction> getTransactionInfosFromBlockAddrsByToAddress(List<BlockAddrs> blockAddrsList, String toAddress) throws Exception {
        List<Transaction> transactionList = Collections.synchronizedList(new ArrayList<Transaction>());
        if (blockAddrsList.isEmpty()) {
            return transactionList;
        }
        Instant start = Instant.now();
        CountDownLatch countDownLatch = new CountDownLatch(blockAddrsList.size());
        for (BlockAddrs blockAddrs : blockAddrsList) {
            long blockNumber = blockAddrs.getbNumber();
            asyncTask.getTransactionInfoByNumberAndToAddressFromNode(countDownLatch, transactionList, blockNumber, toAddress);
        }
        countDownLatch.await(COUNTDOWNLATCH_AWAIT_MINUTES, TimeUnit.MINUTES);
        Duration spend = Duration.between(start, Instant.now());
        log.info("从区块链节点上获取{}个连续区块的交易信息,花费时间：{}", blockAddrsList.size(), DurationFormatUtils.formatDuration(spend.toMillis(), "ss.SSS"));
        log.info("transactionList.size ===" + transactionList.size());
        return transactionList;
    }


    public BigInteger getTotalTransactionAmountFromBlockAddrsByToAddress(List<BlockAddrs> blockAddrsList, String toAddress) throws Exception {
        BigInteger totalAmount = BigInteger.ZERO;
        List<Transaction> transactionList = this.getTransactionInfosFromBlockAddrsByToAddress(blockAddrsList, toAddress);
        for (Transaction tx : transactionList) {
            totalAmount = totalAmount.add(tx.getValue());
            log.debug(" totalAmount [{}], tx.getValue() [{}]", totalAmount, tx.getValue());
        }
        return totalAmount;
    }

    public BigInteger getTotalTransactionAmountFromBlockAddrsByFromAddress(List<BlockAddrs> blockAddrsList, String toAddress) throws Exception {
        BigInteger totalAmount = BigInteger.ZERO;
        List<Transaction> transactionList = this.getTransactionInfosFromBlockAddrsByFromAddress(blockAddrsList, toAddress);
        for (Transaction tx : transactionList) {
            totalAmount = totalAmount.add(tx.getValue());
        }
        return totalAmount;
    }

    public Map<String, Object> getFromTotalTransactionAmountAndTotalGasSpentFromBlockAddrsByFromAddress(List<BlockAddrs> blockAddrsList, String address) throws Exception {
        Map<String, Object> result = new HashMap();
        BigInteger fromTotalAmount = BigInteger.ZERO;
        BigDecimal totalGasSpentHpbValue = BigDecimal.ZERO;
        BigDecimal fromTotalAmountHpbValue = BigDecimal.ZERO;
        try {
            List<Transaction> transactionList = this.getTransactionInfosFromBlockAddrsByFromAddress(blockAddrsList, address);
            for (Transaction tx : transactionList) {
                fromTotalAmount = fromTotalAmount.add(tx.getValue());
            }
            BigInteger totalGasSpent = getTotalGasSpentOfTransactionList(transactionList);
            totalGasSpentHpbValue = Convert.fromWei(String.valueOf(totalGasSpent), Convert.Unit.HPB).setScale(8, BigDecimal.ROUND_HALF_EVEN);
            fromTotalAmountHpbValue = Convert.fromWei(String.valueOf(fromTotalAmount), Convert.Unit.HPB).setScale(8, BigDecimal.ROUND_HALF_EVEN);
        } catch (Exception e) {
            e.printStackTrace();
        }
        result.put("totalGasSpent", totalGasSpentHpbValue);
        result.put("totalFromAmount", fromTotalAmountHpbValue);
        return result;
    }


    private BigInteger getTotalGasSpentOfTransactionList(List<Transaction> transactionList) throws InterruptedException {
        BigInteger totalGasSpent = BigInteger.ZERO;
        List<TransactionDetailModel> transactionDetailModelList = Collections.synchronizedList(new ArrayList<TransactionDetailModel>());
        CountDownLatch countDownLatch = new CountDownLatch(transactionList.size());
        for (Transaction tx : transactionList) {
            asyncTask.getTransactionDetailModelWithGasSpentByTransactionList(countDownLatch, transactionDetailModelList, tx);
        }
        countDownLatch.await(2, TimeUnit.MINUTES);
        for (TransactionDetailModel trm : transactionDetailModelList) {
            totalGasSpent = totalGasSpent.add(new BigInteger(trm.getGasSpent()));
        }
        return totalGasSpent;
    }


    public List<Transaction> getTransactionInfosFromBlockAddrsList(List<BlockAddrs> blockAddrsList) throws Exception {
        List<Transaction> transactionList = Collections.synchronizedList(new ArrayList<Transaction>());
        if (blockAddrsList.isEmpty()) {
            return transactionList;
        }
        Instant start = Instant.now();
        CountDownLatch countDownLatch = new CountDownLatch(blockAddrsList.size());
        for (BlockAddrs blockAddrs : blockAddrsList) {
            long blockNumber = blockAddrs.getbNumber();
            asyncTask.getTransactionByNumberFromNode(countDownLatch, transactionList, blockNumber);
        }
        countDownLatch.await(COUNTDOWNLATCH_AWAIT_MINUTES, TimeUnit.MINUTES);
        Duration spend = Duration.between(start, Instant.now());
        log.info("从区块链节点上获取{}个连续区块的交易信息,花费时间：{}", blockAddrsList.size(), DurationFormatUtils.formatDuration(spend.toMillis(), "ss.SSS"));
        log.info("transactionList.size ===" + transactionList.size());
        return transactionList;
    }


    public List<TransactionDetailModel> getTransactionDetailModelFromBlockAddrsListAndPageSize(List<BlockAddrs> blockAddrsList, Integer pageSize) throws Exception {
        List<TransactionDetailModel> transactionList = Collections.synchronizedList(new ArrayList<TransactionDetailModel>());
        if (blockAddrsList.isEmpty()) {
            return transactionList;
        }
        try {
            Instant start = Instant.now();
            int totalTxCountInBlockAddrsListLoop = 0;
            int countDownLatchSize = 0;
            for (int i = 0; i < blockAddrsList.size(); i++) {
                BlockAddrs blockAddrs = blockAddrsList.get(i);
                countDownLatchSize = i;
                if (totalTxCountInBlockAddrsListLoop + blockAddrs.getTxcount() <= pageSize.intValue()) {

                    totalTxCountInBlockAddrsListLoop += blockAddrs.getTxcount();
                } else {

                    break;
                }

            }
            if (countDownLatchSize + blockLimitProperties.getElasticStepCountDownLatchSize() <= blockAddrsList.size()) {
                countDownLatchSize = countDownLatchSize + blockLimitProperties.getElasticStepCountDownLatchSize();
            }

            CountDownLatch countDownLatch = new CountDownLatch(countDownLatchSize);
            for (int i = 0; i < countDownLatchSize; i++) {
                BlockAddrs blockAddrs = blockAddrsList.get(i);
                asyncTask.getTransactionModelByNumberFromNodeAndPageSize(countDownLatch, transactionList, blockAddrs.getbNumber(), pageSize);
            }
            countDownLatch.await(COUNTDOWNLATCH_AWAIT_MINUTES, TimeUnit.MINUTES);
            Duration spend = Duration.between(start, Instant.now());
            log.info("从区块链节点上获取{}个连续区块的交易信息,花费时间：{}", blockAddrsList.size(), DurationFormatUtils.formatDuration(spend.toMillis(), "ss.SSS"));
            log.info("transactionList.size ===" + transactionList.size());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return transactionList;
    }


    public List<TransactionDetailModel> getTransactionDetailModelByBlockNumberAndPageSizeAndTxIndex(long blockNumber, Integer pageSize, BigInteger txIndex, String pageFlag, BigInteger totalTxcount) throws Exception {
        List<TransactionDetailModel> transactionList = Collections.synchronizedList(new ArrayList<TransactionDetailModel>());
        try {
            Instant start = Instant.now();
            HpbBlock hpbBlock = admin.hpbGetBlockByNumber(new DefaultBlockParameterNumber(blockNumber), false).sendAsync().get(TIME_OUT, TimeUnit.MINUTES);
            HpbBlock.Block block = hpbBlock.getBlock();
            CountDownLatch countDownLatch = new CountDownLatch(0);

            if (BlockConstant.NEXT.equals(pageFlag)) {
                int endStepIndex = txIndex.intValue() - pageSize.intValue();

                if (endStepIndex <= 0) {
                    countDownLatch = new CountDownLatch(txIndex.intValue());
                    for (int i = txIndex.intValue() - 1; i >= 0; i--) {
                        asyncTask.getTransactionDetailModelByBlockNumberAndTxIndex(countDownLatch, transactionList, blockNumber, txIndex, totalTxcount, block);
                    }
                } else {
                    countDownLatch = new CountDownLatch(pageSize.intValue());
                    for (int i = 1; i <= pageSize.intValue(); i++) {
                        BigInteger currentIndex = txIndex.subtract(BigInteger.valueOf(i));
                        asyncTask.getTransactionDetailModelByBlockNumberAndTxIndex(countDownLatch, transactionList, blockNumber, currentIndex, totalTxcount, block);
                    }
                }
            } else if (BlockConstant.PRE.equals(pageFlag)) {


                if (totalTxcount.intValue() - (txIndex.intValue() + 1) + 1 - pageSize >= 0) {
                    countDownLatch = new CountDownLatch(pageSize.intValue());
                    for (int i = 1; i < pageSize; i++) {
                        txIndex = txIndex.add(BigInteger.valueOf(i));
                        asyncTask.getTransactionDetailModelByBlockNumberAndTxIndex(countDownLatch, transactionList, blockNumber, txIndex, totalTxcount, block);
                    }
                } else {
                    int countDownLatchSize = totalTxcount.intValue() - (txIndex.intValue() + 1) + 1;
                    countDownLatch = new CountDownLatch(countDownLatchSize);
                    for (int i = txIndex.intValue() + 1; i < totalTxcount.intValue(); i++) {
                        asyncTask.getTransactionDetailModelByBlockNumberAndTxIndex(countDownLatch, transactionList, blockNumber, txIndex, totalTxcount, block);
                    }
                }
            }
            countDownLatch.await(COUNTDOWNLATCH_AWAIT_MINUTES, TimeUnit.MINUTES);
            Duration spend = Duration.between(start, Instant.now());
            log.info("从区块链节点上获取区块{}的交易信息,花费时间：{}", blockNumber, DurationFormatUtils.formatDuration(spend.toMillis(), "ss.SSS"));
            log.info("transactionList.size ===" + transactionList.size());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return transactionList;
    }


    public List<TransactionDetailModel> getTransactionDetailModelFromTransaction(List<Transaction> transactionList) throws Exception {
        List<TransactionDetailModel> transactionDetailList = Collections.synchronizedList(new ArrayList<TransactionDetailModel>());
        if (transactionList.isEmpty()) {
            return transactionDetailList;
        }
        Instant start = Instant.now();
        CountDownLatch countDownLatch = new CountDownLatch(transactionList.size());
        for (Transaction transaction : transactionList) {
            asyncTask.getTransactionDetailByTransactionList(countDownLatch, transactionDetailList, transaction);
        }
        countDownLatch.await(COUNTDOWNLATCH_AWAIT_MINUTES, TimeUnit.MINUTES);
        Duration spend = Duration.between(start, Instant.now());
        log.info(" 把{}条交易转化,花费时间：{}", transactionList.size(), DurationFormatUtils.formatDuration(spend.toMillis(), "ss.SSS"));
        log.info("transactionList.size ===" + transactionList.size());

        Collections.sort(transactionDetailList, new DescTransactionComparator());
        return transactionDetailList;
    }


    public List<Transaction> getTransactionInfosFromBlockAddrsByAddress(List<BlockAddrs> blockAddrsList, String toAddress) throws Exception {
        List<Transaction> transactionList = Collections.synchronizedList(new ArrayList<Transaction>());
        if (blockAddrsList.isEmpty()) {
            return transactionList;
        }
        Instant start = Instant.now();
        CountDownLatch countDownLatch = new CountDownLatch(blockAddrsList.size());
        for (BlockAddrs blockAddrs : blockAddrsList) {
            long blockNumber = blockAddrs.getbNumber();
            asyncTask.getTransactionInfoByNumberAndAddressFromNode(countDownLatch, transactionList, blockNumber, toAddress);
        }
        countDownLatch.await(COUNTDOWNLATCH_AWAIT_MINUTES, TimeUnit.MINUTES);
        Duration spend = Duration.between(start, Instant.now());
        log.info("从区块链节点上获取{}个连续区块的交易信息,花费时间：{}", blockAddrsList.size(), DurationFormatUtils.formatDuration(spend.toMillis(), "ss.SSS"));
        log.info("transactionList.size ===" + transactionList.size());
        return transactionList;
    }

    @Deprecated
    public PageInfo<TransactionDetailModel> getTransactionInfosFromBlockAddrsByAddressForCurrentPageJustForEnoughPage(List<BlockAddrs> blockAddrsList, String address, int currentPage, int pageSize, int totalTxAmount) throws Exception {
        List<Transaction> transactionList = Collections.synchronizedList(new ArrayList<Transaction>());
        if (blockAddrsList.isEmpty()) {
            PageInfo<TransactionDetailModel> pageInfo = new PageInfo<TransactionDetailModel>(new ArrayList<TransactionDetailModel>());
            return pageInfo;
        }
        Instant start = Instant.now();

        Integer totalPage = 0;
        if (totalTxAmount % pageSize == 0) {
            totalPage = totalTxAmount / pageSize;
        } else {
            totalPage = totalTxAmount / pageSize + 1;
        }
        log.info("result ===" + totalTxAmount);


        Integer currentPageFirst = (Integer.valueOf(currentPage) - 1) * pageSize + 1;
        Integer currentPageLast = 0;

        if (Integer.valueOf(currentPage) < totalPage) {
            currentPageLast = Integer.valueOf(currentPage).intValue() * pageSize;
        }

        if (totalPage.equals(currentPage)) {
            currentPageLast = totalTxAmount;
        }
        List<Transaction> startTransactionPageList = Collections.synchronizedList(new ArrayList<Transaction>());
        List<Transaction> allTransactionPageList = Collections.synchronizedList(new ArrayList<Transaction>());
        List<BlockAddrs> currentPageBlockAddrsList = new ArrayList<>();


        if (totalPage.intValue() == currentPage) {

            Collections.reverse(blockAddrsList);

            List<BlockAddrs> blockAddrsListOfLastPage = new ArrayList<>();
            if (blockAddrsList.size() < 2 * pageSize) {
                blockAddrsListOfLastPage = blockAddrsList;
            } else {
                blockAddrsListOfLastPage = blockAddrsList.subList(0, 2 * pageSize);
            }

            CountDownLatch countDownLatch = new CountDownLatch(blockAddrsListOfLastPage.size());
            for (BlockAddrs blockAddrs : blockAddrsListOfLastPage) {
                long blockNumber = blockAddrs.getbNumber();
                asyncTask.getTransactionInfoByNumberAndAddressFromNode(countDownLatch, transactionList, blockNumber, address);
            }
            countDownLatch.await(COUNTDOWNLATCH_AWAIT_MINUTES, TimeUnit.MINUTES);
            Collections.sort(transactionList, new DescTransactionComparator());

            int totalTxCount = currentPageLast - currentPageFirst + 1;
            List<Transaction> txOfLastPage = transactionList.subList(transactionList.size() - totalTxCount, transactionList.size());
            allTransactionPageList.addAll(txOfLastPage);
        } else {

            BlockAddrs blockAddrsOfFirstTx = new BlockAddrs();
            Integer totalAmountBeforeCurrentFirstBlock = 0;
            int firstTxBlockIndex = 0;
            for (int x = 0; x < blockAddrsList.size(); x++) {
                BlockAddrs b = blockAddrsList.get(x);

                if (totalAmountBeforeCurrentFirstBlock + b.getTxcount() >= currentPageFirst) {
                    blockAddrsOfFirstTx = b;
                    break;
                }
                firstTxBlockIndex = x;
                totalAmountBeforeCurrentFirstBlock = totalAmountBeforeCurrentFirstBlock + b.getTxcount();
            }

            HpbBlock firstPageHpbBlock = admin.hpbGetBlockByNumber(new DefaultBlockParameterNumber(blockAddrsOfFirstTx.getbNumber()), true).send();

            HpbBlock.Block firstBlock = firstPageHpbBlock.getBlock();
            List<TransactionResult> firstBlocktransactionResultList = firstBlock.getTransactions();
            if (firstBlock != null && !firstBlocktransactionResultList.isEmpty()) {
                if (totalAmountBeforeCurrentFirstBlock + firstBlocktransactionResultList.size() == currentPageFirst) {
                    log.debug("1.totalAmountBeforeCurrentFirstBlock [{}],firstBlocktransactionResultList.size [{}],currentPageFirst [{}]", totalAmountBeforeCurrentFirstBlock, firstBlocktransactionResultList.size(), currentPageFirst);
                    for (TransactionResult transactionResult : firstBlocktransactionResultList) {
                        Transaction tx = (Transaction) transactionResult;
                        if (address.equals(tx.getTo()) || address.equals(tx.getFrom())) {
                            startTransactionPageList.add(tx);
                        }
                    }
                }

                if (totalAmountBeforeCurrentFirstBlock + firstBlocktransactionResultList.size() > currentPageFirst) {
                    log.debug("2.totalAmountBeforeCurrentFirstBlock [{}],firstBlocktransactionResultList.size [{}],currentPageFirst [{}]", totalAmountBeforeCurrentFirstBlock, firstBlocktransactionResultList.size(), currentPageFirst);
                    for (int i = 0; i < firstBlocktransactionResultList.size(); i++) {
                        Transaction tx = (Transaction) firstBlocktransactionResultList.get(i);
                        int subTx = currentPageFirst - totalAmountBeforeCurrentFirstBlock;
                        if (tx.getTransactionIndex().intValue() < subTx) {
                            if (address.equals(tx.getTo()) || address.equals(tx.getFrom())) {
                                startTransactionPageList.add(tx);
                            }
                        }
                    }
                }
            }

            List<BlockAddrs> middleList = new ArrayList<>();
            if (firstTxBlockIndex + 1 + 2 * pageSize < blockAddrsList.size()) {
                middleList = blockAddrsList.subList(firstTxBlockIndex + 1, firstTxBlockIndex + 1 + 2 * pageSize);
            } else {
                middleList = blockAddrsList.subList(firstTxBlockIndex + 1, blockAddrsList.size());
            }

            CountDownLatch countDownLatch = new CountDownLatch(middleList.size());
            for (BlockAddrs blockAddrs : middleList) {
                long blockNumber = blockAddrs.getbNumber();
                asyncTask.getTransactionInfoByNumberAndAddressFromNode(countDownLatch, transactionList, blockNumber, address);
            }
            countDownLatch.await(COUNTDOWNLATCH_AWAIT_MINUTES, TimeUnit.MINUTES);
            allTransactionPageList.addAll(startTransactionPageList);
            allTransactionPageList.addAll(transactionList);
            Collections.sort(allTransactionPageList, new DescTransactionComparator());

            allTransactionPageList = allTransactionPageList.subList(0, pageSize);
        }

        Duration spend = Duration.between(start, Instant.now());
        log.info("从区块链节点上获取{}个连续区块的交易信息,花费时间：{}", currentPageBlockAddrsList.size(), DurationFormatUtils.formatDuration(spend.toMillis(), "ss.SSS"));
        log.info("allTransactionPageList.size ===" + allTransactionPageList.size());

        List<TransactionDetailModel> pageTransactionModelList = new ArrayList<>();

        try {
            pageTransactionModelList = this.getTransactionDetailModelFromTransaction(allTransactionPageList);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Collections.sort(pageTransactionModelList, new DescTransactionComparator());
        PageInfo<TransactionDetailModel> pageInfo = new PageInfo<TransactionDetailModel>(pageTransactionModelList);
        pageInfo.setTotal(totalTxAmount);
        pageInfo.setPageSize(pageSize);
        pageInfo.setPageNum(currentPage);
        pageInfo.setPages(totalPage);
        return pageInfo;
    }


    public List<TransactionDetailModel> getPageTransactionDetailModelByBlockNumberAndTransactionIndex(BlockInfo blockInfo, BigInteger startIndex, BigInteger endIndex) {
        List<TransactionDetailModel> transactionDetailModelList = Collections.synchronizedList(new ArrayList<TransactionDetailModel>());
        try {
            int count = endIndex.intValue() - startIndex.intValue() + 1;
            CountDownLatch countDownLatch = new CountDownLatch(count);
            for (int i = startIndex.intValue(); i <= endIndex.intValue(); i++) {
                asyncTask.getPageTransactionDetailModelByBlockNumberAndTransactionIndex(countDownLatch, transactionDetailModelList, blockInfo, BigInteger.valueOf(i));
            }
            countDownLatch.await(2, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return transactionDetailModelList;
    }


    public List<TransactionDetailModel> getPageTransactionDetailModelByBlockHashAndTransactionIndex(String hash, BlockInfo blockInfo, BigInteger startIndex, BigInteger endIndex) {
        List<TransactionDetailModel> transactionDetailModelList = Collections.synchronizedList(new ArrayList<TransactionDetailModel>());
        try {
            int count = endIndex.intValue() - startIndex.intValue() + 1;
            CountDownLatch countDownLatch = new CountDownLatch(count);
            for (int i = startIndex.intValue(); i <= endIndex.intValue(); i++) {
                asyncTask.getPageTransactionDetailModelByBlockHashAndTransactionIndex(countDownLatch, transactionDetailModelList, hash, blockInfo, BigInteger.valueOf(i));
            }
            countDownLatch.await(2, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return transactionDetailModelList;
    }

    public Map<String, List<TransactionDetailModel>> getPageTransactionDetailModelByBlockHashAndTransactionIndexAndTxType(String hash, String txType, BlockInfo blockInfo, BigInteger startIndex, BigInteger endIndex) {
        Map<String, List<TransactionDetailModel>> resultMap = new HashMap<>();
        List<TransactionDetailModel> contractTransactionDetailModelList = Collections.synchronizedList(new ArrayList<TransactionDetailModel>());
        List<TransactionDetailModel> commonTransactionDetailModelList = Collections.synchronizedList(new ArrayList<TransactionDetailModel>());
        try {
            int count = endIndex.intValue() - startIndex.intValue() + 1;
            CountDownLatch countDownLatch = new CountDownLatch(count);
            for (int i = startIndex.intValue(); i <= endIndex.intValue(); i++) {
                asyncTask.getPageTransactionDetailModelByBlockHashAndTransactionIndexAndTxType(countDownLatch, contractTransactionDetailModelList, commonTransactionDetailModelList, hash, blockInfo, BigInteger.valueOf(i));
            }
            countDownLatch.await(2, TimeUnit.MINUTES);
            resultMap.put(BcConstant.SMART_CONTRACT_TX_LIST, contractTransactionDetailModelList);
            resultMap.put(BcConstant.COMMON_CONTRACT_TX_LIST, commonTransactionDetailModelList);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return resultMap;
    }

    public String decodeInputData(String toAccount, String original) {
        if (toAccount == null) {
            return "0x";
        } else {
            ContractInfo contractInfo = contractInfoMapper.selectByPrimaryKey(toAccount);
            if (contractInfo == null) {
                return new String(Numeric.hexStringToByteArray(original), StandardCharsets.UTF_8);
            } else {
                if (original != null && original.length() > 10) {
                    String methodId = original.substring(0, 10);
                    ContractMethodInfo contractMethodInfo = contractMethodInfoMapper.selectByContractAndMethod(toAccount, methodId);
                    StringBuilder decodeInput = new StringBuilder("Function: ")
                            .append(contractMethodInfo.getMethodName()).append("<br/>")
                            .append("MethodID: ").append(methodId).append("<br/>");
                    String params = original.substring(10);
                    int paramsNum = params.length() / 64;

                    for (int i = 0; i < paramsNum; i++) {

                        decodeInput.append("[").append(i).append("]:  ").append(params, i * 64, (i + 1) * 64).append("<br/>");
                    }
                    return new String(decodeInput.toString().getBytes(), StandardCharsets.UTF_8);
                } else {
                    return "0x";
                }
            }
        }
    }
}
