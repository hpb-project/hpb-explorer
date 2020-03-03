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

package com.hpb.bc.entity;

import java.math.BigInteger;

/**
 * @author lij <email=jian.li@hpb.io>
 * @CreatTime 2019/9/4 16:36
 * @Desc 721代币实体
 */
public class Erc721Token {

    /**
     * 代币id
     */
    private Long tokenId;

    /**
     * 图片地址
     */
    private String imageUrl;

    /**
     * 当前持有人地址
     */
    private String holderAddress;

    /**
     * 所属721合约地址
     */

    private String parentErc721Address;

    /**
     * token 当前的索引
     */
    private BigInteger tokenIndex;

    /**
     * 转移数
     */
    private Long transferTimes;

    /**
     * 代币名称
     */
    private String tokenName;

    /**
     * 合约图片地址
     */
    private String tokenSymbolImageUrl;

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

    public Long getTransferTimes() {
        return transferTimes;
    }

    public void setTransferTimes(Long transferTimes) {
        this.transferTimes = transferTimes;
    }

    public BigInteger getTokenIndex() {
        return tokenIndex;
    }

    public void setTokenIndex(BigInteger tokenIndex) {
        this.tokenIndex = tokenIndex;
    }

    public Long getTokenId() {
        return tokenId;
    }

    public void setTokenId(Long tokenId) {
        this.tokenId = tokenId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getHolderAddress() {
        return holderAddress;
    }

    public void setHolderAddress(String holderAddress) {
        this.holderAddress = holderAddress;
    }

    public String getParentErc721Address() {
        return parentErc721Address;
    }

    public void setParentErc721Address(String parentErc721Address) {
        this.parentErc721Address = parentErc721Address;
    }
}
