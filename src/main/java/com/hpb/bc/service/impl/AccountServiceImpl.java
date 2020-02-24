package com.hpb.bc.service.impl;

import com.hpb.bc.constant.ApiConstant;
import com.hpb.bc.entity.Addrs;
import com.hpb.bc.entity.BlockAddrs;
import com.hpb.bc.entity.BlocksMinedInfo;
import com.hpb.bc.exception.ApiException;
import com.hpb.bc.model.TransactionDetailModel;
import com.hpb.bc.page.HpbPageHelper;
import com.hpb.bc.service.AccountService;
import com.hpb.bc.service.AddrsService;
import com.hpb.bc.util.BasicUtils;
import io.hpb.web3.protocol.admin.Admin;
import io.hpb.web3.protocol.core.DefaultBlockParameterName;
import io.hpb.web3.protocol.core.methods.response.Transaction;
import io.hpb.web3.protocol.core.methods.response.TransactionReceipt;
import io.hpb.web3.utils.Convert;
import io.hpb.web3.utils.Numeric;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created with IDEA
 *
 * @ Author：wangm
 * @ Date：Created in  2018/12/11 14:21
 * @ Description：账户服务实现类
 */
@Service
public class AccountServiceImpl implements AccountService {
    @Autowired
    private Admin admin;
    @Autowired
    private HpbPageHelper hpbPageHelper;
    @Autowired
    private BlockAddrsServiceImpl blockAddrsService;
    @Autowired
    private AddrsService addrsService;

    @Override
    public String getSingleBalanceByAddress(Map<String, String> param) {
        String balanceStr = "";
        try {
            String address = param.get(ApiConstant.PARAM_ADDRESS);
            BigInteger balance = admin.hpbGetBalance(address, DefaultBlockParameterName.LATEST).send().getBalance();
            BigDecimal balanceWeiAmt = Convert.fromWei(balance.toString(), Convert.Unit.HPB);
            balanceStr = balanceWeiAmt.toString();
        } catch (IOException e) {
            throw new ApiException(e.getMessage(), e);
        }
        return balanceStr;
    }

    @Override
    public List<Map<String, String>> getBalanceMultiByMultipleAddresses(Map<String, String> param) {
        List<Map<String, String>> addressBalanceList = new ArrayList<Map<String, String>>();
        try {
            String address = param.get(ApiConstant.PARAM_ADDRESS);
            String[] addressArr = address.split(",");
            for (int i = 0; i < addressArr.length; i++) {
                BigInteger balance = admin.hpbGetBalance(addressArr[i], DefaultBlockParameterName.LATEST).send().getBalance();
                BigDecimal balanceWeiAmt = Convert.fromWei(balance.toString(), Convert.Unit.HPB);
                Map<String, String> balanceMap = new TreeMap<>();
                balanceMap.put(ApiConstant.ACCOUNT, addressArr[i]);
                balanceMap.put(ApiConstant.BALANCE, balanceWeiAmt.toString());
                addressBalanceList.add(balanceMap);
            }
        } catch (IOException e) {
            throw new ApiException(e.getMessage(), e);
        }
        return addressBalanceList;
    }

    @Override
    public List<Map<String, String>> getNormalTransactionsByAddress1(Map<String, String> param) {
        List<Map<String, String>> resMapList = new ArrayList<>();
        try {
            String address = param.get(ApiConstant.PARAM_ADDRESS);
            long startBlock = Long.valueOf(param.get(ApiConstant.PARAM_START_BLOCK));
            Addrs addrs = addrsService.getAddrsByAddress(address);
            if (addrs != null) {
                Long lastestBlock = getActualMaxBlockNum(addrs, param);
                if (startBlock >= lastestBlock) {
                    return null;
                }
                List<BlockAddrs> blockAddrsList = blockAddrsService.getBlockAddrsListFromStartBlockToEndBlockByAddrs(addrs, lastestBlock);
                BigInteger x = BigInteger.valueOf(addrs.getNumber());//地址
                String s = Numeric.toHexStringNoPrefix(x);
                List<BlockAddrs> finalList = blockAddrsList.stream().filter(e -> Arrays.asList(e.getAddrs().split(",")).contains(s)).collect(Collectors.toList());
                List<Transaction> transactionList = hpbPageHelper.getTransactionInfosFromBlockAddrsByToAddress(finalList, address);
                List<TransactionDetailModel> txtModels = hpbPageHelper.getTransactionDetailModelFromTransaction(transactionList);
                txtModels.forEach(transactionDetailModel -> {
                    Map<String, String> resMap = BasicUtils.convertClassToMap(transactionDetailModel);
                    resMapList.add(resMap);
                });
            }
        } catch (Exception e) {
            throw new ApiException(e.getMessage(), e);
        }
        return resMapList;
    }

