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

package com.hpb.bc.async;

import com.hpb.bc.configure.BlockLimitProperties;
import com.hpb.bc.constant.BcConstant;
import com.hpb.bc.entity.BlockInfo;
import com.hpb.bc.model.TransactionDetailModel;
import com.hpb.bc.service.BlockFacetService;
import io.hpb.web3.abi.datatypes.Address;
import io.hpb.web3.protocol.admin.Admin;
import io.hpb.web3.protocol.core.DefaultBlockParameterName;
import io.hpb.web3.protocol.core.DefaultBlockParameterNumber;
import io.hpb.web3.protocol.core.methods.response.*;
import io.hpb.web3.protocol.core.methods.response.HpbBlock.Block;
import io.hpb.web3.protocol.core.methods.response.HpbBlock.TransactionResult;
import io.hpb.web3.utils.Convert;
import io.hpb.web3.utils.Numeric;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Component
public class AsyncTask {

    public static final int TIME_OUT = 2;
    public Logger log = LoggerFactory.getLogger(AsyncTask.class);
    @Autowired
    BlockFacetService blockFacetService;

    @Autowired
    BlockLimitProperties blockLimitProperties;
    @Autowired
    private Admin admin;

    @Async
    public void getBlockInfoByNumberFromNode(CountDownLatch countDownLatch, List<BlockInfo> blockList, long blockNumber) {
        try {
            HpbBlock hpbBlock = admin.hpbGetBlockByNumber(new DefaultBlockParameterNumber(blockNumber), false).sendAsync().get(TIME_OUT, TimeUnit.MINUTES);
            Block bb = hpbBlock.getBlock();
            BlockInfo info = new BlockInfo(bb, admin);
            info.setTransCount((long) bb.getTransactions().size());
            blockList.add(info);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            e.printStackTrace();
        } finally {
            countDownLatch.countDown();
        }
    }

