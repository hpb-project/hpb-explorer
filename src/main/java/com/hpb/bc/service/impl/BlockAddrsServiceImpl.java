package com.hpb.bc.service.impl;

import java.math.BigInteger;
import java.util.List;

import io.hpb.web3.utils.Numeric;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hpb.bc.entity.Addrs;
import com.hpb.bc.entity.BlockAddrs;
import com.hpb.bc.example.BlockAddrsExample;
import com.hpb.bc.mapper.BlockAddrsMapper;
import com.hpb.bc.service.BlockAddrsService;

@Service
public class BlockAddrsServiceImpl implements BlockAddrsService {

    @Autowired
    BlockAddrsMapper blockAddrsMapper;

    public Logger log = LoggerFactory.getLogger(this.getClass());

    @Override
    public PageInfo<BlockAddrs> getPageBlockAddrsLessThanBlockNumberByExample(Long blockNumber, int pageNum, int pageSize) {

        PageHelper.startPage(pageNum, pageSize);
        BlockAddrsExample blockAddrsExample = new BlockAddrsExample();
        blockAddrsExample.createCriteria().andBNumberLessThanOrEqualTo(blockNumber);
        blockAddrsExample.setOrderByClause(" b_number desc");
        BlockAddrs blockAddrs = new BlockAddrs();
        blockAddrs.setbNumber(blockNumber);
        List<BlockAddrs> list = blockAddrsMapper.selectPageBlockAddrsUnderBlockNumber(blockAddrs);
        PageInfo<BlockAddrs> pageInfo = new PageInfo<BlockAddrs>(list);
        return pageInfo;
    }

    @Override
    public Integer selectSumTxCountPerPage(Long blockNumber, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        BlockAddrsExample blockAddrsExample = new BlockAddrsExample();
        blockAddrsExample.createCriteria().andBNumberLessThanOrEqualTo(blockNumber);
        blockAddrsExample.setOrderByClause(" b_number desc");
        blockAddrsExample.setFechNum(null);
        BlockAddrs blockAddrsPara = new BlockAddrs();
        blockAddrsPara.setbNumber(blockNumber);
        List<BlockAddrs> list = blockAddrsMapper.selectPageBlockAddrsUnderBlockNumber(blockAddrsPara);

        PageInfo<BlockAddrs> pageInfo = new PageInfo<BlockAddrs>(list);

        List<BlockAddrs> pageList = pageInfo.getList();
        Integer result = 0;
        for (BlockAddrs blockAddrs : pageList) {
            result += blockAddrs.getTxcount();
        }
        log.info(" blockNumber： {} 以下，第 {} 页面，有{} 笔交易", blockNumber, pageNum, pageSize);
        return result;
    }


    @Override
    public List<BlockAddrs> getBlockAddrsListFromStartBlockToEndBlockByAddrs(Addrs addrs, Long underBlock) {
        return blockAddrsMapper.selectBlockAddrsListFromStartBlockToEndBlock(addrs, underBlock);
    }

    @Override
    public List<BlockAddrs> getBlockAddrsListBetweenStartBlockAndEndBlock(Long startBlockNum, Long endBlockNum) {
        return blockAddrsMapper.selectBlockAddrsListBetweenStartBlockAndEndBlock(null, startBlockNum, endBlockNum);
    }

}
