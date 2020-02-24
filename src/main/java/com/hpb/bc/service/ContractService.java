package com.hpb.bc.service;

import com.github.pagehelper.PageInfo;
import com.hpb.bc.entity.ContractEventInfo;
import com.hpb.bc.entity.ContractInfo;
import com.hpb.bc.entity.ContractMethodInfo;
import com.hpb.bc.entity.result.Result;
import com.hpb.bc.model.ContractInfoModel;
import com.hpb.bc.model.ContractVerifyModel;
import com.hpb.bc.solidity.CompilationResult;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

public interface ContractService {
    Result<List<ContractEventInfo>> getContractEventInfoByEventHash(String eventHash);

    Result<List<ContractEventInfo>> getContractEventInfoByContractAddr(String contractAddr);

    Result<List<ContractMethodInfo>> getContractMethodInfoByContractAddr(String contractAddr);

    Result<List<ContractMethodInfo>> getContractMethodInfoByMethodId(String methodId);

    Result<ContractInfo> getContractInfoByContractAddr(String contractAddr);

    Result<PageInfo<ContractInfo>> getContractInfoByName(String contractName, int pageNum, int pageSize);

    Result<ContractInfo> validateContractInfo(String txHash, String contractAddr, String contractName, String contractSrc, String contractAbi,
                                              String contractBin, String optimizeFlag, String contractCompilerType, String contractCompilerVersion);


    String getContractAddress(String txHash);

    PageInfo<ContractInfo> queryPageContractInfoList(ContractInfoModel model);


    ContractInfo getContractInfoByContractAddrs(String contractAddr);

    void compileSrc();

    ResponseEntity<Object> contractVerify(ContractVerifyModel contractVerifyModel);
}
