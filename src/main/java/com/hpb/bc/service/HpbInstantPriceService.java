package com.hpb.bc.service;


import com.hpb.bc.entity.HpbInstantPrice;

public interface HpbInstantPriceService extends BaseService {
    /**
     * 获取当前价格
     *
     * @param id 查询参数
     */
    HpbInstantPrice getHpbInstantPriceById(Integer id);

}