    @Override
    public List<Map<String, String>> getNormalTransactionsByAddressAndPage(Map<String, String> param) {
        List<Map<String, String>> resMapList = new ArrayList<>();
        try {
            String address = param.get(ApiConstant.PARAM_ADDRESS);
            long startBlock = Long.valueOf(param.get(ApiConstant.PARAM_START_BLOCK));
            Long currentPage = Long.valueOf(param.get(ApiConstant.PARAM_PAGE));
            Long pageSize = Long.valueOf(param.get(ApiConstant.PARAM_PAGESIZE));
            Addrs addrs = addrsService.getAddrsByAddress(address);
            if (addrs != null) {
                Long lastestBlock = getActualMaxBlockNum(addrs, param);
                if (startBlock >= lastestBlock) {
                    return null;
                }
                List<BlockAddrs> blockAddrsList = blockAddrsService.getBlockAddrsListFromStartBlockToEndBlockByAddrs(addrs, lastestBlock);

                BigInteger x = BigInteger.valueOf(addrs.getNumber());//地址
                String s = Numeric.toHexStringNoPrefix(x);
                List<BlockAddrs> finalList = blockAddrsList.stream().filter(e -> Arrays.asList(e.getAddrs().split(",")).contains(s)).collect(Collectors.toList());
                List<Transaction> transactionList = hpbPageHelper.getTransactionInfosFromBlockAddrsByToAddress(finalList, address);
                List<TransactionDetailModel> txtModels = hpbPageHelper.formatTransactionPageInfo(currentPage, pageSize, transactionList);
                txtModels.forEach(transactionDetailModel -> {
                    Map<String, String> resMap = BasicUtils.convertClassToMap(transactionDetailModel);
                    resMapList.add(resMap);
                });
            }
        } catch (Exception e) {
            throw new ApiException(e.getMessage(), e);
        }
        return resMapList;
    }

    @Override
    public TransactionReceipt getTransactionsByTransactionHash(Map<String, String> param) {
        TransactionReceipt receipt = null;
        try {
            receipt = hpbPageHelper.getTransactionReceipt(param.get(ApiConstant.PARAM_TXHASH));
        } catch (Exception e) {
            throw new ApiException(e.getMessage(), e);
        }
        return receipt;
    }

    @Override
    public List<Map<String, String>> getTransactionsByERC20TokenByAddress(Map<String, String> param) {
        List<Map<String, String>> resMapList = new ArrayList<>();
        try {
            long startBlock = Long.valueOf(param.get(ApiConstant.PARAM_START_BLOCK));
            String address = param.get(ApiConstant.PARAM_ADDRESS);
            Addrs addrs = addrsService.getAddrsByAddress(address);
            if (addrs != null) {
                Long lastestBlock = getActualMaxBlockNum(addrs, param);
                if (startBlock >= lastestBlock) {
                    return null;
                }
                List<BlockAddrs> blockAddrsList = blockAddrsService.getBlockAddrsListFromStartBlockToEndBlockByAddrs(addrs, lastestBlock);

                BigInteger x = BigInteger.valueOf(addrs.getNumber());//地址
                String s = Numeric.toHexStringNoPrefix(x);
                List<BlockAddrs> finalList = blockAddrsList.stream().filter(e -> Arrays.asList(e.getAddrs().split(",")).contains(s)).collect(Collectors.toList());
                List<Transaction> transactionList = hpbPageHelper.getTransactionInfosFromBlockAddrsByToAddress(finalList, address);
                List<TransactionDetailModel> txtModels = hpbPageHelper.getTransactionDetailModelFromTransaction(transactionList);
                txtModels.forEach(transactionDetailModel -> {
                    Map<String, String> resMap = BasicUtils.convertClassToMap(transactionDetailModel);
                    resMapList.add(resMap);
                });
            }
        } catch (Exception e) {
            throw new ApiException(e.getMessage(), e);
        }
        return resMapList;
    }

    @Override
    public List<Map<String, String>> getTransactionsByERC20TokenByContractAddressAndPage(Map<String, String> param) {
        List<Map<String, String>> resMapList = new ArrayList<>();
        try {
            String address = param.get(ApiConstant.PARAM_CONTRACTADDRESS);
            Long currentPage = Long.valueOf(param.get(ApiConstant.PARAM_PAGE));
            Long pageSize = Long.valueOf(param.get(ApiConstant.PARAM_PAGESIZE));
            Addrs addrs = addrsService.getAddrsByAddress(address);
            if (addrs != null) {
                Long lastestBlock = addrs.getLastestBlock();
                List<BlockAddrs> blockAddrsList = blockAddrsService.getBlockAddrsListFromStartBlockToEndBlockByAddrs(addrs, lastestBlock);
                BigInteger x = BigInteger.valueOf(addrs.getNumber());//地址
                String s = Numeric.toHexStringNoPrefix(x);
                List<BlockAddrs> finalList = blockAddrsList.stream().filter(e -> Arrays.asList(e.getAddrs().split(",")).contains(s)).collect(Collectors.toList());
                List<Transaction> transactionList = hpbPageHelper.getTransactionInfosFromBlockAddrsByToAddress(finalList, address);
                List<TransactionDetailModel> txtModels = hpbPageHelper.formatTransactionPageInfo(currentPage, pageSize, transactionList);
                txtModels.forEach(transactionDetailModel -> {
                    Map<String, String> resMap = BasicUtils.convertClassToMap(transactionDetailModel);
                    resMapList.add(resMap);
                });
            }
        } catch (Exception e) {
            throw new ApiException(e.getMessage(), e);
        }
        return resMapList;
    }

