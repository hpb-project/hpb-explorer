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

import com.hpb.bc.entity.ContractLibraryAddressInfo;
import org.omg.CORBA.PRIVATE_MEMBER;

import java.util.List;

/**
 * update by lij
 * 2019年10月25日 11:15:45
 */
public class ContractVerifyModel {

    /**
     * 合同地址
     */
    private String contractAddr;
    /**
     * 合约名称
     */
    private String contractName;

    /**
     * 编译器版本
     */
    private String contractCompilerVersion;

    /**
     * 是否优化
     */
    private String optimizeFlag;

    /**
     * 源代码
     */
    private String contractSrc;
    /**
     * 合约创建的交易hash  可能废弃此字段,从链上查询
     */
    private String txHash;

    /**
     * 编译器类型,默认solidity
     */
    private String contractCompilerType;

    /**
     * 合约abi
     */
    private String contractAbi;

    /**
     * 运行次数
     */
    private Long miscSettingRuns;
    /**
     * evm版本
     */
    private String hvmVersion;

    /**
     * 合约编译版本号,没用的字段
     */
    private String contractCompilerVersionNumber;

    /**
     * 合约库地址List
     */
    private List<ContractLibraryAddressInfo> contractLibraryAddressInfoList;

    /**
     * 合约编译后的bin
     */
    private String contractBin;

    public String getTxHash() {
        return txHash;
    }

    public void setTxHash(String txHash) {
        this.txHash = txHash;
    }

    public String getContractAddr() {
        return contractAddr;
    }

    public void setContractAddr(String contractAddr) {
        this.contractAddr = contractAddr;
    }

    public String getContractSrc() {
        return contractSrc;
    }

    public void setContractSrc(String contractSrc) {
        this.contractSrc = contractSrc;
    }

    public String getContractCompilerType() {
        return contractCompilerType;
    }

    public void setContractCompilerType(String contractCompilerType) {
        this.contractCompilerType = contractCompilerType;
    }

    public String getContractCompilerVersion() {
        return contractCompilerVersion;
    }

    public void setContractCompilerVersion(String contractCompilerVersion) {
        this.contractCompilerVersion = contractCompilerVersion;
    }

    public String getContractName() {
        return contractName;
    }

    public void setContractName(String contractName) {
        this.contractName = contractName;
    }

    public String getContractAbi() {
        return contractAbi;
    }

    public void setContractAbi(String contractAbi) {
        this.contractAbi = contractAbi;
    }

    public String getContractBin() {
        return contractBin;
    }

    public void setContractBin(String contractBin) {
        this.contractBin = contractBin;
    }

    public String getOptimizeFlag() {
        return optimizeFlag;
    }

    public void setOptimizeFlag(String optimizeFlag) {
        this.optimizeFlag = optimizeFlag;
    }

    public List<ContractLibraryAddressInfo> getContractLibraryAddressInfoList() {
        return contractLibraryAddressInfoList;
    }

    public void setContractLibraryAddressInfoList(List<ContractLibraryAddressInfo> contractLibraryAddressInfoList) {
        this.contractLibraryAddressInfoList = contractLibraryAddressInfoList;
    }

    public Long getMiscSettingRuns() {
        return miscSettingRuns;
    }

    public void setMiscSettingRuns(Long miscSettingRuns) {
        this.miscSettingRuns = miscSettingRuns;
    }

    public String getHvmVersion() {
        return hvmVersion;
    }

    public void setHvmVersion(String hvmVersion) {
        this.hvmVersion = hvmVersion;
    }

    public String getContractCompilerVersionNumber() {
        return contractCompilerVersionNumber;
    }

    public void setContractCompilerVersionNumber(String contractCompilerVersionNumber) {
        this.contractCompilerVersionNumber = contractCompilerVersionNumber;
    }
}
