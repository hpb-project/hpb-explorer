package com.hpb.bc.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hpb.bc.async.AsyncGetBalanceOrderTask;
import com.hpb.bc.configure.*;
import com.hpb.bc.constant.BcConstant;
import com.hpb.bc.entity.Addrs;
import com.hpb.bc.entity.ContractErcStandardInfo;
import com.hpb.bc.entity.HpbInstantPrice;
import com.hpb.bc.entity.result.Result;
import com.hpb.bc.entity.result.ResultCode;
import com.hpb.bc.example.AddrsExample;
import com.hpb.bc.mapper.AddrsMapper;
import com.hpb.bc.mapper.ContractErcStandardInfoMapper;
import com.hpb.bc.mapper.HpbInstantPriceMapper;
import com.hpb.bc.service.*;
import com.hpb.bc.util.AddressBalanceHelper;
import com.hpb.bc.util.Erc20Helper;
import io.hpb.web3.crypto.WalletUtils;
import io.hpb.web3.protocol.admin.Admin;
import io.hpb.web3.protocol.core.DefaultBlockParameterName;
import io.hpb.web3.protocol.core.methods.response.HpbGetCode;
import io.hpb.web3.protocol.core.methods.response.HpbGetTransactionCount;
import io.hpb.web3.utils.Convert;
import io.hpb.web3.utils.Numeric;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Service
public class AddrsServiceImpl implements AddrsService {

    public Logger log = LoggerFactory.getLogger(this.getClass());
    @Autowired
    AddrsMapper addrsMapper;
    @Autowired
    HpbInstantPriceMapper hpbInstantPriceMapper;
    @Autowired
    Admin admin;
    @Autowired
    Erc20Helper erc20Helper;
    @Autowired
    RedisExpireTimeProperties redisExpireTimeProperties;


    @Autowired
    EthProperties ethProperties;

    @Autowired
    AsyncGetBalanceOrderTask asyncGetBalanceOrderTask;

    @Autowired
    TransactionService transactionService;

    @Autowired
    AddressBalanceHelper addressBalanceHelper;

    @Autowired
    RedisService redisService;

    @Autowired
    Web3Properties web3Properties;


    @Autowired
    BlockFacetService blockFacetService;

    @Autowired
    ContractErcStandardInfoService contractErcStandardInfoService;

    @Autowired
    ContractErcStandardInfoMapper contractErcStandardInfoMapper;


    @Override
    public Result<PageInfo<Addrs>> getPageAddrs(Addrs addrs, int pageNum, int pageSize) throws Exception {
        PageHelper.startPage(pageNum, pageSize);
        AddrsExample addrsExample = new AddrsExample();
        AddrsExample.Criteria criteria = addrsExample.createCriteria();
        criteria.andBalanceGreaterThan(BigDecimal.ZERO);
        addrsExample.setOrderByClause(" balance desc ");
        List<Addrs> list = addrsMapper.selectByAddrs(addrs);
        PageInfo<Addrs> pageInfo = new PageInfo<Addrs>(list);
        list = pageInfo.getList();
        for (int index = 0; index < list.size(); index++) {
            Addrs add = list.get(index);
            if (add.getBalance() != null) {
                BigDecimal banlance = Convert.fromWei(add.getBalance().toString(), Convert.Unit.HPB);
                BigDecimal rate = banlance.divide(BigDecimal.valueOf(100000000 / 100));
                add.setPercentRate(rate.setScale(4, BigDecimal.ROUND_HALF_EVEN).stripTrailingZeros().toPlainString() + "%");
                add.setBalanceStr(banlance.setScale(8, BigDecimal.ROUND_HALF_EVEN).toPlainString() + "");
            } else {
                add.setPercentRate("0%");
                add.setBalanceStr("0");
            }

            int order = 0;
            if (pageNum > 0) {
                order = (pageNum - 1) * pageSize + index + 1;
            } else {
                order = pageNum * pageSize + index + 1;
            }
            add.setAssetRankingOrder(String.valueOf(order));
        }

        pageInfo.setList(list);
        return new Result<PageInfo<Addrs>>(ResultCode.SUCCESS, pageInfo);
    }

