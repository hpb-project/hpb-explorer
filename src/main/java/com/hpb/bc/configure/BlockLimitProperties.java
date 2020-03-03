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

import static com.hpb.bc.configure.BlockLimitProperties.BLOCK_PREFIX;

/**
 * web3 property container.
 */
@Component
@ConfigurationProperties(prefix = BLOCK_PREFIX)
public class BlockLimitProperties {

    public static final String BLOCK_PREFIX = "block";

    public int averagePriceTxMinTxCount;

    private int elasticPageSize;

    private int elasticStepCountDownLatchSize;

    private int notSelectBlockTimeMiniAmount;

    private boolean ifSelectBiggerBlock;

    public int getAveragePriceTxMinTxCount() {
        return averagePriceTxMinTxCount;
    }


    public void setAveragePriceTxMinTxCount(int averagePriceTxMinTxCount) {
        this.averagePriceTxMinTxCount = averagePriceTxMinTxCount;
    }

    public int getElasticPageSize() {
        return elasticPageSize;
    }

    public void setElasticPageSize(int elasticPageSize) {
        this.elasticPageSize = elasticPageSize;
    }

    public int getElasticStepCountDownLatchSize() {
        return elasticStepCountDownLatchSize;
    }

    public void setElasticStepCountDownLatchSize(int elasticStepCountDownLatchSize) {
        this.elasticStepCountDownLatchSize = elasticStepCountDownLatchSize;
    }

    public int getNotSelectBlockTimeMiniAmount() {
        return notSelectBlockTimeMiniAmount;
    }

    public void setNotSelectBlockTimeMiniAmount(int notSelectBlockTimeMiniAmount) {
        this.notSelectBlockTimeMiniAmount = notSelectBlockTimeMiniAmount;
    }

    public boolean isIfSelectBiggerBlock() {
        return ifSelectBiggerBlock;
    }

    public void setIfSelectBiggerBlock(boolean ifSelectBiggerBlock) {
        this.ifSelectBiggerBlock = ifSelectBiggerBlock;
    }
}
