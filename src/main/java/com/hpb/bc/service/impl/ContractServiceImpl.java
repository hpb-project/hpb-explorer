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

package com.hpb.bc.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hpb.bc.async.AsyncErc20Task;
import com.hpb.bc.async.AsyncTask;
import com.hpb.bc.configure.HpbContractValidateProperties;
import com.hpb.bc.constant.BcConstant;
import com.hpb.bc.entity.ContractErcStandardInfo;
import com.hpb.bc.entity.ContractEventInfo;
import com.hpb.bc.entity.ContractInfo;
import com.hpb.bc.entity.ContractMethodInfo;
import com.hpb.bc.entity.result.Result;
import com.hpb.bc.entity.result.ResultCode;
import com.hpb.bc.example.ContractEventInfoExample;
import com.hpb.bc.example.ContractInfoExample;
import com.hpb.bc.example.ContractMethodInfoExample;
import com.hpb.bc.mapper.ContractErcStandardInfoMapper;
import com.hpb.bc.mapper.ContractEventInfoMapper;
import com.hpb.bc.mapper.ContractInfoMapper;
import com.hpb.bc.mapper.ContractMethodInfoMapper;
import com.hpb.bc.model.ContractInfoModel;
import com.hpb.bc.model.ContractVerifyModel;
import com.hpb.bc.propeller.solidity.HpbPropellerSolidityCompiler;
import com.hpb.bc.service.ContractService;
import com.hpb.bc.solidity.CompilationResult;
import com.hpb.bc.solidity.values.SoliditySource;
import com.hpb.bc.solidity.values.SoliditySourceFile;
import com.hpb.bc.util.UUIDGeneratorUtil;
import io.hpb.web3.crypto.Hash;
import io.hpb.web3.crypto.WalletUtils;
import io.hpb.web3.protocol.ObjectMapperFactory;
import io.hpb.web3.protocol.admin.Admin;
import io.hpb.web3.protocol.core.methods.response.AbiDefinition;
import io.hpb.web3.protocol.core.methods.response.AbiDefinition.NamedType;
import io.hpb.web3.protocol.core.methods.response.HpbTransaction;
import io.hpb.web3.protocol.core.methods.response.Transaction;
import io.hpb.web3.protocol.core.methods.response.TransactionReceipt;
import io.hpb.web3.utils.Numeric;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;

@Service
public class ContractServiceImpl implements ContractService {
    private static final String FUNCTION = "function";
    private static final String EVENT = "event";
    private static final String COMMA = ",";
    private static final String ONE_BLANK = " ";
    private static final String LEFT_BRACKET = "(";
    private static final String RIGHT_BRACKET = ")";
    public Logger log = LoggerFactory.getLogger("contractCompileAppenderLogger");
    @Autowired
    AsyncErc20Task asyncErc20Task;
    @Autowired
    HpbContractValidateProperties hpbContractValidateProperties;
    @Autowired
    private Admin admin;
    @Autowired
    private ContractEventInfoMapper contractEventInfoMapper;
    @Autowired
    private ContractInfoMapper contractInfoMapper;
    @Autowired
    private ContractMethodInfoMapper contractMethodInfoMapper;
    @Autowired
    private ContractErcStandardInfoMapper contractErcStandardInfoMapper;


    @Autowired
    private RestTemplate restTemplate;

    @Override
    public Result<List<ContractEventInfo>> getContractEventInfoByEventHash(String eventHash) {
        if (StringUtils.isBlank(eventHash) || eventHash.length() != 66) {
            return new Result<List<ContractEventInfo>>(ResultCode.SUCCESS);
        }
        ContractEventInfoExample contractEventInfoExample = new ContractEventInfoExample();
        contractEventInfoExample.createCriteria().andEventHashEqualTo(eventHash);
        List<ContractEventInfo> list = contractEventInfoMapper.selectByExample(contractEventInfoExample);
        return new Result<List<ContractEventInfo>>(ResultCode.SUCCESS, list);
    }

