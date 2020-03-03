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

import com.hpb.bc.configure.RedisExpireTimeProperties;
import com.hpb.bc.constant.RedisKeyConstant;
import com.hpb.bc.entity.BlockBigRecord;
import com.hpb.bc.entity.BlockInfo;
import com.hpb.bc.example.BlockBigRecordExample;
import com.hpb.bc.mapper.AddrsMapper;
import com.hpb.bc.mapper.BlockBigRecordMapper;
import com.hpb.bc.service.BlockFacetService;
import com.hpb.bc.service.RedisService;
import com.hpb.bc.util.GsonUtil;
import com.hpb.bc.util.RedisUtil;
import io.hpb.web3.protocol.admin.Admin;
import io.hpb.web3.protocol.core.DefaultBlockParameterNumber;
import io.hpb.web3.protocol.core.methods.response.HpbBlock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Service
public class BlockFacetServiceImpl implements BlockFacetService {

    @Autowired
    RedisService redisService;

    @Autowired
    AddrsMapper addrsMapper;

    @Autowired
    RedisExpireTimeProperties redisExpireTimeProperties;

    @Autowired
    Admin admin;

    @Autowired
    RedisUtil redisUtil;

    @Autowired
    BlockBigRecordMapper blockBigRecordMapper;


    @Override
    public boolean isContractAddress(String contractAddress) {
        List<String> contractAddressList = (List<String>) redisService.getObject(RedisKeyConstant.ContractAddressList);
        if (contractAddressList == null) {
            contractAddressList = addrsMapper.selectContractAddressByAccountType(Integer.valueOf(1));
            redisService.saveWithExpireTime(RedisKeyConstant.ContractAddressList, contractAddressList, redisExpireTimeProperties.getMinutesNumber(), TimeUnit.MINUTES);
            if (contractAddressList.contains(contractAddress)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public BlockInfo getBlockInfoByBlockNumberByMapFromRedis(long blockNumber) {
        BlockInfo blockInfo = null;
        Map<Object, Object> infoRedis = redisUtil.hmget(RedisKeyConstant.BLOCK_INFO_MAP_PREFIX + String.valueOf(blockNumber));
        if (infoRedis != null && !infoRedis.isEmpty()) {
            Object key = RedisKeyConstant.BLOCK_INFO_MAP_PREFIX + String.valueOf(blockNumber);
            blockInfo = (BlockInfo) infoRedis.get(key);
            return blockInfo;
        }
        blockInfo = getBlockInfoByBlockNumberFromDb(blockNumber);
        if (blockInfo != null) {
            return blockInfo;
        }
        blockInfo = getBlockInfoByBlockNumberFromChain(blockNumber);
        return blockInfo;
    }

    private BlockInfo getBlockInfoByBlockNumberFromChain(long blockNumber) {
        try {
            HpbBlock hpbBlock = admin.hpbGetBlockByNumber(new DefaultBlockParameterNumber(blockNumber), false).sendAsync().get(2, TimeUnit.MINUTES);
            HpbBlock.Block block = hpbBlock.getBlock();
            BlockInfo blockInfo = new BlockInfo(block, admin);
            if (block != null && block.getTransactions().size() > redisExpireTimeProperties.getRedisMinLimit()) {
                Map<String, Object> map = new HashMap<>();
                String key = RedisKeyConstant.BLOCK_INFO_MAP_PREFIX + String.valueOf(blockNumber);
                map.put(key, blockInfo);
                redisUtil.hmset(key, map, redisExpireTimeProperties.getExpireSecondAmount());
            }
            return blockInfo;
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
        return null;
    }


    private BlockInfo getBlockInfoByBlockHashFromChain(String hash) {
        try {
            HpbBlock hpbBlock = admin.hpbGetBlockByHash(hash, false).sendAsync().get(2, TimeUnit.MINUTES);
            HpbBlock.Block block = hpbBlock.getBlock();
            BlockInfo blockInfo = new BlockInfo(block, admin);
            if (block != null && block.getTransactions().size() > redisExpireTimeProperties.getRedisMinLimit()) {
                Map<String, Object> map = new HashMap<>();
                String key = RedisKeyConstant.BLOCK_INFO_MAP_PREFIX + String.valueOf(hash);
                map.put(key, blockInfo);
                redisUtil.hmset(key, map, redisExpireTimeProperties.getExpireSecondAmount());
            }
            return blockInfo;
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
        return null;
    }

    private BlockInfo getBlockInfoByBlockNumberFromDb(long blockNumber) {
        BlockBigRecordExample blockBigRecordExample = new BlockBigRecordExample();
        blockBigRecordExample.createCriteria().andBlockNumberEqualTo(blockNumber);
        BlockBigRecord blockBigRecord = blockBigRecordMapper.selectBlockBigRecordByBlockNumber(blockNumber);
        if (blockBigRecord != null) {
            BlockInfo blockInfo = GsonUtil.gsonToBean(blockBigRecord.getBlockInfoJsonStr(), BlockInfo.class);
            if (blockInfo != null && blockInfo.getNumber() != null) {
                return blockInfo;
            }
        }
        return null;
    }


    private BlockInfo getBlockInfoByBlockHashFromDb(String blockHash) {
        BlockBigRecordExample blockBigRecordExample = new BlockBigRecordExample();
        blockBigRecordExample.createCriteria().andBlockHashEqualTo(blockHash);
        BlockBigRecord blockBigRecord = blockBigRecordMapper.selectBlockBigRecordByBlockHash(blockHash);
        if (blockBigRecord != null) {
            BlockInfo blockInfo = GsonUtil.gsonToBean(blockBigRecord.getBlockInfoJsonStr(), BlockInfo.class);
            if (blockInfo != null && blockInfo.getNumber() != null) {
                return blockInfo;
            }
        }
        return null;
    }


    @Override
    public BlockInfo getBlockInfoByBlockHashFromRedis(String hash) {
        BlockInfo blockInfo = null;
        String key = RedisKeyConstant.BLOCK_INFO_MAP_PREFIX + hash;
        Map<Object, Object> infoRedis = redisUtil.hmget(key);
        if (infoRedis != null && !infoRedis.isEmpty()) {
            blockInfo = (BlockInfo) infoRedis.get(key);
            return blockInfo;
        }
        blockInfo = getBlockInfoByBlockHashFromDb(hash);
        if (blockInfo != null) {
            return blockInfo;
        }
        blockInfo = this.getBlockInfoByBlockHashFromChain(hash);
        return blockInfo;
    }


}
