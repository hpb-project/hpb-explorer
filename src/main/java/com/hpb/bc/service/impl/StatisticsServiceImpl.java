package com.hpb.bc.service.impl;

import com.hpb.bc.entity.StatTransactionAmountDaily;
import com.hpb.bc.mapper.StatTransactionAmountDailyMapper;
import com.hpb.bc.service.StatisticsService;
import com.hpb.bc.util.DateUtils;
import com.hpb.bc.util.HttpUtil;
import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Service
public class StatisticsServiceImpl implements StatisticsService {
    private static final String PRICE_LAST = "last";
    private static final String PRICE_LAST_BTC = "last_btc";
    private static final String PRICE_LAST_CNY = "last_cny";
    private static final String PRICE_LAST_USD = "last_usd";

    @Value("${hpb.price.bibox.uri}")
    private String biboxUri;

    @Autowired
    StatTransactionAmountDailyMapper statTransactionAmountDailyMapper;

    @Override
    public Map<String, String> getInstantPrice() {
        Map<String, Object> map = HttpUtil.getHpbPriceInfoOfBibox(biboxUri);
        Map<String, String> mapStr = new TreeMap<>();
        mapStr.put(PRICE_LAST_BTC, MapUtils.getString(map, PRICE_LAST));
        mapStr.put(PRICE_LAST_USD, MapUtils.getString(map, PRICE_LAST_USD));
        mapStr.put(PRICE_LAST_CNY, MapUtils.getString(map, PRICE_LAST_CNY));
        return mapStr;
    }


    @Override
    public List<StatTransactionAmountDaily> selectLastDaysTransactionAmountByDayNumber(int days) {
        Date endDateTime = DateUtils.getNowDate();
        if (days == 0) {
            days = 14;
        }
        Date startDateTime = DateUtils.addDays(endDateTime, -days);
        List<StatTransactionAmountDaily> objectList = statTransactionAmountDailyMapper.selectStatTransactionAmountDailyBetweenStartTimeAndEndTime(startDateTime, endDateTime);
        return objectList;
    }
}