    @Override
    public Result<List<ContractEventInfo>> getContractEventInfoByContractAddr(String contractAddr) {
        if (!WalletUtils.isValidAddress(contractAddr)) {
            return new Result<List<ContractEventInfo>>(ResultCode.SUCCESS);
        }
        ContractEventInfoExample contractEventInfoExample = new ContractEventInfoExample();
        contractEventInfoExample.createCriteria().andContractAddrEqualTo(contractAddr);
        List<ContractEventInfo> list = contractEventInfoMapper.selectByExample(contractEventInfoExample);
        return new Result<List<ContractEventInfo>>(ResultCode.SUCCESS, list);
    }

    @Override
    public Result<List<ContractMethodInfo>> getContractMethodInfoByContractAddr(String contractAddr) {
        if (!WalletUtils.isValidAddress(contractAddr)) {
            return new Result<List<ContractMethodInfo>>(ResultCode.SUCCESS);
        }
        ContractMethodInfoExample contractMethodInfoExample = new ContractMethodInfoExample();
        contractMethodInfoExample.createCriteria().andContractAddrEqualTo(contractAddr);
        List<ContractMethodInfo> list = contractMethodInfoMapper.selectByExample(contractMethodInfoExample);
        return new Result<List<ContractMethodInfo>>(ResultCode.SUCCESS, list);
    }

    @Override
    public Result<List<ContractMethodInfo>> getContractMethodInfoByMethodId(String methodId) {
        if (StringUtils.isBlank(methodId) || methodId.length() != 10) {
            return new Result<List<ContractMethodInfo>>(ResultCode.SUCCESS);
        }
        ContractMethodInfoExample contractMethodInfoExample = new ContractMethodInfoExample();
        contractMethodInfoExample.createCriteria().andMethodIdEqualTo(methodId);
        List<ContractMethodInfo> list = contractMethodInfoMapper.selectByExample(contractMethodInfoExample);
        return new Result<List<ContractMethodInfo>>(ResultCode.SUCCESS, list);
    }

    @Override
    public Result<PageInfo<ContractInfo>> getContractInfoByName(String contractName, int pageNum, int pageSize) {
        if (StringUtils.isBlank(contractName)) {
            return new Result<PageInfo<ContractInfo>>(ResultCode.SUCCESS);
        }
        PageHelper.startPage(pageNum, pageSize);
        ContractInfoExample contractInfoExample = new ContractInfoExample();
        contractInfoExample.createCriteria().andContractNameEqualTo(contractName);
        contractInfoExample.setOrderByClause("contract_name desc");
        List<ContractInfo> list = contractInfoMapper.selectByExample(contractInfoExample);
        PageInfo<ContractInfo> pageInfo = new PageInfo<ContractInfo>(list);
        return new Result<PageInfo<ContractInfo>>(ResultCode.SUCCESS, pageInfo);
    }

