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

package com.hpb.bc.model;

import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

/**
 * @author LiXing
 * @version v1.0
 * date 2019/8/6 11:53
 **/
public class ContractErcProfileSummaryModel extends BaseModel {

    private String form;

    private String sign;
    // 审批标志；
    private String approveFlag;

    public String getForm() {
        return form;
    }

    public void setForm(String form) {
        this.form = form;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public static class ContractErcProfileSummaryInner {
        private String id;
        private String tokenSymbolImageUrl;
        private String tokenName;
        private String contractAddress;
        private String contractType;
        private String profileZh;
        private String profileEn;
        private String officialSite;
        private String email;
        private String facebook;
        private String twitter;
        private String weibo;
        private String github;
        private String telegram;
        private String approveStatus;
        private Date createTime;
        private Date updateTime;

        public String getTelegram() {
            return telegram;
        }

        public void setTelegram(String telegram) {
            this.telegram = telegram;
        }

        public String getProfileZh() {
            return profileZh;
        }

        public void setProfileZh(String profileZh) {
            this.profileZh = profileZh;
        }

        public String getProfileEn() {
            return profileEn;
        }

        public void setProfileEn(String profileEn) {
            this.profileEn = profileEn;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getFacebook() {
            return facebook;
        }

        public void setFacebook(String facebook) {
            this.facebook = facebook;
        }

        public String getTwitter() {
            return twitter;
        }

        public void setTwitter(String twitter) {
            this.twitter = twitter;
        }

        public String getWeibo() {
            return weibo;
        }

        public void setWeibo(String weibo) {
            this.weibo = weibo;
        }

        public String getGithub() {
            return github;
        }

        public void setGithub(String github) {
            this.github = github;
        }

        public Date getCreateTime() {
            return createTime;
        }

        public void setCreateTime(Date createTime) {
            this.createTime = createTime;
        }

        public Date getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(Date updateTime) {
            this.updateTime = updateTime;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getTokenSymbolImageUrl() {
            return tokenSymbolImageUrl;
        }

        public void setTokenSymbolImageUrl(String tokenSymbolImageUrl) {
            this.tokenSymbolImageUrl = tokenSymbolImageUrl;
        }

        public String getTokenName() {
            return tokenName;
        }

        public void setTokenName(String tokenName) {
            this.tokenName = tokenName;
        }

        public String getContractAddress() {
            return contractAddress;
        }

        public void setContractAddress(String contractAddress) {
            this.contractAddress = contractAddress;
        }

        public String getContractType() {
            return contractType;
        }

        public void setContractType(String contractType) {
            this.contractType = contractType;
        }


        public String getOfficialSite() {
            return officialSite;
        }

        public void setOfficialSite(String officialSite) {
            this.officialSite = officialSite;
        }


        public String getApproveStatus() {
            return approveStatus;
        }

        public void setApproveStatus(String approveStatus) {
            this.approveStatus = approveStatus;
        }
    }

    public String getApproveFlag() {
        return approveFlag;
    }

    public void setApproveFlag(String approveFlag) {
        this.approveFlag = approveFlag;
    }
}