    @Async
    public void getVoterSnapshotBalanceFromNode(CountDownLatch countDownLatch, Address addr, Map<String, BigInteger> m, BigInteger _currentSnapshotBlock) {
        try {
            HpbGetBalance balance = admin.hpbGetBalance(addr.getValue(), new DefaultBlockParameterNumber(_currentSnapshotBlock)).send();
            BigInteger bigBalance = balance.getBalance();
            m.put(addr.getValue(), bigBalance);
            countDownLatch.countDown();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Async
    public void getVoterSnapshotBalanceFromNode(CountDownLatch countDownLatch, Address addr,
                                                Map<String, BigInteger> m, BigInteger _currentSnapshotBlock, Admin newAdmin) {
        try {
            HpbGetBalance balance = newAdmin.hpbGetBalance(addr.getValue(),
                    new DefaultBlockParameterNumber(_currentSnapshotBlock)).send();
            BigInteger bigBalance = balance.getBalance();
            m.put(addr.getValue(), bigBalance);
            countDownLatch.countDown();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Async
    @SuppressWarnings("rawtypes")
    public void getTransactionInfoByNumberFromNode(CountDownLatch countDownLatch,
                                                   List<TransactionResult> transactionList, long blockNumber) {
        try {
            HpbBlock hpbBlock = admin.hpbGetBlockByNumber(new DefaultBlockParameterNumber(blockNumber), true)
                    .sendAsync().get(TIME_OUT, TimeUnit.MINUTES);
            Block block = hpbBlock.getBlock();
            List<TransactionResult> transactions = block.getTransactions();
            if (CollectionUtils.isNotEmpty(transactions)) {
                transactionList.addAll(transactions);
            }
            countDownLatch.countDown();
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            e.printStackTrace();
        }
    }

    @Async
    @SuppressWarnings("rawtypes")
    public void getTransactionByNumberFromNode(CountDownLatch countDownLatch,
                                               List<Transaction> transactionList, long blockNumber) {
        try {
            HpbBlock hpbBlock = admin.hpbGetBlockByNumber(new DefaultBlockParameterNumber(blockNumber), true)
                    .sendAsync().get(TIME_OUT, TimeUnit.MINUTES);
            Block block = hpbBlock.getBlock();
            List<TransactionResult> transactions = block.getTransactions();
            if (CollectionUtils.isNotEmpty(transactions)) {
                if (CollectionUtils.isNotEmpty(transactions)) {
                    transactions.stream().forEach(transactionResult -> {
                        Transaction transaction = (Transaction) transactionResult.get();
                        transactionList.add(transaction);
                    });
                }
            }
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            e.printStackTrace();
        } finally {
            countDownLatch.countDown();
        }
    }

    @Async
    @SuppressWarnings("rawtypes")
    public void getTransactionByNumberFromNodeAndPageSize(CountDownLatch countDownLatch, List<Transaction> transactionList, long blockNumber, Integer pageSize) {
        try {
            BigInteger totalTxCount = admin.hpbGetBlockTransactionCountByNumber(new DefaultBlockParameterNumber(blockNumber)).send().getTransactionCount();
            if (totalTxCount.intValue() < 30) {
                HpbBlock hpbBlock = admin.hpbGetBlockByNumber(new DefaultBlockParameterNumber(blockNumber), true)
                        .sendAsync().get(TIME_OUT, TimeUnit.MINUTES);
                Block block = hpbBlock.getBlock();
                List<TransactionResult> transactions = block.getTransactions();
                if (CollectionUtils.isNotEmpty(transactions)) {
                    if (CollectionUtils.isNotEmpty(transactions)) {
                        transactions.stream().forEach(transactionResult -> {
                            Transaction transaction = (Transaction) transactionResult.get();
                            transactionList.add(transaction);
                        });
                    }
                }
            } else {
                for (int i = totalTxCount.intValue() - 1; i > totalTxCount.intValue() - 1 - pageSize.intValue(); i--) {
                    Optional<Transaction> transactionOptional = admin.hpbGetTransactionByBlockNumberAndIndex(new DefaultBlockParameterNumber(blockNumber), BigInteger.valueOf(i)).send().getTransaction();
                    if (transactionOptional.isPresent()) {
                        transactionList.add(transactionOptional.get());
                    }
                }
            }

        } catch (InterruptedException | ExecutionException | TimeoutException | IOException e) {
            e.printStackTrace();
        } finally {
            countDownLatch.countDown();
        }
    }

    @Async
    @SuppressWarnings("rawtypes")
    public void getTransactionModelByNumberFromNodeAndPageSize(CountDownLatch countDownLatch, List<TransactionDetailModel> transactionList, long blockNumber, Integer pageSize) {
        try {
            HpbBlock hpbBlockSimple = admin.hpbGetBlockByNumber(new DefaultBlockParameterNumber(blockNumber), false).sendAsync().get(TIME_OUT, TimeUnit.MINUTES);
            Block blockSimpl = hpbBlockSimple.getBlock();
            BigInteger totalTxCount = admin.hpbGetBlockTransactionCountByNumber(new DefaultBlockParameterNumber(blockNumber)).send().getTransactionCount();
            if (totalTxCount.intValue() < 30) {
                HpbBlock hpbBlock = admin.hpbGetBlockByNumber(new DefaultBlockParameterNumber(blockNumber), true)
                        .sendAsync().get(TIME_OUT, TimeUnit.MINUTES);
                Block block = hpbBlock.getBlock();
                List<TransactionResult> transactions = block.getTransactions();
                if (CollectionUtils.isNotEmpty(transactions)) {
                    if (CollectionUtils.isNotEmpty(transactions)) {
                        transactions.stream().forEach(transactionResult -> {
                            Transaction transaction = (Transaction) transactionResult.get();
                            transactionList.add(this.getTransactionDetailModelFromTransaction(transaction, block.getTimestamp(), block.getTimestamp()));
                        });
                    }
                }
            } else {
                for (int i = totalTxCount.intValue() - 1; i > totalTxCount.intValue() - 1 - pageSize.intValue(); i--) {
                    Optional<Transaction> transactionOptional = admin.hpbGetTransactionByBlockNumberAndIndex(new DefaultBlockParameterNumber(blockNumber), BigInteger.valueOf(i)).send().getTransaction();
                    if (transactionOptional.isPresent()) {
                        transactionList.add(this.getTransactionDetailModelFromTransaction(transactionOptional.get(), blockSimpl.getTimestamp(), blockSimpl.getTimestamp()));
                    }
                }
            }

        } catch (InterruptedException | ExecutionException | TimeoutException | IOException e) {
            e.printStackTrace();
        } finally {
            countDownLatch.countDown();
        }
    }

    @Async
    @SuppressWarnings("rawtypes")
    public void getTransactionByBlockNumberAndTxIndex(CountDownLatch countDownLatch, List<Transaction> transactionList, long blockNumber, BigInteger txIndex, BigInteger totalTxCountPara) {
        try {
            if (totalTxCountPara == null) {
                totalTxCountPara = admin.hpbGetBlockTransactionCountByNumber(new DefaultBlockParameterNumber(blockNumber)).send().getTransactionCount();
            }
            if (txIndex.compareTo(totalTxCountPara) <= 0) {
                Optional<Transaction> transactionOptional = admin.hpbGetTransactionByBlockNumberAndIndex(new DefaultBlockParameterNumber(blockNumber), txIndex).send().getTransaction();
                if (transactionOptional.isPresent()) {
                    transactionList.add(transactionOptional.get());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            countDownLatch.countDown();
        }
    }


    @Async
    @SuppressWarnings("rawtypes")
    public void getTransactionDetailModelByBlockNumberAndTxIndex(CountDownLatch countDownLatch, List<TransactionDetailModel> transactionList, long blockNumber, BigInteger txIndex, BigInteger totalTxCountPara, HpbBlock.Block block) {
        try {
            if (txIndex.compareTo(totalTxCountPara) <= 0) {
                Optional<Transaction> transactionOptional = admin.hpbGetTransactionByBlockNumberAndIndex(new DefaultBlockParameterNumber(blockNumber), txIndex).send().getTransaction();
                if (transactionOptional.isPresent()) {
                    transactionList.add(this.getTransactionDetailModelFromTransaction(transactionOptional.get(), block.getTimestamp(), block.getGasLimit()));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            countDownLatch.countDown();
        }
    }

    @Async
    @SuppressWarnings("rawtypes")
    public void getTransactionInfoByNumberAndAddressFromNode(
            CountDownLatch countDownLatch,
            List<Transaction> transactionList,
            long blockNumber,
            String address
    ) {
        try {
            HpbBlock hpbBlock = admin.hpbGetBlockByNumber(new DefaultBlockParameterNumber(blockNumber), true).sendAsync().get(TIME_OUT, TimeUnit.MINUTES);
            Block block = hpbBlock.getBlock();
            List<TransactionResult> transactions = block.getTransactions();
            if (CollectionUtils.isNotEmpty(transactions)) {
                transactions.stream().forEach(transactionResult -> {
                    Transaction transaction = (Transaction) transactionResult.get();
                    if (address.equals(transaction.getFrom()) || address.equals(transaction.getTo())) {
                        transactionList.add(transaction);
                    }
                });
            }

        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            e.printStackTrace();
        } finally {
            countDownLatch.countDown();
        }
    }

    @Async
    @SuppressWarnings("rawtypes")
    public void getTransactionInfoByNumberAndFromAddressFromNode(
            CountDownLatch countDownLatch,
            List<Transaction> transactionList,
            long blockNumber,
            String address
    ) {
        try {
            HpbBlock hpbBlock = admin.hpbGetBlockByNumber(new DefaultBlockParameterNumber(blockNumber), true)
                    .sendAsync().get(TIME_OUT, TimeUnit.MINUTES);
            Block block = hpbBlock.getBlock();
            List<TransactionResult> transactions = block.getTransactions();
            if (CollectionUtils.isNotEmpty(transactions)) {
                transactions.stream().forEach(transactionResult -> {
                    Transaction transaction = (Transaction) transactionResult.get();
                    if (address.equals(transaction.getFrom())) {
                        transactionList.add(transaction);
                    }
                });
            }
        } catch (InterruptedException | ExecutionException | TimeoutException e) {

            e.printStackTrace();
        } finally {
            countDownLatch.countDown();
        }
    }

    @Async
    @SuppressWarnings("rawtypes")
    public void getTransactionInfoByNumberAndToAddressFromNode(
            CountDownLatch countDownLatch,
            List<Transaction> transactionList,
            long blockNumber,
            String address
    ) {
        try {
            HpbBlock hpbBlock = admin.hpbGetBlockByNumber(new DefaultBlockParameterNumber(blockNumber), true)
                    .sendAsync().get(TIME_OUT, TimeUnit.MINUTES);
            Block block = hpbBlock.getBlock();
            List<TransactionResult> transactions = block.getTransactions();
            if (CollectionUtils.isNotEmpty(transactions)) {
                transactions.stream().forEach(transactionResult -> {
                    Transaction transaction = (Transaction) transactionResult.get();
                    log.info("transaction.getTo() :" + transaction.getTo());
                    if (address.equals(transaction.getTo())) {
                        transactionList.add(transaction);
                    }
                });
            }
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            log.info("e=====================" + e);
            e.printStackTrace();
        } finally {
            countDownLatch.countDown();
        }
    }


    @Async
    @SuppressWarnings("rawtypes")
    public void getTransactionDetailByTransactionList(CountDownLatch countDownLatch, List<TransactionDetailModel> transactionDetailModelList, Transaction transaction) {
        try {

            HpbBlock hpbBlock = admin.hpbGetBlockByNumber(new DefaultBlockParameterNumber(transaction.getBlockNumber()), true).sendAsync().get(TIME_OUT, TimeUnit.MINUTES);
            Block block = hpbBlock.getBlock();
            BigInteger timeStamp = block.getTimestamp();
            BigInteger gasLimit = block.getGasLimit();
            TransactionDetailModel detailModel = getTransactionDetailModelFromTransaction(transaction, timeStamp, gasLimit);
            transactionDetailModelList.add(detailModel);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            e.printStackTrace();
        } finally {
            countDownLatch.countDown();
        }
    }

    private TransactionDetailModel getTransactionDetailModelFromTransaction(Transaction transaction, BigInteger timeStamp, BigInteger gasLimit) {
        TransactionDetailModel detailModel = new TransactionDetailModel();
        TransactionReceipt receipt = new TransactionReceipt();
        BeanUtils.copyProperties(transaction, detailModel);
        try {
            HpbGetTransactionReceipt receiptResult = admin.hpbGetTransactionReceipt(transaction.getHash()).sendAsync().get(AsyncTask.TIME_OUT, TimeUnit.MINUTES);
            receipt = receiptResult.getResult();
        } catch (Exception e) {
            e.printStackTrace();
        }
        detailModel.setTransactionHash(receipt.getTransactionHash());
        detailModel.setTransactionStatus(receipt.getStatus());
        detailModel.setBlockNumber(transaction.getBlockNumber() + "");
        detailModel.setBlockHash(transaction.getBlockHash());
        if (receipt.getContractAddress() != null) {
            detailModel.setTxType(BcConstant.TX_SMART_CONTRACT_TYPE);
        } else {
            if (StringUtils.isNotEmpty(transaction.getTo())) {
                boolean isContractFlag = checkIsContractAddress(transaction.getTo());
                if (isContractFlag) {
                    detailModel.setTxType(BcConstant.TX_SMART_CONTRACT_TYPE);
                } else {
                    detailModel.setTxType(BcConstant.TX_COMMON_TYPE);
                }
            } else {
                detailModel.setTxType(BcConstant.TX_SMART_CONTRACT_TYPE);
            }


        }
        if (StringUtils.isEmpty(receipt.getTo()) && StringUtils.isNotEmpty(receipt.getContractAddress())) {
            detailModel.setTo(receipt.getContractAddress());
        }
        detailModel.setValue(Numeric.encodeQuantity(transaction.getValue()));
        BigDecimal transactionHpbValue = Convert.fromWei(transaction.getValue() + "", Convert.Unit.HPB);
        detailModel.setValueStr(transactionHpbValue.setScale(8, BigDecimal.ROUND_HALF_EVEN).toPlainString());
        if (gasLimit != null) {
            detailModel.setGasLimit(String.valueOf(gasLimit));
        }
        detailModel.setGasPrice(transaction.getGasPrice() + "");
        detailModel.setGas(Numeric.encodeQuantity(transaction.getGas()));
        detailModel.setGasUsed(receipt.getGasUsed() + "");
        BigInteger gasSpent = receipt.getGasUsed().multiply(transaction.getGasPrice());
        BigDecimal gasSpentHpbValue = Convert.fromWei(String.valueOf(gasSpent), Convert.Unit.HPB);
        detailModel.setGasSpent(gasSpentHpbValue.setScale(8, BigDecimal.ROUND_HALF_EVEN).toPlainString());
        detailModel.setNonce(Numeric.encodeQuantity(transaction.getNonce()) + "");
        detailModel.setFrom(transaction.getFrom());
        if (StringUtils.isNotEmpty(transaction.getTo())) {
            detailModel.setTo(transaction.getTo());
        }
        detailModel.setInput(transaction.getInput());
        detailModel.setR(transaction.getR());
        detailModel.setS(transaction.getS());
        detailModel.setV(transaction.getV());
        if (timeStamp != null) {
            detailModel.setTransactionTimestamp(String.valueOf(timeStamp));
        }

        detailModel.setTransactionIndex(Numeric.encodeQuantity(transaction.getTransactionIndex()));
        detailModel.setLogs(receipt.getLogs());
        return detailModel;
    }

    private boolean checkIsContractAddress(String address) {
        try {
            HpbGetCode hpbGetCode = admin.hpbGetCode(address, DefaultBlockParameterName.LATEST).sendAsync().get(2, TimeUnit.MINUTES);
            if (hpbGetCode.getCode() != null && !BcConstant.HX_PREFIX.equalsIgnoreCase(hpbGetCode.getCode())) {
                return true;
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Async
    @SuppressWarnings("rawtypes")
    public void getTransactionDetailModelWithGasSpentByTransactionList(CountDownLatch countDownLatch, List<TransactionDetailModel> transactionDetailModelList, Transaction transaction) {
        try {
            TransactionReceipt receipt = new TransactionReceipt();

            TransactionDetailModel transactionDetailModel = new TransactionDetailModel();
            BeanUtils.copyProperties(transaction, transactionDetailModel);
            HpbGetTransactionReceipt receiptResult = admin.hpbGetTransactionReceipt(transaction.getHash()).sendAsync().get(AsyncTask.TIME_OUT, TimeUnit.MINUTES);
            receipt = receiptResult.getResult();
            transactionDetailModel.setGasUsed(String.valueOf(receipt.getGasUsed()));
            transactionDetailModel.setGasSpent(String.valueOf(receipt.getGasUsed().multiply(transaction.getGasPrice())));
            transactionDetailModelList.add(transactionDetailModel);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            countDownLatch.countDown();
        }
    }

    @Async
    public void getPageTransactionDetailModelByBlockNumberAndTransactionIndex(CountDownLatch countDownLatch, List<TransactionDetailModel> transactionDetailModelList, BlockInfo blockInfo, BigInteger txIndex) {

        try {
            HpbTransaction hpbTransaction = admin.hpbGetTransactionByBlockNumberAndIndex(new DefaultBlockParameterNumber(blockInfo.getNumber()), txIndex).sendAsync().get(TIME_OUT, TimeUnit.MINUTES);
            if (hpbTransaction != null && hpbTransaction.getTransaction() != null) {
                Transaction transaction = hpbTransaction.getTransaction().get();
                if (transaction != null) {
                    TransactionDetailModel transactionDetailModel;
                    if (blockInfo != null) {
                        transactionDetailModel = this.getTransactionDetailModelFromTransaction(transaction, blockInfo.getTimestamp(), blockInfo.getGasLimit());
                    } else {
                        transactionDetailModel = this.getTransactionDetailModelFromTransaction(transaction, null, null);
                    }

                    transactionDetailModelList.add(transactionDetailModel);
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        } finally {
            countDownLatch.countDown();
        }
    }

    //
    @Async
    public void getPageTransactionDetailModelByBlockHashAndTransactionIndex(CountDownLatch countDownLatch, List<TransactionDetailModel> transactionDetailModelList, String hash, BlockInfo blockInfo, BigInteger txIndex) {
        try {
            HpbTransaction hpbTransaction = admin.hpbGetTransactionByBlockHashAndIndex(hash, txIndex).sendAsync().get(TIME_OUT, TimeUnit.MINUTES);
            if (hpbTransaction != null && hpbTransaction.getTransaction() != null) {
                Transaction transaction = hpbTransaction.getTransaction().get();
                if (transaction != null) {
                    TransactionDetailModel transactionDetailModel;
                    if (blockInfo != null) {
                        transactionDetailModel = this.getTransactionDetailModelFromTransaction(transaction, blockInfo.getTimestamp(), blockInfo.getGasLimit());
                    } else {
                        transactionDetailModel = this.getTransactionDetailModelFromTransaction(transaction, null, null);
                    }
                    transactionDetailModelList.add(transactionDetailModel);
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        } finally {
            countDownLatch.countDown();
        }
    }

    @Async
    public void getPageTransactionDetailModelByBlockHashAndTransactionIndexAndTxType(CountDownLatch countDownLatch, List<TransactionDetailModel> contractTransactionDetailModelList, List<TransactionDetailModel> commonTransactionDetailModelList, String hash, BlockInfo blockInfo, BigInteger txIndex) {
        try {
            HpbTransaction hpbTransaction = admin.hpbGetTransactionByBlockHashAndIndex(hash, txIndex).sendAsync().get(TIME_OUT, TimeUnit.MINUTES);
            if (hpbTransaction != null && hpbTransaction.getTransaction() != null) {
                Transaction transaction = hpbTransaction.getTransaction().get();
                if (transaction != null) {
                    TransactionDetailModel transactionDetailModel;
                    if (blockInfo != null) {
                        transactionDetailModel = this.getTransactionDetailModelFromTransaction(transaction, blockInfo.getTimestamp(), blockInfo.getGasLimit());
                    } else {
                        transactionDetailModel = this.getTransactionDetailModelFromTransaction(transaction, null, null);
                    }
                    if (BcConstant.TX_SMART_CONTRACT_TYPE.equals(transactionDetailModel.getTxType())) {
                        contractTransactionDetailModelList.add(transactionDetailModel);
                    } else {
                        commonTransactionDetailModelList.add(transactionDetailModel);
                    }
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        } finally {
            countDownLatch.countDown();
        }
    }


    @Async
    public void queryBlockGasDetailInfoByTransactionResult(CountDownLatch countDownLatch, List<BigInteger> totalGasList, List<BigInteger> totalGasSpentList,
                                                           List<BigInteger> contractCountList, List<BigInteger> transCountList, HpbBlock.TransactionResult transactionResult) {
        String hash2 = transactionResult.get().toString();
        TransactionReceipt transactionReceipt = null;
        try {
            Optional<Transaction> transactionOptional = admin.hpbGetTransactionByHash(transactionResult.get().toString()).sendAsync().get(TIME_OUT, TimeUnit.MINUTES).getTransaction();
            transactionReceipt = admin.hpbGetTransactionReceipt(hash2).send().getResult();
            String contractAddress = transactionReceipt.getContractAddress();
            if (!StringUtils.isEmpty(contractAddress)) {
                contractCountList.add(BigInteger.ONE);
            } else {
                transCountList.add(BigInteger.ONE);
            }
            if (transactionOptional.isPresent()) {
                Transaction transaction = transactionOptional.get();
                totalGasList.add(transaction.getGas());
                totalGasSpentList.add(transactionReceipt.getGasUsed().multiply(transaction.getGasPrice()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            countDownLatch.countDown();
        }
    }

}
