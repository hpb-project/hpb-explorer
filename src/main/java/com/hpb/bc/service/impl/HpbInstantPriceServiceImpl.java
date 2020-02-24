package com.hpb.bc.service.impl;

import com.hpb.bc.entity.HpbInstantPrice;
import com.hpb.bc.mapper.HpbInstantPriceMapper;
import com.hpb.bc.service.HpbInstantPriceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HpbInstantPriceServiceImpl implements HpbInstantPriceService {

    @Autowired
    HpbInstantPriceMapper hpbInstantPriceMapper;

    @Override
    public HpbInstantPrice getHpbInstantPriceById(Integer id) {
        return hpbInstantPriceMapper.selectByPrimaryKey(id);
    }
}
