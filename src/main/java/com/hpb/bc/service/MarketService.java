package com.hpb.bc.service;

import java.math.BigDecimal;
import java.util.Map;

public interface MarketService {
    BigDecimal getTotalSupply();

    Map<String, Object> getIcoPrice();

}
