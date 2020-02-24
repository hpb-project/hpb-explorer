package com.hpb.bc.controller;

import com.github.pagehelper.PageInfo;
import com.hpb.bc.constant.BlockConstant;
import com.hpb.bc.entity.BlockInfo;
import com.hpb.bc.entity.HpbInstantPrice;
import com.hpb.bc.entity.result.Result;
import com.hpb.bc.entity.result.ResultCode;
import com.hpb.bc.model.HpbMarketPriceModel;
import com.hpb.bc.service.BlockService;
import com.hpb.bc.service.HPBMarketPriceService;
import com.hpb.bc.service.HpbInstantPriceService;
import com.hpb.bc.util.AddressBalanceHelper;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/price")
public class HPBMarketController extends BaseController {
    @Autowired
    private HPBMarketPriceService hPBMarketPriceService;

    @Autowired
    HpbInstantPriceService hpbInstantPriceService;

    @Autowired
    AddressBalanceHelper addressBalanceHelper;

    @ApiOperation(value = "获取区块信息最近一周价格", notes = "获取区块信息最近一周价格")
    @PostMapping("/getEchartData")
    public List<Object> getEchartData(HttpServletRequest request, HttpServletResponse response) throws Exception {

        Map<String, Object> map = hPBMarketPriceService.queryEchartData();

        HpbInstantPrice hpbInstantPrice = hpbInstantPriceService.getHpbInstantPriceById(Integer.valueOf(1));
        map.put("hpbInstantPrice", hpbInstantPrice);

        Result<Map<String, Object>> result = new Result<>(ResultCode.SUCCESS, map);
        return result.getValue();
    }


}