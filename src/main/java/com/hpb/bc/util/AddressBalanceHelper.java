package com.hpb.bc.util;

import com.hpb.bc.async.AsyncGetBalanceOrderTask;
import com.hpb.bc.configure.EthProperties;
import com.hpb.bc.configure.RedisExpireTimeProperties;
import com.hpb.bc.constant.RedisKeyConstant;
import com.hpb.bc.contracts.NodeVoteResultHelper;
import com.hpb.bc.mapper.AddrsMapper;
import com.hpb.bc.service.RedisService;
import io.hpb.web3.protocol.admin.Admin;
import io.hpb.web3.utils.Convert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@Component
public class AddressBalanceHelper {

    public Logger log = LoggerFactory.getLogger(AddressBalanceHelper.class);
    @Autowired
    AsyncGetBalanceOrderTask asyncGetBalanceOrderTask;
    @Autowired
    Admin admin;

    @Autowired
    AddrsMapper addrsMapper;

    @Autowired
    EthProperties ethProperties;

    @Autowired
    RedisService redisService;

    @Autowired
    RedisExpireTimeProperties redisExpireTimeProperties;

    @Autowired
    NodeVoteResultHelper nodeVoteResultHepler;

    public BigDecimal getBalanceOfNodeWhichWithoutTransaction() {
        List<String> nodeAddressListWithoutTx = new ArrayList<>();
        nodeAddressListWithoutTx = addrsMapper.selectNodeAddressListWithoutTransaction();

        if (!nodeAddressListWithoutTx.isEmpty()) {
            log.debug("nodeAddressListWithoutTx.size [{}]  ", nodeAddressListWithoutTx.size());
            List<BigInteger> balanceList = Collections.synchronizedList(new ArrayList<BigInteger>());
            try {
                CountDownLatch countDownLatch = new CountDownLatch(nodeAddressListWithoutTx.size());
                for (int i = 0; i < nodeAddressListWithoutTx.size(); i++) {
                    String address = nodeAddressListWithoutTx.get(i);
                    asyncGetBalanceOrderTask.getBalanceOfAddress(countDownLatch, balanceList, address);
                }
                countDownLatch.await(2, TimeUnit.MINUTES);

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            BigInteger totalBalance = BigInteger.ZERO;
            for (BigInteger b : balanceList) {
                totalBalance = totalBalance.add(b);
            }
            return Convert.fromWei(String.valueOf(totalBalance), Convert.Unit.HPB);
        }
        return BigDecimal.ZERO;
    }


    public BigDecimal getTotalSupply() {
        try {
            BigDecimal totalSupply = BigDecimal.ZERO;
            Object totalSupplyObject = redisService.getObject(RedisKeyConstant.TotalSupply);
            if (totalSupplyObject == null) {
                String totalAssetsAmount = addrsMapper.selectTotalAssetsAmount();

                BigDecimal totalAssetsAmountValue = Convert.fromWei(totalAssetsAmount, Convert.Unit.HPB);

                BigDecimal totalBalanceOfNodeWithTx = this.getBalanceOfNodeWhichWithoutTransaction();

                totalSupply = totalBalanceOfNodeWithTx.add(totalAssetsAmountValue).setScale(8, BigDecimal.ROUND_HALF_EVEN);
                if (totalSupply.compareTo(BigDecimal.ZERO) > 0) {
                    redisService.saveWithExpireTime(RedisKeyConstant.TotalSupply, totalSupply, redisExpireTimeProperties.getMinutesNumber(), TimeUnit.MINUTES);
                }else {
                    totalSupply = BigDecimal.ZERO;
                }
            } else {
                totalSupply = BigDecimal.valueOf((Double) totalSupplyObject);
            }
            return totalSupply;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return BigDecimal.ZERO;
    }


    public BigInteger getBalanceByAddress(String address) {
        try {
            List<BigInteger> bigIntegerList = Collections.synchronizedList(new ArrayList<BigInteger>());
            CountDownLatch countDownLatch = new CountDownLatch(1);
            asyncGetBalanceOrderTask.getBalanceByAddress(address, countDownLatch, bigIntegerList);
            countDownLatch.await(2, TimeUnit.MINUTES);
            return bigIntegerList.get(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
