package com.hpb.bc.configure;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import static com.hpb.bc.configure.MarketProperties.MARKET_PREFIX;

/**
 * web3 property container.
 */
@Component
@ConfigurationProperties(prefix = MARKET_PREFIX)
public class MarketProperties {

    public static final String MARKET_PREFIX = "market";

    private String usdPrice;

    private String cnyPrice;

    public String getUsdPrice() {
        return usdPrice;
    }

    public void setUsdPrice(String usdPrice) {
        this.usdPrice = usdPrice;
    }

    public String getCnyPrice() {
        return cnyPrice;
    }

    public void setCnyPrice(String cnyPrice) {
        this.cnyPrice = cnyPrice;
    }

}