    @Override
    public Result<ContractInfo> getContractInfoByContractAddr(String contractAddr) {
        if (!WalletUtils.isValidAddress(contractAddr)) {
            return new Result<ContractInfo>(ResultCode.SUCCESS);
        }
        ContractInfo contractInfo = contractInfoMapper.selectByPrimaryKey(contractAddr);

        ContractErcStandardInfo standardInfo = contractErcStandardInfoMapper.selectByContractAddress(contractAddr);
        if (standardInfo != null) {
            String deployTxHash = standardInfo.getDeployTxHash();
            try {
                HpbTransaction hpbTransaction = admin.hpbGetTransactionByHash(deployTxHash).sendAsync().get(AsyncTask.TIME_OUT, TimeUnit.MINUTES);
                Transaction transaction = hpbTransaction.getResult();
                String input = transaction.getInput();
                contractInfo.setContractCreater(input.startsWith("0x") ? Numeric.cleanHexPrefix(input) : input);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return new Result<ContractInfo>(ResultCode.SUCCESS, contractInfo);
    }

    @Override
    public Result<ContractInfo> validateContractInfo(String txHash, String contractAddr, String contractName,
                                                     String contractSrc, String contractAbi, String contractBin, String optimizeFlag, String contractCompilerType, String contractCompilerVersion) {
        try {
            HpbTransaction hpbTransaction = admin.hpbGetTransactionByHash(txHash).sendAsync().
                    get(BcConstant.WEB3J_TIMEOUT, TimeUnit.MINUTES);
            Transaction transaction = hpbTransaction.getResult();
            ContractInfo contractInfo = new ContractInfo();
            if (transaction != null && contractAddr.equals(transaction.getTo())) {
                String input = transaction.getInput();
                if (contractAbi.equals(input)) {
                    String contractCreater = transaction.getFrom();
                    contractInfo.setContractName(contractName);
                    contractInfo.setContractCreater(contractCreater);
                    contractInfo.setContractSrc(contractSrc);
                    contractInfo.setContractAbi(contractAbi);
                    contractInfo.setContractBin(contractBin);
                    contractInfo.setOptimizeFlag(optimizeFlag);
                    contractInfo.setCompilerType(contractCompilerType);
                    contractInfo.setCompilerVersion(contractCompilerVersion);
                    contractInfoMapper.insert(contractInfo);

                    ObjectMapper objectMapper = ObjectMapperFactory.getObjectMapper();
                    AbiDefinition[] abiDefinition = objectMapper.readValue(contractAbi, AbiDefinition[].class);
                    List<AbiDefinition> abiDefinitionList = Arrays.asList(abiDefinition);
                    abiDefinitionList.forEach(obj -> {
                        if (FUNCTION.equals(obj.getType())) {
                            String name = obj.getName();
                            List<NamedType> parameters = obj.getInputs();
                            StringBuilder methodBody = new StringBuilder();
                            StringBuilder methodName = new StringBuilder();
                            methodBody.append(name);
                            methodBody.append(LEFT_BRACKET);
                            String params = parameters.stream().map(NamedType::getType).collect(Collectors.joining(COMMA));
                            methodBody.append(params);
                            methodBody.append(RIGHT_BRACKET);

                            methodName.append(name);
                            methodName.append(LEFT_BRACKET);
                            String ps = parameters.stream().map(x -> x.getType() + ONE_BLANK + x.getName())
                                    .collect(Collectors.joining(COMMA));
                            methodName.append(ps);
                            methodName.append(RIGHT_BRACKET);
                            String methodId = buildMethodId(methodBody.toString());
                            ContractMethodInfo contractMethodInfo = new ContractMethodInfo();
                            contractMethodInfo.setContractAddr(contractAddr);
                            contractMethodInfo.setId(UUIDGeneratorUtil.generate(obj));
                            contractMethodInfo.setMethodId(methodId);
                            contractMethodInfo.setMethodName(methodName.toString());
                            contractMethodInfoMapper.insert(contractMethodInfo);
                        }
                        if (EVENT.equals(obj.getType())) {
                            String name = obj.getName();
                            List<NamedType> parameters = obj.getInputs();
                            StringBuilder methodBody = new StringBuilder();
                            StringBuilder eventName = new StringBuilder();
                            methodBody.append(name);
                            methodBody.append(LEFT_BRACKET);
                            String params = parameters.stream().map(NamedType::getType).collect(Collectors.joining(COMMA));
                            methodBody.append(params);
                            methodBody.append(RIGHT_BRACKET);

                            eventName.append(name);
                            eventName.append(LEFT_BRACKET);
                            String ps = parameters.stream().map(x -> x.getType() + ONE_BLANK + x.getName())
                                    .collect(Collectors.joining(COMMA));
                            eventName.append(ps);
                            eventName.append(RIGHT_BRACKET);
                            String eventHash = buildEventSignature(methodBody.toString());
                            ContractEventInfo contractEventInfo = new ContractEventInfo();
                            contractEventInfo.setContractAddr(contractAddr);
                            contractEventInfo.setId(UUIDGeneratorUtil.generate(obj));
                            contractEventInfo.setEventName(eventName.toString());
                            contractEventInfo.setEventHash(eventHash);
                            contractEventInfoMapper.insert(contractEventInfo);
                        }
                    });
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            e.printStackTrace();
        }
        return null;
    }

    private String buildMethodId(String methodBody) {
        byte[] input = methodBody.getBytes();
        byte[] hash = Hash.sha3(input);
        return Numeric.toHexString(hash).substring(0, 10);
    }

    private String buildEventSignature(String methodBody) {
        byte[] input = methodBody.getBytes();
        byte[] hash = Hash.sha3(input);
        return Numeric.toHexString(hash);
    }

    @Override
    public String getContractAddress(String txHash) {
        try {
            HpbTransaction hpbTransaction = admin.hpbGetTransactionByHash(txHash).sendAsync().get(BcConstant.WEB3J_TIMEOUT, TimeUnit.MINUTES);
            Transaction transaction = hpbTransaction.getResult();
            if (transaction != null) {
                Optional<TransactionReceipt> txReceiptOptional = admin.hpbGetTransactionReceipt(hpbTransaction.getTransaction().get().getHash()).sendAsync().get(BcConstant.WEB3J_TIMEOUT, TimeUnit.MINUTES).getTransactionReceipt();

                if (txReceiptOptional.isPresent()) {
                    TransactionReceipt transactionReceipt = txReceiptOptional.get();
                    return transactionReceipt.getContractAddress();
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
        return null;
    }


    @Override
    public PageInfo<ContractInfo> queryPageContractInfoList(ContractInfoModel model) {
        PageHelper.startPage(model.getCurrentPage(), model.getPageSize());
        ContractInfoExample contractInfoExample = new ContractInfoExample();
        ContractInfoExample.Criteria criteria = contractInfoExample.createCriteria();
        if (StringUtils.isNotEmpty(model.getContractAddr())) {
            criteria.andContractAddrEqualTo(model.getContractAddr());
        }

        if (StringUtils.isNotEmpty(model.getContractName())) {
            criteria.andContractNameEqualTo(model.getContractName());
        }

        contractInfoExample.setOrderByClause(" create_timestamp desc");
        List<ContractInfo> list = contractInfoMapper.selectByExample(contractInfoExample);
        PageInfo<ContractInfo> pageInfo = new PageInfo<ContractInfo>(list);
        List<ContractInfo> contractInfoList = pageInfo.getList();
        List<ContractInfo> resultContractInfoList = Collections.synchronizedList(new ArrayList<ContractInfo>());
        try {
            CountDownLatch countDownLatch = new CountDownLatch(contractInfoList.size());
            for (ContractInfo contractInfo : contractInfoList) {
                asyncErc20Task.getBalanceByAddress(countDownLatch, contractInfo, resultContractInfoList);
            }
            countDownLatch.await(2, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return pageInfo;
    }


    @Override
    public void compileSrc() {
        HpbPropellerSolidityCompiler hpbPropellerSolidityCompiler = HpbPropellerSolidityCompiler.getInstance();
        String path = "/hpb/project/HpbExplore/";
        if (StringUtils.isNotEmpty(hpbContractValidateProperties.getPath())) {
            path = hpbContractValidateProperties.getPath();
        }
        log.info("1. compileSrc path====" + path);
        try {
            SoliditySourceFile soliditySourceFile = SoliditySource.from(new File(path));
            log.info("2. soliditySourceFile  =======");
            if (soliditySourceFile.getSource() != null) {
                log.info("3. soliditySourceFile  =========" + soliditySourceFile.getSource().getAbsolutePath());
            }
            CompilationResult compilationResult = hpbPropellerSolidityCompiler.compileSrc(soliditySourceFile);
            log.info("4. compilationResult ===" + compilationResult.getContracts());
        } catch (Exception e) {
            log.info("5. compilationResult exception compileSrc path ===" + e);
            e.printStackTrace();
        }

    }


    @Override
    public ContractInfo getContractInfoByContractAddrs(String contractAddr) {
        return contractInfoMapper.selectByPrimaryKey(contractAddr);
    }


    @Override
    public ResponseEntity<Object> contractVerify(ContractVerifyModel contractVerifyModel) {
        ResponseEntity<Object> objectResponseEntity = restTemplate.postForEntity(hpbContractValidateProperties.getValidateServiceUrl(), contractVerifyModel, Object.class);
        return objectResponseEntity;
    }
}