package com.hpb.bc.model;


/**
 * @author LiXing
 * @version v1.0
 * date 2019/8/6 11:53
 **/
public class ContractApproveModel{

    private String contractAddress;

    private String approveStatus;

    public String getContractAddress() {
        return contractAddress;
    }

    public void setContractAddress(String contractAddress) {
        this.contractAddress = contractAddress;
    }

    public String getApproveStatus() {
        return approveStatus;
    }

    public void setApproveStatus(String approveStatus) {
        this.approveStatus = approveStatus;
    }
}
