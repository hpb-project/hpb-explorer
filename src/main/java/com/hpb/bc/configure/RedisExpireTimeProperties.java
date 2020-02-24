package com.hpb.bc.configure;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import static com.hpb.bc.configure.RedisExpireTimeProperties.REDIS_PREFIX;

/**
 * web3 property container.
 */
@Component
@ConfigurationProperties(prefix = REDIS_PREFIX)
public class RedisExpireTimeProperties {

    public static final String REDIS_PREFIX = "redis";

    private long dayNumber;

    private long hourNumber;

    private long minutesNumber;

    //   redis 最低滑门；
    private int redisMinLimit;
    //  redis  存储最低交易数目；
    private int redisMiniTransactionCount;

    // 过期时间秒数
    private long expireSecondAmount;

    public long getDayNumber() {
        return dayNumber;
    }

    public void setDayNumber(long dayNumber) {
        this.dayNumber = dayNumber;
    }

    public long getHourNumber() {
        return hourNumber;
    }

    public void setHourNumber(long hourNumber) {
        this.hourNumber = hourNumber;
    }

    public long getMinutesNumber() {
        return minutesNumber;
    }

    public void setMinutesNumber(long minutesNumber) {
        this.minutesNumber = minutesNumber;
    }

    public int getRedisMinLimit() {
        return redisMinLimit;
    }

    public void setRedisMinLimit(int redisMinLimit) {
        this.redisMinLimit = redisMinLimit;
    }


    public int getRedisMiniTransactionCount() {
        return redisMiniTransactionCount;
    }

    public void setRedisMiniTransactionCount(int redisMiniTransactionCount) {
        this.redisMiniTransactionCount = redisMiniTransactionCount;
    }

    public long getExpireSecondAmount() {
        return expireSecondAmount;
    }

    public void setExpireSecondAmount(long expireSecondAmount) {
        this.expireSecondAmount = expireSecondAmount;
    }
}
