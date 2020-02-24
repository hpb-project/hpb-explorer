package com.hpb.bc.service;

import com.github.pagehelper.PageInfo;
import com.hpb.bc.entity.ContractErcStandardInfo;

import java.util.List;

public interface ContractErcStandardInfoService {

    List<ContractErcStandardInfo> getContractErcStandardInfoList(ContractErcStandardInfo recored);

    PageInfo<ContractErcStandardInfo> queryPageContractErcStandardInfo(ContractErcStandardInfo contractErcStandardInfo, int pageNum, int pageSize);

    ContractErcStandardInfo getContractErcStandardInfoByContractAddress(String contractAddress);

    ContractErcStandardInfo selectByContractAddress(String contractAddress);

}
