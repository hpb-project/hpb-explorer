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
