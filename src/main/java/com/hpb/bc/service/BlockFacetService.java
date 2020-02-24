package com.hpb.bc.service;

import com.hpb.bc.entity.BlockInfo;

public interface BlockFacetService {

    boolean isContractAddress(String address);

    BlockInfo getBlockInfoByBlockNumberByMapFromRedis(long blockNumber);

    BlockInfo getBlockInfoByBlockHashFromRedis(String hash);
}
