package com.hpb.bc.service;

import com.hpb.bc.entity.StatTransactionAmountDaily;

import java.util.List;
import java.util.Map;


public interface StatisticsService {
    Map<String, String> getInstantPrice();

    /***
     * @param  days 查询日期；
     */
    List<StatTransactionAmountDaily> selectLastDaysTransactionAmountByDayNumber(int days);
}
