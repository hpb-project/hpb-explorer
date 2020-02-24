package com.hpb.bc.model;

/**
 * @author LiXing
 * @version v1.0
 * date 2019/8/20 18:33
 **/
public class Erc20ContractTransferModel extends BaseModel {
    private String address;
    private String contractAddress;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContractAddress() {
        return contractAddress;
    }

    public void setContractAddress(String contractAddress) {
        this.contractAddress = contractAddress;
    }
}
