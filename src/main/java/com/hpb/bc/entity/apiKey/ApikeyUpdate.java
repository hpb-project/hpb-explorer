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

package com.hpb.bc.entity.apiKey;

import com.fasterxml.jackson.annotation.JsonInclude;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY;

/**
 * Created by luthien on 4/12/2017.
 */

@JsonInclude(NON_EMPTY)
public class ApikeyUpdate implements ApikeyAction {

    public ApikeyUpdate(String apikey,
                        String firstName,
                        String lastName,
                        String email,
                        String appName,
                        String company,
                        String sector,
                        String website) {
        this.apikey = apikey;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.appName = appName;
        this.company = company;
        this.sector = sector;
        this.website = website;
    }

    //empty constructor needed to facilitate integration testing
    public ApikeyUpdate() {
    }

    private String apikey;
    private String firstName;
    private String lastName;
    private String email;
    private String appName;
    private String company;
    private String sector;
    private String website;

    public String getApikey() {
        return apikey;
    }

    @Override
    public String getFirstName() {
        return firstName;
    }

    @Override
    public String getLastName() {
        return lastName;
    }

    @Override
    public String getEmail() {
        return email;
    }

    public String getAppName() {
        return appName;
    }

    public String getCompany() {
        return company;
    }

    public String getSector() {
        return sector;
    }

    public String getWebsite() {
        return website;
    }
}
