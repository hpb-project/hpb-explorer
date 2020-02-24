package com.hpb.bc.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hpb.bc.async.AsyncErc20Task;
import com.hpb.bc.async.AsyncErcStandContractTask;
import com.hpb.bc.constant.BcConstant;
import com.hpb.bc.constant.StringConstant;
import com.hpb.bc.entity.ContractErcStandardInfo;
import com.hpb.bc.example.ContractErcStandardInfoExample;
import com.hpb.bc.mapper.AddressErcHolderMapper;
import com.hpb.bc.mapper.ContractErcStandardInfoMapper;
import com.hpb.bc.mapper.TxTransferRecordMapper;
import com.hpb.bc.service.ContractErcStandardInfoService;
import com.hpb.bc.util.DebuggerLogUtil;
import com.hpb.bc.util.ERC721Full;
import com.hpb.bc.util.Erc721Helper;
import com.hpb.bc.util.Latchs;
import io.hpb.web3.abi.FunctionEncoder;
import io.hpb.web3.abi.FunctionReturnDecoder;
import io.hpb.web3.abi.TypeReference;
import io.hpb.web3.abi.datatypes.Function;
import io.hpb.web3.abi.datatypes.Type;
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
import org.springframework.util.StopWatch;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class ContractErcStandardInfoServiceImpl implements ContractErcStandardInfoService {
    public Logger log = LoggerFactory.getLogger(this.getClass());
    @Autowired
    ContractErcStandardInfoMapper contractErcStandardInfoMapper;

    @Autowired
    AddressErcHolderMapper addressErcHolderMapper;

    @Autowired
    TxTransferRecordMapper txTransferRecordMapper;

    @Autowired
    AsyncErcStandContractTask asyncErcStandContractTask;

    @Autowired
    AsyncErc20Task asyncErc20Task;

    @Autowired
    private Erc721Helper erc721Helper;

    @Autowired
    private Admin admin;


    @Override
    public List<ContractErcStandardInfo> getContractErcStandardInfoList(ContractErcStandardInfo recored) {

        log.info("getContractErcStandardInfoList  record contract ===" + recored.getContractAddress());
        ContractErcStandardInfoExample example = new ContractErcStandardInfoExample();
        ContractErcStandardInfoExample.Criteria criteria = example.createCriteria();
        if (StringUtils.isNotEmpty(recored.getId())) {
            criteria.andIdEqualTo(recored.getId());
        }
        if (StringUtils.isNotEmpty(recored.getContractAddress())) {
            criteria.andIdEqualTo(recored.getContractAddress());
        }
        if (StringUtils.isNotEmpty(recored.getContractCreater())) {
            criteria.andContractCreaterEqualTo(recored.getContractCreater());
        }
        if (StringUtils.isNotEmpty(recored.getTokenName())) {
            criteria.andTokenNameEqualTo(recored.getTokenName());
        }
        if (StringUtils.isNotEmpty(recored.getTokenSymbol())) {
            criteria.andTokenSymbolEqualTo(recored.getTokenSymbol());
        }

        if (StringUtils.isEmpty(recored.getContractType())) {
            criteria.andContractTypeEqualTo(BcConstant.CONTRACT_ERC20_TYPE);
        }
        if (StringUtils.isNotEmpty(recored.getContractType())) {
            criteria.andContractTypeEqualTo(recored.getContractType());
        }

        criteria.andStatusEqualTo(BcConstant.CONTRACT_STATUS_ONE);
        example.setOrderByClause(" create_timestamp  desc ");
        return contractErcStandardInfoMapper.selectByExample(example);
    }

    @Override
    public PageInfo<ContractErcStandardInfo> queryPageContractErcStandardInfo(ContractErcStandardInfo contractErcStandardInfo, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);

        List<ContractErcStandardInfo> list = this.getContractErcStandardInfoList(contractErcStandardInfo);

        if (CollectionUtils.isNotEmpty(list)) {
            List<String> contractAddressList = list.stream().map(ContractErcStandardInfo::getContractAddress).collect(Collectors.toList());


            StopWatch stopWatch3 = new StopWatch();
            stopWatch3.start();

            CountDownLatch latch = Latchs.newCountDownLatch(contractAddressList.size() + 1);

            for (String cAddress : contractAddressList) {
                asyncErcStandContractTask.setTransferNum(list, cAddress, latch);
            }

            stopWatch3.stop();

            DebuggerLogUtil.debug("查询转移数数花费时间{}", false, stopWatch3.getTotalTimeMillis());

            asyncErcStandContractTask.setHolderNum(list, contractAddressList, latch);

            try {
                latch.await(30, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        PageInfo<ContractErcStandardInfo> pageInfo = new PageInfo<>(list);

        return pageInfo;
    }


    @Override
    public ContractErcStandardInfo getContractErcStandardInfoByContractAddress(String contractAddress) {
        Integer transferCount = txTransferRecordMapper.selectTransferCountByAddress(contractAddress);
        Integer holdersCount = addressErcHolderMapper.selectHoldersCountByContractAddress(contractAddress);
        ContractErcStandardInfo contractErcStandardInfo = this.selectByContractAddress(contractAddress);
        contractErcStandardInfo.setTransfersNum(transferCount);
        contractErcStandardInfo.setHolders(holdersCount);
        log.info("asyncErc20Task.getHpbBalance(contractAddress) ===");
        contractErcStandardInfo.setHpbBalance(asyncErc20Task.getHpbBalance(contractAddress));

        //<---------------- add by lij-----------------
        if (erc721Helper.isHRC721Contact(contractAddress) == true) {
            try {
                setHrc721TotalSupply(contractAddress, contractErcStandardInfo);
            } catch (Exception e) {
                contractErcStandardInfo.setTokenTotalSupply(0L);
                e.printStackTrace();
            }
        }

        return contractErcStandardInfo;
    }

    private void setHrc721TotalSupply(String contractAddress, ContractErcStandardInfo contractErcStandardInfo) throws Exception {
        final Function function = new Function(ERC721Full.FUNC_TOTALSUPPLY,
                Arrays.asList(),
                Arrays.asList(new TypeReference<Uint256>() {
                }));

        String encodedFunction = FunctionEncoder.encode(function);
        HpbCall hpbCall = admin.hpbCall(Transaction.createHpbCallTransaction(StringConstant.ZERO_ADDRESS, contractAddress, encodedFunction), DefaultBlockParameterName.LATEST).send();
        String value = hpbCall.getValue();
        List<Type> typeList = FunctionReturnDecoder.decode(value, function.getOutputParameters());
        Uint256 uint256 = (Uint256) typeList.get(0);

        contractErcStandardInfo.setTokenTotalSupply(uint256.getValue().longValue());
    }


    @Override
    public ContractErcStandardInfo selectByContractAddress(String contractAddress) {
        return contractErcStandardInfoMapper.selectByContractAddress(contractAddress);
    }
}
