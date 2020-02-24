package com.hpb.bc.async;

import com.hpb.bc.entity.ContractErcStandardInfo;
import com.hpb.bc.entity.ContractInfo;
import com.hpb.bc.mapper.ContractErcStandardInfoMapper;
import com.hpb.bc.util.Erc20Helper;
import io.hpb.web3.protocol.admin.Admin;
import io.hpb.web3.protocol.core.DefaultBlockParameterName;
import io.hpb.web3.protocol.core.methods.response.HpbGetBalance;
import io.hpb.web3.utils.Convert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static io.hpb.web3.utils.Convert.Unit.HPB;

@Component
public class AsyncErc20Task {

    public static final int TIME_OUT = 2;

    @Autowired
    Admin admin;

    public Logger log = LoggerFactory.getLogger(AsyncErc20Task.class);

    @Async
    public void getBalanceByAddress(CountDownLatch countDownLatch, ContractInfo contractInfo, List<ContractInfo> resultContractInfosList) {
        try {
            HpbGetBalance hpbGetBalance = admin.hpbGetBalance(contractInfo.getContractAddr(), DefaultBlockParameterName.LATEST).sendAsync().get(2, TimeUnit.MINUTES);
            if (hpbGetBalance != null) {
                contractInfo.setBalance(Convert.fromWei(String.valueOf(hpbGetBalance.getBalance()), HPB));
            } else {
                contractInfo.setBalance(BigDecimal.ZERO);
            }
            resultContractInfosList.add(contractInfo);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            countDownLatch.countDown();
        }
    }

    public BigDecimal getHpbBalance(String address) {

        try {
            HpbGetBalance hpbGetBalance = admin.hpbGetBalance(address, DefaultBlockParameterName.LATEST).sendAsync().get(2, TimeUnit.MINUTES);
            if (hpbGetBalance != null) {
                return Convert.fromWei(String.valueOf(hpbGetBalance.getBalance()), HPB);
            } else {
                return BigDecimal.ZERO;
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
        return BigDecimal.ZERO;
    }

}
