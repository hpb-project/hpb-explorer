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