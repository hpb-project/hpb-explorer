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

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.hpb.bc.async.AddressErcBalanceTask;
import com.hpb.bc.async.AsyncErc721Task;
import com.hpb.bc.constant.BcConstant;
import com.hpb.bc.constant.StringConstant;
import com.hpb.bc.entity.ContractErcStandardInfo;
import com.hpb.bc.entity.Erc721Token;
import com.hpb.bc.entity.Erc721TokenModel;
import com.hpb.bc.entity.TxTransferRecord;
import com.hpb.bc.example.ContractErcStandardInfoExample;
import com.hpb.bc.example.TxTransferRecordExample;
import com.hpb.bc.mapper.ContractErcStandardInfoMapper;
import com.hpb.bc.mapper.TxTransferRecordMapper;
import com.hpb.bc.model.Erc20TokenModel;
import com.hpb.bc.model.TxTransferRecordModel;
import com.hpb.bc.service.TxTransferRecordService;
import com.hpb.bc.util.ERC721Full;
import com.hpb.bc.util.Erc721Helper;
import com.hpb.bc.util.Latchs;
import io.hpb.web3.abi.FunctionEncoder;
import io.hpb.web3.abi.FunctionReturnDecoder;
import io.hpb.web3.abi.TypeReference;
import io.hpb.web3.abi.datatypes.Address;
import io.hpb.web3.abi.datatypes.Function;
import io.hpb.web3.abi.datatypes.Type;
import io.hpb.web3.abi.datatypes.Utf8String;
import io.hpb.web3.abi.datatypes.generated.Uint256;
import io.hpb.web3.protocol.admin.Admin;
import io.hpb.web3.protocol.core.DefaultBlockParameterName;
import io.hpb.web3.protocol.core.methods.request.Transaction;
import io.hpb.web3.protocol.core.methods.response.HpbCall;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class TxTransferRecordServiceImpl implements TxTransferRecordService {

    private static final Logger logger = LoggerFactory.getLogger(TxTransferRecordServiceImpl.class);
    @Autowired
    TxTransferRecordMapper txTransferRecordMapper;
    @Autowired
    ContractErcStandardInfoMapper contractErcStandardInfoMapper;
    @Autowired
    AddressErcBalanceTask addressErcBalanceTask;
    @Autowired
    private Admin admin;
    @Autowired
    private Erc721Helper erc721Helper;

    @Autowired
    private AsyncErc721Task asyncErc721Task;

    @Override
    public boolean save(TxTransferRecord record) {
        int result = txTransferRecordMapper.insert(record);
        if (result > 0) {
            return true;
        }
        {
            return false;
        }
    }


    @Override
    public List<TxTransferRecord> getTxTransferRecordListByContractAddress(String address) {
        return txTransferRecordMapper.selectTxTransferRecordListByContractAddress(address);
    }


    @Override
    public PageInfo<TxTransferRecord> queryPageTxTransferRecordListByContractAddress(String address, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<TxTransferRecord> list = this.getTxTransferRecordListByContractAddress(address);


        packageBlockTimestamp4TransferRecordList(list);

        PageInfo<TxTransferRecord> pageInfo = new PageInfo<TxTransferRecord>(list);
        return pageInfo;
    }


    @Override
    public PageInfo<TxTransferRecord> queryPageTxTransferRecordListByTxTransferRecord(TxTransferRecordModel record, int pageNum, int pageSize) {

        PageHelper.startPage(pageNum, pageSize);
        TxTransferRecordExample example = new TxTransferRecordExample();
        TxTransferRecordExample.Criteria criterion = example.createCriteria();
        if (StringUtils.isNotBlank(record.getTxHash())) {
            criterion.andTxHashEqualTo(record.getTxHash());
        }
        if (StringUtils.isNotBlank(record.getBlockHash())) {
            criterion.andBlockHashEqualTo(record.getBlockHash());
        }

        if (record.getBlockNumber() != null) {
            criterion.andBlockNumberEqualTo(record.getBlockNumber());
        }

        if (StringUtils.isNotBlank(record.getContractAddress())) {
            criterion.andContractAddressEqualTo(record.getContractAddress());
        }
        if (StringUtils.isNotBlank(record.getFromAddress())) {
            criterion.andFromAddressEqualTo(record.getFromAddress());
        }
        if (StringUtils.isNotBlank(record.getToAddress())) {
            criterion.andToAddressEqualTo(record.getToAddress());
        }


        TxTransferRecordExample.Criteria criterion2 = example.createCriteria();
        if (StringUtils.isNotEmpty(record.getReferedAddress())) {
            criterion.andFromAddressEqualTo(record.getReferedAddress());
            criterion2.andToAddressEqualTo(record.getReferedAddress());
        }
        example.or(criterion2);


        String queryTargetType = record.getQueryTargetType();
        if (StringUtils.isNotBlank(queryTargetType)) {
            if ("hrc20".equalsIgnoreCase(queryTargetType)) {

                criterion.andTokenIdIsNull();
                criterion2.andTokenIdIsNull();
            } else if ("hrc721".equalsIgnoreCase(queryTargetType)) {

                criterion.andTokenIdIsNotNull();
                criterion2.andTokenIdIsNotNull();
            }
        } else {
            logger.error("当前查询传入的代币标准不支持,当前入参为:【{}】", queryTargetType);
        }

        example.setOrderByClause(" block_number desc ");
        List<TxTransferRecord> list = txTransferRecordMapper.selectByExample(example);

        packageBlockTimestamp4TransferRecordList(list);

        PageInfo<TxTransferRecord> pageInfo = new PageInfo<TxTransferRecord>(list);
        return pageInfo;
    }


    @Override
    public List<Erc20TokenModel> queryErc20TokenModelListByModel(String address) {
        ContractErcStandardInfoExample contractErcStandardInfoExample = new ContractErcStandardInfoExample();
        ContractErcStandardInfoExample.Criteria criteria = contractErcStandardInfoExample.createCriteria();

        criteria.andStatusEqualTo(BcConstant.CONTRACT_STATUS_ONE);
        List<ContractErcStandardInfo> contractErcStandardInfoList = contractErcStandardInfoMapper.selectByExample(contractErcStandardInfoExample);
        List<Erc20TokenModel> resultAddressErc20List = Collections.synchronizedList(new ArrayList<Erc20TokenModel>());
        try {
            CountDownLatch countDownLatch = new CountDownLatch(contractErcStandardInfoList.size());
            for (ContractErcStandardInfo contractErcStandardInfo : contractErcStandardInfoList) {
                addressErcBalanceTask.getERC20BalanceByAddress(countDownLatch, contractErcStandardInfo, resultAddressErc20List, address);
            }
            countDownLatch.await(2, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return resultAddressErc20List;
    }

    @Override
    public PageInfo<Erc721Token> getHrc721StoragePage(Erc721TokenModel model, int currentPage, int pageSize) {

        String contractAddress = model.getParentErc721Address();
        if (StringUtils.isBlank(contractAddress)) {
            return new PageInfo(Lists.newArrayList());
        }

        if (erc721Helper.isHRC721Contact(contractAddress) == false) {
            return new PageInfo(Lists.newArrayList());
        }


        PageInfo<Erc721Token> erc721TokenPage = getTokenListFromMainNet(contractAddress, currentPage, pageSize);


        setErc721TokenInfoFromMainNet(erc721TokenPage.getList(), contractAddress);

        return erc721TokenPage;
    }


    @Override
    public Erc721Token getErc721TokenInfoById(Long tokenId, String address) {
        Erc721Token token = new Erc721Token();

        setTokenInfoByIdFromRpc(tokenId, address, token);

        setTransferTimes(token);

        copyInfoFromParam(address, token);

        setTokenSymbolImageUrl(address, token);

        return token;
    }

    private void setTokenSymbolImageUrl(String address, Erc721Token token) {
        ContractErcStandardInfo ercStandardInfo = contractErcStandardInfoMapper.selectByContractAddress(address);
        if (ercStandardInfo != null) {
            token.setTokenSymbolImageUrl(ercStandardInfo.getTokenSymbolImageUrl());
        }
    }

    @Override
    public PageInfo<TxTransferRecord> getErc721TokenTransferPage(String address, Long tokenId, int currentPage, int pageSize) {
        PageHelper.startPage(currentPage, pageSize);

        TxTransferRecordExample example = new TxTransferRecordExample();
        TxTransferRecordExample.Criteria criteria = example.createCriteria();
        criteria.andTokenIdEqualTo(tokenId);
        criteria.andContractAddressEqualTo(address);
        List<TxTransferRecord> transferList = txTransferRecordMapper.selectByExample(example);


        packageBlockTimestamp4TransferRecordList(transferList);

        PageInfo<TxTransferRecord> pageInfo = new PageInfo<>(transferList);
        return pageInfo;
    }

    private void packageBlockTimestamp4TransferRecordList(List<TxTransferRecord> transferList) {
        if (CollectionUtils.isNotEmpty(transferList)) {
            CountDownLatch latch = Latchs.newCountDownLatch(transferList.size());
            for (TxTransferRecord r : transferList) {
                asyncErc721Task.setBlockTimestamp(r, latch);
            }
            Latchs.await(latch);
        }
    }

    private void copyInfoFromParam(String address, Erc721Token token) {
        token.setParentErc721Address(address);
    }


    private void setTransferTimes(Erc721Token token) {
        Long tokenId = token.getTokenId();

        TxTransferRecordExample example = new TxTransferRecordExample();
        TxTransferRecordExample.Criteria criteria = example.createCriteria();
        criteria.andTokenIdEqualTo(tokenId);
        long transferTimes = txTransferRecordMapper.countByExample(example);
        token.setTransferTimes(transferTimes);

    }

    private void setTokenInfoByIdFromRpc(Long tokenId, String address, Erc721Token token) {
        try {
            token.setTokenId(tokenId);
            logger.debug("设置 tokenid 没问题");
            logger.debug("开始设置图片地址");

            String uri = tokenURI(tokenId, address);
            token.setImageUrl(uri);

            logger.debug("图片设置完成,开始设置当前持有人");

            String holderAddress = ownerOf(tokenId, address);
            token.setHolderAddress(holderAddress);
            logger.debug("设置当前持有人结束");

            logger.debug("开始设置token名称");

            String tokenName = symbol(address);
            token.setTokenName(tokenName);
            logger.debug("设置token名称结束");

        } catch (Exception e) {
            logger.error("根据合约地址 {} 和代币的索引 {} 查询代币id失败,原因: {}", address, tokenId, e.getMessage());
            e.printStackTrace();
        }
    }

    private String symbol(String contractAddress) throws Exception {
        final Function function = new Function(ERC721Full.FUNC_SYMBOL,
                Arrays.asList(),
                Arrays.asList(new TypeReference<Utf8String>() {
                }));

        String encodedFunction = FunctionEncoder.encode(function);
        HpbCall hpbCall = admin.hpbCall(Transaction.createHpbCallTransaction(StringConstant.ZERO_ADDRESS, contractAddress, encodedFunction), DefaultBlockParameterName.LATEST).send();
        String value = hpbCall.getValue();
        List<Type> typeList = FunctionReturnDecoder.decode(value, function.getOutputParameters());
        Utf8String utf8String = (Utf8String) typeList.get(0).getValue();
        return utf8String.getValue();
    }

    private String ownerOf(Long tokenId, String contractAddress) throws Exception {
        final Function function = new Function(ERC721Full.FUNC_OWNEROF,
                Arrays.asList(new Uint256(tokenId)),
                Arrays.asList(new TypeReference<Address>() {
                }));

        String encodedFunction = FunctionEncoder.encode(function);
        HpbCall hpbCall = admin.hpbCall(Transaction.createHpbCallTransaction(StringConstant.ZERO_ADDRESS, contractAddress, encodedFunction), DefaultBlockParameterName.LATEST).send();
        String value = hpbCall.getValue();
        List<Type> typeList = FunctionReturnDecoder.decode(value, function.getOutputParameters());
        Utf8String utf8String = (Utf8String) typeList.get(0).getValue();
        return utf8String.getValue();
    }

    private String tokenURI(Long tokenId, String contractAddress) throws Exception {
        final Function function = new Function(ERC721Full.FUNC_TOKENURI,
                Arrays.asList(new Uint256(tokenId)),
                Arrays.asList(new TypeReference<Utf8String>() {
                }));

        String encodedFunction = FunctionEncoder.encode(function);
        HpbCall hpbCall = admin.hpbCall(Transaction.createHpbCallTransaction(StringConstant.ZERO_ADDRESS, contractAddress, encodedFunction), DefaultBlockParameterName.LATEST).send();
        String value = hpbCall.getValue();
        List<Type> typeList = FunctionReturnDecoder.decode(value, function.getOutputParameters());
        Utf8String utf8String = (Utf8String) typeList.get(0).getValue();

        return utf8String.getValue();
    }

    private PageInfo<Erc721Token> getTokenListFromMainNet(String cAddress, int pageNo, int pageSize) {


        Long totalSupply = getTotalSupply(cAddress);

        if (totalSupply < 1 || pageNo < 1 || pageSize < 1) {
            return new PageInfo<>(Lists.newArrayList());
        }


        List<Erc721Token> tokenList = queryTokenIdListByIndexFromRpc(cAddress, pageNo, pageSize, totalSupply);


        PageInfo<Erc721Token> tokenPageInfo = getErc721TokenPageInfo(pageSize, totalSupply, tokenList);

        return tokenPageInfo;
    }

    private PageInfo<Erc721Token> getErc721TokenPageInfo(int pageSize, Long totalSupply, List<Erc721Token> tokenList) {
        PageInfo<Erc721Token> tokenPageInfo = new PageInfo<>(tokenList);

        long yu = totalSupply % pageSize;
        Long pages = 0L;
        if (yu == 0) {
            pages = (totalSupply / pageSize);
        } else {
            pages = (totalSupply / pageSize) + 1;
        }

        tokenPageInfo.setPages(pages.intValue());
        tokenPageInfo.setTotal(totalSupply.intValue());
        return tokenPageInfo;
    }

    private List<Erc721Token> queryTokenIdListByIndexFromRpc(String parentErc721Address, int pageNo, int pageSize, Long totalSupply) {


        Long start = (pageNo - 1L) * pageSize;

        Long end = calculateEnd(pageSize, totalSupply, start);


        Long reverseStart = (totalSupply - 1) - (end - 1);
        Long reverseEnd = (totalSupply) - start;


        List<Erc721Token> tokenList = Collections.synchronizedList(Lists.newArrayList());


        Long nowPageCap = end - start;
        if (nowPageCap < 1) {
            return Lists.newArrayList();
        }

        CountDownLatch latch = Latchs.newCountDownLatch(nowPageCap.intValue());

        for (long tokenIndex = reverseStart; tokenIndex < reverseEnd; tokenIndex++) {
            asyncErc721Task.buildTokenListByIndex(parentErc721Address, tokenList, tokenIndex, latch);
        }


        Latchs.await(latch);

        return sortListByTokenIdDesc(tokenList);
    }


    private List<Erc721Token> sortListByTokenIdDesc(List<Erc721Token> tokenList) {
        return tokenList.stream().sorted((x, y) -> {
            if (x == null) {
                return -1;
            }
            if (y == null) {
                return 1;
            }
            Long xTokenId = x.getTokenId();
            Long yTokenId = y.getTokenId();
            if (xTokenId == null) {
                logger.error("出现了空token,id");
                return -1;
            }
            if (yTokenId == null) {
                logger.error("出现了空token,id");
                return 1;
            }
            return xTokenId.compareTo(yTokenId) == 1 ? -1 : 1;
        }).collect(Collectors.toList());
    }

    private Long calculateEnd(int pageSize, Long totalSupply, Long start) {
        Long end = start + pageSize;
        if (end > totalSupply) {
            end = totalSupply;
        }
        return end;
    }

    private Long getTotalSupply(String parentErc721Address) {
        Long totalSupply;
        try {
            totalSupply = getHrc721TotalSupply(parentErc721Address).longValue();
        } catch (Exception e) {
            logger.error("查询合约地址为 {} 的totalSupply失败,异常信息", parentErc721Address, e.getMessage());
            totalSupply = 0L;
            e.printStackTrace();
        }
        return totalSupply;
    }


    private BigInteger getHrc721TotalSupply(String contractAddress) throws Exception {
        final Function function = new Function(ERC721Full.FUNC_TOTALSUPPLY,
                Arrays.asList(),
                Arrays.asList(new TypeReference<Uint256>() {
                }));

        String encodedFunction = FunctionEncoder.encode(function);
        HpbCall hpbCall = admin.hpbCall(Transaction.createHpbCallTransaction(StringConstant.ZERO_ADDRESS, contractAddress, encodedFunction), DefaultBlockParameterName.LATEST).send();
        String value = hpbCall.getValue();
        List<Type> typeList = FunctionReturnDecoder.decode(value, function.getOutputParameters());
        Uint256 uint256 = (Uint256) typeList.get(0);
        return uint256.getValue();
    }


    private void setErc721TokenInfoFromMainNet(List<Erc721Token> tokenList, String contactAddress) {
        if (CollectionUtils.isEmpty(tokenList)) {
            return;
        }

        CountDownLatch latch = Latchs.newCountDownLatch(tokenList.size() * 2);

        tokenList.parallelStream().forEach(e -> {
            Long tokenId = e.getTokenId();
            Uint256 uint256TokenId = new Uint256(tokenId);

            asyncErc721Task.setUri(contactAddress, e, uint256TokenId, latch);

            asyncErc721Task.setHolderAddress(contactAddress, e, uint256TokenId, latch);
        });

        Latchs.await(latch);
    }


}
