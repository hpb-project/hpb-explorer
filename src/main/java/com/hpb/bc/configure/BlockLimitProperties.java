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
