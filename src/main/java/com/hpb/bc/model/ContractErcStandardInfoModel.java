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

import com.hpb.bc.entity.BaseEntity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class ContractErcStandardInfoModel extends BaseModel {

    private String id;
    private String tokenSymbol;
    private String tokenSymbolImageUrl;
    private String tokenName;
    private Long decimals;
    private String deployTxHash;
    private String contractCreater;
    private String contractAddress;
    private Long tokenTotalSupply;
    private String contractType;
    private String verifiedStatus;
    private String price;
    private String changeRate;
    private String volume24h;
    private BigDecimal marketCap;
    private Integer holders;
    private Integer transfersNum;
    private String status;
    private Date createTimestamp;
    private Date updateTimestamp;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTokenSymbol() {
        return tokenSymbol;
    }

    public void setTokenSymbol(String tokenSymbol) {
        this.tokenSymbol = tokenSymbol;
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

    public Long getDecimals() {
        return decimals;
    }

    public void setDecimals(Long decimals) {
        this.decimals = decimals;
    }

    public String getDeployTxHash() {
        return deployTxHash;
    }

    public void setDeployTxHash(String deployTxHash) {
        this.deployTxHash = deployTxHash;
    }

    public String getContractCreater() {
        return contractCreater;
    }

    public void setContractCreater(String contractCreater) {
        this.contractCreater = contractCreater;
    }

    public String getContractAddress() {
        return contractAddress;
    }

    public void setContractAddress(String contractAddress) {
        this.contractAddress = contractAddress;
    }

    public Long getTokenTotalSupply() {
        return tokenTotalSupply;
    }

    public void setTokenTotalSupply(Long tokenTotalSupply) {
        this.tokenTotalSupply = tokenTotalSupply;
    }

    public String getContractType() {
        return contractType;
    }

    public void setContractType(String contractType) {
        this.contractType = contractType;
    }

    public String getVerifiedStatus() {
        return verifiedStatus;
    }

    public void setVerifiedStatus(String verifiedStatus) {
        this.verifiedStatus = verifiedStatus;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getChangeRate() {
        return changeRate;
    }

    public void setChangeRate(String changeRate) {
        this.changeRate = changeRate;
    }

    public String getVolume24h() {
        return volume24h;
    }

    public void setVolume24h(String volume24h) {
        this.volume24h = volume24h;
    }

    public BigDecimal getMarketCap() {
        return marketCap;
    }

    public void setMarketCap(BigDecimal marketCap) {
        this.marketCap = marketCap;
    }

    public Integer getHolders() {
        return holders;
    }

    public void setHolders(Integer holders) {
        this.holders = holders;
    }

    public Integer getTransfersNum() {
        return transfersNum;
    }

    public void setTransfersNum(Integer transfersNum) {
        this.transfersNum = transfersNum;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getCreateTimestamp() {
        return createTimestamp;
    }

    public void setCreateTimestamp(Date createTimestamp) {
        this.createTimestamp = createTimestamp;
    }

    public Date getUpdateTimestamp() {
        return updateTimestamp;
    }

    public void setUpdateTimestamp(Date updateTimestamp) {
        this.updateTimestamp = updateTimestamp;
    }
}