    @Override
    public Addrs getAddrsByAddress(String address) {

        Addrs addrs = addrsMapper.selectByPrimaryKey(address);
        if (addrs == null) {
            return null;
        }
        BigInteger balance = addressBalanceHelper.getBalanceByAddress(address);
        if (balance != null) {
            addrs.setBalance(balance);
            BigDecimal balanceHpb = Convert.fromWei(String.valueOf(balance), Convert.Unit.HPB);
            addrs.setBalanceStr(String.valueOf(balanceHpb));
        }
        return addrs;
    }


    @Override
    public Map getAddrsDetailInfo(String address) {
        Map map = new HashMap();
        if (!WalletUtils.isValidAddress(address)) {
            map.put("msg", "非法的账号");
            return map;
        }
        Addrs addrs = this.getAddrsByAddress(address);
        HpbInstantPrice hpbInstantPrice = hpbInstantPriceMapper.selectByPrimaryKey(Integer.valueOf(1));
        BigInteger nonce = BigInteger.ZERO;
        try {
            HpbGetTransactionCount transactionCount = admin.hpbGetTransactionCount(address, DefaultBlockParameterName.LATEST).send();
            nonce = Numeric.decodeQuantity(transactionCount.getResult());
            map.put("nonce", nonce);
        } catch (IOException e) {
            log.error("  账户{}hpbGetTransactionCount e == {} ", address, e);
            e.printStackTrace();
        }

        List<Addrs> allOrderList = addrsMapper.selectAddrsOrderByBalanceDesc();

        if (addrs == null) {
            addrs = new Addrs();
            addrs.setAddress(address);
            addrs.setFromCount(0L);
            addrs.setToCount(0L);

            BigInteger balance = addressBalanceHelper.getBalanceByAddress(address);
            if (balance != null) {
                addrs.setBalance(balance);
                BigDecimal balanceHpbValue = Convert.fromWei(String.valueOf(balance), Convert.Unit.HPB);
                addrs.setBalanceStr(balanceHpbValue.setScale(8, BigDecimal.ROUND_HALF_EVEN).toPlainString() + "");
            }
            if (balance.compareTo(BigInteger.ZERO) == 0) {
                addrs.setAssetRankingOrder(String.valueOf(0));
                addrs.setPercentRate("0%");
            }

            map.put("addrs", addrs);
            map.put("hpbInstantPrice", hpbInstantPrice);
            return map;
        }
        BigInteger baValue = BigInteger.ZERO;
        if (addrs.getBalance() != null) {
            baValue = addrs.getBalance();
        }
        BigDecimal balanceValue = Convert.fromWei(baValue + "", Convert.Unit.HPB);
        addrs.setBalanceStr(balanceValue.setScale(8, BigDecimal.ROUND_HALF_EVEN).toPlainString() + "");
        for (int x = 0; x < allOrderList.size(); x++) {
            Addrs addrs1 = allOrderList.get(x);
            if (address.equals(addrs1.getAddress())) {
                int order = x + 1;
                addrs.setAssetRankingOrder(String.valueOf(order));
            }
        }

        BigDecimal rate = balanceValue.divide(BigDecimal.valueOf(100000000 / 100));
        addrs.setPercentRate(rate.setScale(4, BigDecimal.ROUND_HALF_EVEN).toPlainString() + "%");
        map.put("addrs", addrs);
        map.put("hpbInstantPrice", hpbInstantPrice);
        return map;
    }

    @Override
    public int getAddrCount() {
        return addrsMapper.selectAddrCount();
    }

    @Override
    public Map<String, Object> getAddressOverview() {
        Map<String, Object> map = new HashMap<>();
        String totalAddressAmount = addrsMapper.selectAddrCount() + "";
        BigDecimal totalSupply = addressBalanceHelper.getTotalSupply();
        map.put("totalAddressAmount", totalAddressAmount);
        map.put("totalAssetsAmount", totalSupply);
        map.put("mainNetAssetsAmount", "");
        return map;
    }

