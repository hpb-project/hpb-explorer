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
