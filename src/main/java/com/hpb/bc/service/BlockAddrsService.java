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
