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
