package com.hpb.bc.service.impl;


import com.hpb.bc.entity.BlockMaxSize;
import com.hpb.bc.mapper.BlockMaxSizeMapper;
import com.hpb.bc.service.BlockMaxSizeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class BlockMaxSizeServiceImpl implements BlockMaxSizeService {

    @Autowired
    BlockMaxSizeMapper blockMaxSizeMapper;

    @Override
    public BlockMaxSize getBlockMaxSizeById(Integer id) {
        return blockMaxSizeMapper.selectByPrimaryKey(id);
    }

}
