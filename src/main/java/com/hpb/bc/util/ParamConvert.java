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

package com.hpb.bc.util;

import java.math.BigInteger;
import java.util.Map;

import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;

import com.hpb.bc.constant.BcConstant;
import com.hpb.bc.entity.ParamInfo;
import io.hpb.web3.utils.Convert;

public class ParamConvert {
    private static final String VALUE = "5";
    private static final String GASLIMIT_PRICE = "500000";
    private static final String GASLIMIT_DEFAULT = "1000000";
    private final BigInteger scale = new BigInteger("100000");
    private BigInteger gasLimit;
    /*
     * 默认gasPrice即50GWEI
     */
    private BigInteger gasPrice;

    private BigInteger value;

    private BigInteger amountUsed;

    public BigInteger getAmountUsed() {
        return amountUsed;
    }

    public void setValue(BigInteger value) {
        this.value = value;
    }

    public BigInteger getGasLimit() {
        return gasLimit;
    }

    public BigInteger getGasPrice() {
        return gasPrice;
    }

    public BigInteger getValue() {
        return value;
    }

    public ParamConvert(ParamInfo paramInfo, Map<String, Object> param) {
        if (paramInfo == null) {
            paramInfo = new ParamInfo();
        }
        String gasLimitStr = paramInfo.getGasLimit();
        if (StringUtils.isBlank(gasLimitStr)) {
            gasLimitStr = GASLIMIT_DEFAULT;
        }
        this.gasLimit = new BigInteger(MapUtils.getString(param, BcConstant.GAS_LIMIT, gasLimitStr));
        String gasPriceStr = paramInfo.getGasPrice();
        if (StringUtils.isBlank(gasPriceStr)) {
            gasPriceStr = GASLIMIT_PRICE;
        }
        this.gasPrice = Convert.toWei(MapUtils.getString(param, BcConstant.GAS_PRICE, gasPriceStr), Convert.Unit.GWEI)
                .toBigInteger();
        String valueStr = paramInfo.getValue();
        if (StringUtils.isBlank(valueStr)) {
            valueStr = VALUE;
        }
        this.value = Convert.toWei(MapUtils.getString(param, BcConstant.TRADE_VALUE, valueStr), Convert.Unit.HPB)
                .toBigInteger();
    }

    public boolean adjust(BigInteger amountUsed) {
        boolean returnValue = false;
        if (amountUsed == null) {
            return returnValue;
        }
        this.amountUsed = amountUsed;
        if (this.getGasLimit() != null && this.getGasPrice() != null && this.getValue() != null) {
            BigInteger sum = this.getGasLimit().multiply(this.getGasPrice()).add(this.getValue());
            if (amountUsed.compareTo(sum) > 0) {
                this.gasLimit = this.getGasLimit().add(scale);
                this.gasPrice = this.getGasPrice().add(scale);
                sum = this.getGasLimit().multiply(this.getGasPrice()).add(this.getValue());
                if (amountUsed.compareTo(sum) > 0) {
                    this.adjust(amountUsed);
                }
                returnValue = true;
            }
        }
        return returnValue;
    }
}
