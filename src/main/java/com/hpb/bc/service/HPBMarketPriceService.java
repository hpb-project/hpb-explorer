package com.hpb.bc.service;


import com.hpb.bc.entity.HpbMarketPrice;
import com.hpb.bc.model.HpbMarketPriceModel;

import java.util.List;
import java.util.Map;

public interface HPBMarketPriceService extends BaseService {

    void addNewPrice(HpbMarketPrice record);

    Map<String, Object> queryEchartData();

    /**
     * 返回新式接口，改版的浏览器在使用；
     */
    List<HpbMarketPriceModel> queryNewEchartData();
}