    @Override
    public List<Map<String, String>> getBlocksByMinedAddress(Map<String, String> param) {
        List<Map<String, String>> resMapList = new ArrayList<>();
        try {
            String address = param.get(ApiConstant.PARAM_ADDRESS);
            Addrs addrs = addrsService.getAddrsByAddress(address);
            if (addrs != null) {
                Long lastestBlock = addrs.getLastestBlock();
                List<BlockAddrs> blockAddrsList = blockAddrsService.getBlockAddrsListFromStartBlockToEndBlockByAddrs(addrs, lastestBlock);
                BigInteger x = BigInteger.valueOf(addrs.getNumber());//地址
                String s = Numeric.toHexStringNoPrefix(x);
                List<BlockAddrs> finalList = blockAddrsList.stream().filter(e -> Arrays.asList(e.getAddrs().split(",")).contains(s)).collect(Collectors.toList());
                List<Transaction> transactionList = hpbPageHelper.getTransactionInfosFromBlockAddrsByToAddress(finalList, address);
                List<TransactionDetailModel> txtModels = hpbPageHelper.getTransactionDetailModelFromTransaction(transactionList);
                if (txtModels != null && txtModels.size() > 0) {
                    for (TransactionDetailModel t : txtModels) {
                        BlocksMinedInfo blocksMinedInfo = new BlocksMinedInfo();
                        blocksMinedInfo.setBlockNumber(t.getBlockNumber().toString());
                        blocksMinedInfo.setTimeStamp(t.getTransactionTimestamp());
                        blocksMinedInfo.setBlockReward("");
                        Map<String, String> map = BasicUtils.convertClassToMap(blocksMinedInfo);
                        resMapList.add(map);
                    }
                }
            }
        } catch (Exception e) {
            throw new ApiException(e.getMessage(), e);
        }
        return resMapList;
    }

    @Override
    public List<Map<String, String>> getBlocksByMinedAddressAndPage(Map<String, String> param) {
        List<Map<String, String>> resMapList = new ArrayList<>();
        try {
            String address = param.get(ApiConstant.PARAM_ADDRESS);
            Long currentPage = Long.valueOf(param.get(ApiConstant.PARAM_PAGE));
            Long pageSize = Long.valueOf(param.get(ApiConstant.PARAM_PAGESIZE));
            Addrs addrs = addrsService.getAddrsByAddress(address);
            if (addrs != null) {
                Long lastestBlock = addrs.getLastestBlock();
                List<BlockAddrs> blockAddrsList = blockAddrsService.getBlockAddrsListFromStartBlockToEndBlockByAddrs(addrs, lastestBlock);
                BigInteger x = BigInteger.valueOf(addrs.getNumber());
                String s = Numeric.toHexStringNoPrefix(x);
                List<BlockAddrs> finalList = blockAddrsList.stream().filter(e -> Arrays.asList(e.getAddrs().split(",")).contains(s)).collect(Collectors.toList());
                List<Transaction> transactionList = hpbPageHelper.getTransactionInfosFromBlockAddrsByToAddress(finalList, address);
                List<TransactionDetailModel> txtModels = hpbPageHelper.formatTransactionPageInfo(currentPage, pageSize, transactionList);
                txtModels.forEach(t -> {
                    BlocksMinedInfo blocksMinedInfo = new BlocksMinedInfo();
                    blocksMinedInfo.setBlockNumber(t.getBlockNumber().toString());
                    blocksMinedInfo.setTimeStamp(t.getTransactionTimestamp());
                    blocksMinedInfo.setBlockReward("");
                    Map<String, String> map = BasicUtils.convertClassToMap(blocksMinedInfo);
                    resMapList.add(map);
                });
            }
        } catch (Exception e) {
            throw new ApiException(e.getMessage(), e);
        }
        return resMapList;
    }

    private long getActualMaxBlockNum(Addrs addrs, Map<String, String> map) {
        try {
            long endBlock = 0;
            if (addrs == null) {
                return 0;
            }
            String endBlockStr = map.get(ApiConstant.PARAM_END_BLOCK);
            BigInteger blockNumber = admin.hpbBlockNumber().send().getBlockNumber();
            if (blockNumber.longValue() > addrs.getLastestBlock() && blockNumber.longValue() > Long.valueOf(endBlockStr)
                    && addrs.getLastestBlock() > Long.valueOf(endBlockStr)) {
                endBlock = Long.valueOf(endBlockStr);
            } else if (blockNumber.longValue() > addrs.getLastestBlock() && blockNumber.longValue() > Long.valueOf(endBlockStr)
                    && Long.valueOf(endBlockStr) > addrs.getLastestBlock()) {
                endBlock = addrs.getLastestBlock();
            } else if (addrs.getLastestBlock() > blockNumber.longValue() && blockNumber.longValue() > Long.valueOf(endBlockStr)) {
                endBlock = blockNumber.longValue();
            }
            return endBlock;
        } catch (IOException e) {
            throw new ApiException(e.getMessage(), e);
        }
    }
}
