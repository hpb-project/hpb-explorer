package com.hpb.bc.service;


import com.hpb.bc.entity.BlockMaxSize;

import java.math.BigDecimal;

/***
 * @author will
 *
 * 最大区块大小
 */
public interface BlockMaxSizeService {

    /**
     * @param id id;
     * @return BlockMaxSize;
     */
    BlockMaxSize getBlockMaxSizeById(Integer id);

}