    @Override
    public BigDecimal queryTxAmountByFromAccount(String address) {
        try {
            BigInteger blockNumber = admin.hpbBlockNumber().send().getBlockNumber();
            BigDecimal fromTotalAmount = transactionService.getTransactionAmountByFromAccount(address, blockNumber);
            log.info("blockNumber [{}],fromTotalAmount [{}]", blockNumber, fromTotalAmount);
            return fromTotalAmount;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return BigDecimal.ZERO;
    }

    @Override
    public BigDecimal queryTxAmountByToAccount(String address) {
        try {
            BigInteger blockNumber = admin.hpbBlockNumber().send().getBlockNumber();
            BigDecimal fromTotalAmount = transactionService.getTransactionAmountByToAccount(address, blockNumber);
            log.info("blockNumber [{}],fromTotalAmount [{}]", blockNumber, fromTotalAmount);
            return fromTotalAmount;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return BigDecimal.ZERO;
    }

    @Override
    public boolean checkContractAddress(String contractAddress) {
        return isContractAddress(contractAddress);
    }

    private boolean isContractAddress(String contractAddress) {
        boolean isContractFlag = blockFacetService.isContractAddress(contractAddress);
        if (isContractFlag) {
            return isContractFlag;
        }
        ContractErcStandardInfo contractErcStandardInfo = contractErcStandardInfoMapper.selectByContractAddress(contractAddress);
        if (contractErcStandardInfo != null) {
            return true;
        }
        return checkByContractValid(contractAddress);
    }

    private boolean checkByContractValid(String contractAddress) {
        try {
            HpbGetCode hpbGetCode = admin.hpbGetCode(contractAddress, DefaultBlockParameterName.LATEST).sendAsync().get(2, TimeUnit.MINUTES);
            if (hpbGetCode.getCode() != null && !BcConstant.HX_PREFIX.equalsIgnoreCase(hpbGetCode.getCode())) {
                return true;
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public Map<String, Object> checkErcContractAddress(String contractAddress) {
        Map<String, Object> map = new HashMap<>();
        ContractErcStandardInfo contractErcStandardInfo = contractErcStandardInfoMapper.selectByContractAddress(contractAddress);
        if (contractErcStandardInfo != null) {
            map.put("contractType", contractErcStandardInfo.getContractType());
        } else {
            map.put("contractType", BcConstant.CONTRACT_NOT_TYPE);
        }
        return map;
    }

    @Override
    public Result<PageInfo<Addrs>> getPageContractAddrs(Addrs addrs, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        AddrsExample addrsExample = new AddrsExample();
        addrsExample.setOrderByClause(" start_block desc ");
        List<Addrs> list = addrsMapper.selectByContractAddrs(addrs);
        PageInfo<Addrs> pageInfo = new PageInfo<Addrs>(list);
        list = pageInfo.getList();
        for (int index = 0; index < list.size(); index++) {
            Addrs add = list.get(index);
            if (add.getBalance() != null) {
                BigDecimal banlance = Convert.fromWei(add.getBalance().toString(), Convert.Unit.HPB);
                BigDecimal rate = banlance.divide(BigDecimal.valueOf(100000000 / 100));
                add.setPercentRate(rate.setScale(4, BigDecimal.ROUND_HALF_EVEN).stripTrailingZeros().toPlainString() + "%");
                add.setBalanceStr(banlance.setScale(8, BigDecimal.ROUND_HALF_EVEN).toPlainString() + "");
            } else {
                add.setPercentRate("0%");
                add.setBalanceStr("0");
            }

            int order = 0;
            if (pageNum > 0) {
                order = (pageNum - 1) * pageSize + index + 1;
            } else {
                order = pageNum * pageSize + index + 1;
            }
            add.setAssetRankingOrder(String.valueOf(order));
        }

        pageInfo.setList(list);
        return new Result<PageInfo<Addrs>>(ResultCode.SUCCESS, pageInfo);
    }
}
