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

package com.hpb.bc.service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.hpb.bc.entity.result.Result;
import com.hpb.bc.model.TransactionDetailModel;
import com.hpb.bc.model.TransactionQueryModel;
import io.hpb.web3.protocol.core.methods.response.Transaction;
import io.hpb.web3.protocol.core.methods.response.TransactionReceipt;

public interface TransactionService {

    Result<TransactionReceipt> getTransactionByHash(String blockHash);


    Result<TransactionDetailModel> getTransactionDetailModelByHash(String blockHash);


    Result<PageInfo<TransactionDetailModel>> getPageTransactionList(String blockNumber, String currentPage, String pageSize);


    /**
     * 查询总交易数目
     */
    Integer getTotalTransaction();


    /**
     * 查询交易前n笔
     *
     * @param topNNum
     * @return 分页结果
     */
    Result<List<TransactionDetailModel>> getLatestTopNTransactionList(long topNNum);

    /**
     * 根据打款账号查询交易信息，并分页
     *
     * @param blockNumber 区块号；
     * @param address     账号地址
     * @param pageNum     当夜页面号
     * @param pageSize
     * @return 分页结果
     */

    Result<PageInfo<TransactionDetailModel>> getPageTransactionListByFromAccount(Long blockNumber, String address, Long pageNum, Long pageSize);

    /**
     * 根据打款账号查询交易信息，并分页
     *
     * @param blockNumber 区块号；
     * @param address     账号地址
     * @param pageNum     当夜页面号
     * @param pageSize
     * @return 分页结果
     */

    Result<PageInfo<TransactionDetailModel>> getPageTransactionListByToAccount(Long blockNumber, String address, Long pageNum, Long pageSize);


    /**
     * 根据打款账号查询交易信息(打款，收款)，并分页
     *
     * @param blockNumber 区块号；
     * @param address     账号地址
     * @param pageNum     当夜页面号
     * @param pageSize
     * @return 分页结果
     */

    Result<PageInfo<TransactionDetailModel>> getPageTransactionListByAccount(Long blockNumber, String address, Long pageNum, Long pageSize);


    Result<Object> getPageTransactionByBlockNumber(Long blockNumber, Long pageNum, Long pageSize);


    Result<Object> getPageTransactionByBlockHash(String hash, Long pageNum, Long pageSize);

    Result<PageInfo<TransactionDetailModel>> getPageTransactionListByCurrentBlockNumberOfLastTransaction(String currentPage, String pageSize, String blockNumber, String currentBlockNumberOfLastTransaction, String upDown);

    /**
     * 查询打款到address交易总数目；
     *
     * @param address
     */
    BigDecimal getTransactionAmountByToAccount(String address, BigInteger blockNumber);

    /**
     * 查询address打款的交易总数目；
     *
     * @param address
     */
    BigDecimal getTransactionAmountByFromAccount(String address, BigInteger blockNumber);


    Result<PageInfo<TransactionDetailModel>> getPageTransactionListByAccountOfLastTransaction(Long blockNumber, String address, Integer currentPage, Integer pageSize, String txHash, String pageFlag);


    Result<Object> getPageTransactionByBlockHashAndTxType(TransactionQueryModel model);

}
