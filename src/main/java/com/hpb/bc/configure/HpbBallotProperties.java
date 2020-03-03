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

import static com.hpb.bc.configure.HpbBallotProperties.BALLOT_PREFIX;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * web3 property container.
 */
@Component
@ConfigurationProperties(prefix = BALLOT_PREFIX)
public class HpbBallotProperties {

    public static final String BALLOT_PREFIX = "ballot";

    private String password = null;
    private String keyPath = null;
    private String hpbBallotContractAddress = null;
    private String innerContractAddress = null;

    private List<String> contractList = new ArrayList<>();

    private List<String> campaignPeriodContractList;

    private String sampleContract;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getKeyPath() {
        return keyPath;
    }

    public void setKeyPath(String keyPath) {
        this.keyPath = keyPath;
    }

    public String getHpbBallotContractAddress() {
        return hpbBallotContractAddress;
    }

    public void setHpbBallotContractAddress(String hpbBallotContractAddress) {
        this.hpbBallotContractAddress = hpbBallotContractAddress;
    }

    public String getInnerContractAddress() {
        return innerContractAddress;
    }

    public void setInnerContractAddress(String innerContractAddress) {
        this.innerContractAddress = innerContractAddress;
    }

    public List<String> getContractList() {
        return contractList;
    }

    public void setContractList(List<String> contractList) {
        this.contractList = contractList;
    }

    public List<String> getCampaignPeriodContractList() {
        return campaignPeriodContractList;
    }

    public void setCampaignPeriodContractList(List<String> campaignPeriodContractList) {
        this.campaignPeriodContractList = campaignPeriodContractList;
    }

    public String getSampleContract() {
        return sampleContract;
    }

    public void setSampleContract(String sampleContract) {
        this.sampleContract = sampleContract;
    }
}
