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

import com.hpb.bc.constant.ApiConstant;
import com.hpb.bc.entity.result.Result;
import com.hpb.bc.entity.result.ResultCode;
import com.hpb.bc.exception.ApiException;
import com.hpb.bc.exception.ApikeyException;
import com.hpb.bc.service.*;
import com.hpb.bc.util.RequestHandleUtil;
import io.hpb.web3.protocol.core.methods.response.TransactionReceipt;
import io.swagger.annotations.Api;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ApiFacadeServiceImpl implements ApiFacadeService {

    @Autowired
    HpbInstantPriceService hpbInstantPriceService;
    @Autowired
    private AccountServiceImpl accountService;
    @Autowired
    private StatisticsServiceImpl statisticsService;

    public Logger log = LoggerFactory.getLogger(this.getClass());

    @Override
    public Object doExcute(Map<String, String> map) throws Exception {
        Object obj = null;
        try {
            log.debug("doExcute newCountDownLatch:");
            Map<String, String> missMap = RequestHandleUtil.commonCheckParamsIsVaild(map);
            if (missMap.size() > 0) {
                throw new ApikeyException(400, ApiConstant.MISSINGPARAMETER, mandatoryMissing(missMap));
            }

            log.debug("doExcute newCountDownLatch: module [{}],action [{}]", map.get(ApiConstant.MODULE).toString(), map.get(ApiConstant.ACTION).toString());

            String module = map.get(ApiConstant.MODULE).toString();
            String action = map.get(ApiConstant.ACTION).toString();

            // 1. Get Ether Balance for a single Address
            if (ApiConstant.MODULE_ACCOUNT.equals(module) && ApiConstant.ACTION_BALANCE.equals(action)) {
                String balance = accountService.getSingleBalanceByAddress(map);
                obj = balance;
            }
            // 2.Get Ether Balance for multiple Addresses in a single call
            else if (ApiConstant.MODULE_ACCOUNT.equals(module) && ApiConstant.ACTION_BALANCEMULTI.equals(action)) {
                List<Map<String, String>> resBalanceMap = accountService.getBalanceMultiByMultipleAddresses(map);
                obj = resBalanceMap;
            }
            //3.1 Get a list of 'Normal'/'Internal' Transactions By Address
            else if (module.equals(ApiConstant.MODULE_ACCOUNT)
                    && (ApiConstant.ACTION_TXLIST.equals(action) || ApiConstant.ACTION_TXLIST_INTERNAL.equals(action))
                    && !map.containsKey(ApiConstant.PARAM_PAGE) && !map.containsKey(ApiConstant.PARAM_TXHASH)) {
                List<Map<String, String>> resList = accountService.getNormalTransactionsByAddress1(map);
                obj = resList;
            }
            //3.2 Get a list of 'Normal'/'Internal' Transactions By Address and Page
            else if (module.equals(ApiConstant.MODULE_ACCOUNT)
                    && (ApiConstant.ACTION_TXLIST.equals(action) || ApiConstant.ACTION_TXLIST_INTERNAL.equals(action))
                    && map.containsKey(ApiConstant.PARAM_PAGE) && !map.containsKey(ApiConstant.PARAM_TXHASH)) {
                List<Map<String, String>> resList = accountService.getNormalTransactionsByAddressAndPage(map);
                obj = resList;
            }

            //4.Get "Internal Transactions" by Transaction Hash
            else if (ApiConstant.MODULE_ACCOUNT.equals(module) && ApiConstant.ACTION_TXLIST_INTERNAL.equals(action)
                    && map.containsKey(ApiConstant.PARAM_TXHASH)) {
                TransactionReceipt receipt = accountService.getTransactionsByTransactionHash(map);
                obj = receipt;
            }

            //5.1Get a list of "ERC20 - Token Transfer Events" by Address
            else if (ApiConstant.MODULE_ACCOUNT.equals(module) && ApiConstant.ACTION_TOKENTX.equals(action)
                    && map.containsKey(ApiConstant.PARAM_ADDRESS) && !map.containsKey(ApiConstant.PARAM_CONTRACTADDRESS)
                    && !map.containsKey(ApiConstant.PARAM_PAGE)) {
                List<Map<String, String>> resList = accountService.getTransactionsByERC20TokenByAddress(map);
                obj = resList;
            }

            //5.1Get a list of "ERC20 - Token Transfer Events" by contractaddress and Page
            else if (ApiConstant.MODULE_ACCOUNT.equals(module) && ApiConstant.ACTION_TOKENTX.equals(action)
                    && !map.containsKey(ApiConstant.PARAM_ADDRESS) && map.containsKey(ApiConstant.PARAM_CONTRACTADDRESS)
                    && map.containsKey(ApiConstant.PARAM_PAGE)) {
                List<Map<String, String>> resList = accountService.getTransactionsByERC20TokenByContractAddressAndPage(map);
                obj = resList;
            }

            //6.Get list of Blocks Mined by Address
            else if (ApiConstant.MODULE_ACCOUNT.equals(module) && ApiConstant.ACTION_GETMINEDBLOCK.equals(action)
                    && !map.containsKey(ApiConstant.PARAM_PAGE)) {
                List<Map<String, String>> resList = accountService.getBlocksByMinedAddress(map);
                obj = resList;
            }

            //6.2 Get list of Blocks Mined by Address and Page
            else if (ApiConstant.MODULE_ACCOUNT.equals(module) && ApiConstant.ACTION_GETMINEDBLOCK.equals(action)
                    && map.containsKey(ApiConstant.PARAM_PAGE)) {
                List<Map<String, String>> resList = accountService.getBlocksByMinedAddressAndPage(map);
                obj = resList;
            }

            //7 Get HPB LastPrice Price
            else if (ApiConstant.MODULE_STATS.equals(module) && ApiConstant.ACTION_HPBPRICE.equals(action)) {
                Map<String, String> resMap = statisticsService.getInstantPrice();
                obj = resMap;
            }
        } catch (ApiException e) {
            Map<String, String> treeMap = new TreeMap<>(map);
            log.error("【************* ApiFacadeServiceImpl exception, error message {}, request parameter {}:{}************】",
                    e.getMessage(), treeMap.keySet().toArray(), treeMap.values().toArray());
        }
        return obj;
    }

    private String mandatoryMissing(Map<String, String> missMap) {
        String errorMsg = " error parameter key:value====>>>>>";
        for (String str : missMap.keySet()) {
            errorMsg += str + ":" + missMap.get(str);
        }
        log.error("error parameter " + errorMsg);
        return " error parameter " + errorMsg;
    }
}
