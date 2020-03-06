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

package com.hpb.bc.controller;

import com.hpb.bc.entity.HpbInstantPrice;
import com.hpb.bc.entity.StatTransactionAmountDaily;
import com.hpb.bc.entity.result.Result;
import com.hpb.bc.entity.result.ResultCode;
import com.hpb.bc.service.HpbInstantPriceService;
import com.hpb.bc.service.MarketService;
import com.hpb.bc.service.StatisticsService;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.*;

@RestController
@RequestMapping("/stat")
public class StatisticsController extends BaseController {

    @Autowired
    MarketService marketService;

    @Autowired
    StatisticsService statisticsService;

    @Autowired
    HpbInstantPriceService hpbInstantPriceService;

    @ApiOperation(value = "获取最近2周的每天交易数折线图", notes = "获取最近2周的每天交易数折线图")
    @GetMapping("/transactions/amount")
    public List<Object> list(HttpServletRequest request, HttpServletResponse response) throws Exception {
        List<StatTransactionAmountDaily> objectList = statisticsService.selectLastDaysTransactionAmountByDayNumber(14);
        Result<List<StatTransactionAmountDaily>> result = new Result<List<StatTransactionAmountDaily>>(ResultCode.SUCCESS, objectList);
        return result.getValue();
    }

    //
    @ApiOperation(value = "获取实时价格", notes = "获取实时价格")
    @GetMapping("/instantPrice")
    public List<Object> price(HttpServletRequest request, HttpServletResponse response) throws Exception {
        HpbInstantPrice hpbInstantPrice = hpbInstantPriceService.getHpbInstantPriceById(Integer.valueOf(1));
        Map<String, Object> map = new HashMap<>();
        map.put("hpbInstantPrice", hpbInstantPrice);
        Result<Map<String, Object>> result = new Result<>(ResultCode.SUCCESS, map);
        return result.getValue();
    }


}