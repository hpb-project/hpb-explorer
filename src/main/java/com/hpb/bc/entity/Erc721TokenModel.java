package com.hpb.bc.entity;

import com.hpb.bc.model.BaseModel;

import java.math.BigInteger;

/**
 * @author lij <email=jian.li@hpb.io>
 * @CreatTime 2019/9/4 16:31
 * @Desc 721代币
 */
public class Erc721TokenModel extends BaseModel {

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
