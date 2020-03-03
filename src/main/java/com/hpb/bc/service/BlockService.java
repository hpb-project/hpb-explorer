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

import java.math.BigInteger;
import java.util.List;
import java.util.Map;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import com.hpb.bc.entity.BlockInfo;
import com.hpb.bc.entity.result.Result;

public interface BlockService {

    Result<BlockInfo> getBlockInfoByHash(String blockHash);

    Result<List<BlockInfo>> getLatestBlockInfoList();


    Result<PageInfo<BlockInfo>> selectPageBlockInfoList(String pageNum, String pageSize, String blockNumber);


    Result<BigInteger> getMaxBlockNumber();


    Result<Object> getBlockInfoByBlockNumber(String blockNumber);

    Result<Map<String, Object>> getLatestOneBlockInfo();

    /**
     * 获取区块预览
     */
    Map<String, Object> getBlockOverview();

    /**
     * 获取最大TPS
     */
    List<String> getMaxTps();

    String queryAveragePriceByBlockHash(String blockHash);

}
