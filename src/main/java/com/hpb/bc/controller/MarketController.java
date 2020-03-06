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

import com.hpb.bc.service.MarketService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.Map;

@RestController
@RequestMapping("/market")
public class MarketController extends BaseController {

    @Autowired
    MarketService marketService;

    @ApiOperation(value = "获取总供给量", notes = "无参")
    @GetMapping("/totalSupply")
    public BigDecimal getTotalSupply(HttpServletRequest request, HttpServletResponse response) throws Exception {
        BigDecimal total = marketService.getTotalSupply();
        return total;
    }


    @ApiOperation(value = "获取ICO价格", notes = "无参")
    @GetMapping("/ico/price")
    public Map<String, Object> getIcoPrice(HttpServletRequest request, HttpServletResponse response) throws Exception {
        return marketService.getIcoPrice();
    }

}