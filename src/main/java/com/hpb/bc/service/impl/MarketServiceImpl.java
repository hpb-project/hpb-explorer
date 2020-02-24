package com.hpb.bc.service.impl;

import com.hpb.bc.configure.MarketProperties;
import com.hpb.bc.service.MarketService;
import com.hpb.bc.util.AddressBalanceHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Service
public class MarketServiceImpl implements MarketService {

    @Autowired
    AddressBalanceHelper addressBalanceHelper;

    @Autowired
    MarketProperties marketProperties;

    @Override
    public BigDecimal getTotalSupply() {
        return addressBalanceHelper.getTotalSupply();
    }


    @Override
    public Map<String, Object> getIcoPrice() {
        Map<String, Object> map = new HashMap<>();
        String usdPrice;
        String cnyPrice;
        usdPrice = marketProperties.getUsdPrice();
        cnyPrice = marketProperties.getCnyPrice();
        map.put("usdPrice", usdPrice);
        map.put("cnyPrice", cnyPrice);
        return map;
    }
}
