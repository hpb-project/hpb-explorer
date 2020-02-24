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
