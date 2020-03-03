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

import java.util.List;

import com.github.pagehelper.PageInfo;
import com.hpb.bc.entity.Addrs;
import com.hpb.bc.entity.BlockAddrs;

/**
 * 查询BlockAddrs
 *
 * @author zihui
 */
public interface BlockAddrsService {

    /**
     * 获取区块号信息
     *
     * @param blockNumber 区块号
     * @param pageNum     页号
     * @param pageSize    每页数目
     */

    PageInfo<BlockAddrs> getPageBlockAddrsLessThanBlockNumberByExample(Long blockNumber, int pageNum, int pageSize);

    /**
     * 获取区块号以下区块每页交易总数
     *
     * @param blockNumber 区块号
     * @param pageNum     页号
     * @param pageSize    每页数目
     */
    Integer selectSumTxCountPerPage(Long blockNumber, Integer pageNum, Integer pageSize);

    /**
     * 获取区块号以下区块每页交易总数
     *
     * @param addrs      账号信息
     * @param underBlock 区块以下
     * @return 查询结果
     */
    List<BlockAddrs> getBlockAddrsListFromStartBlockToEndBlockByAddrs(Addrs addrs, Long underBlock);


    /**
     * 获取区块见交易总数，闭区间，包括该区块
     *
     * @param startBlockNum 开始区块
     * @param endBlockNum   结束区块
     * @return 查询结果
     */
    List<BlockAddrs> getBlockAddrsListBetweenStartBlockAndEndBlock(Long startBlockNum, Long endBlockNum);



}
