package com.hpb.bc.configure;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

import static com.hpb.bc.configure.HpbContractValidateProperties.HPB_CONTRACT_PREFIX;

/**
 * web3 property container.
 */
@Component
@ConfigurationProperties(prefix = HPB_CONTRACT_PREFIX)
public class HpbContractValidateProperties {

    public static final String HPB_CONTRACT_PREFIX = "hpb.contract";
    private String path;

    private String validateServiceUrl ;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getValidateServiceUrl() {
        return validateServiceUrl;
    }

    public void setValidateServiceUrl(String validateServiceUrl) {
        this.validateServiceUrl = validateServiceUrl;
    }
}
