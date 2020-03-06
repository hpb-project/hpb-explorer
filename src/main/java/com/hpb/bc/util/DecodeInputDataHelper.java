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

import com.hpb.bc.entity.ContractInfo;
import com.hpb.bc.entity.ContractMethodInfo;
import com.hpb.bc.mapper.ContractInfoMapper;
import com.hpb.bc.mapper.ContractMethodInfoMapper;
import io.hpb.web3.utils.Numeric;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

@Component
public class DecodeInputDataHelper {

    @Autowired
    ContractMethodInfoMapper contractMethodInfoMapper;

    @Autowired
    ContractInfoMapper contractInfoMapper;

    public String decodeInputData(String toAccount, String original) {
        if (toAccount == null) {
            return "0x";
        } else {
            ContractInfo contractInfo = contractInfoMapper.selectByPrimaryKey(toAccount);
            if (contractInfo == null) {
                return new String(Numeric.hexStringToByteArray(original), StandardCharsets.UTF_8);
            } else {
                if (original != null && original.length() > 10) {
                    String methodId = original.substring(0, 10);
                    ContractMethodInfo contractMethodInfo = contractMethodInfoMapper.selectByContractAndMethod(toAccount, methodId);
                    if (contractMethodInfo == null) {
                        return null;
                    }
                    StringBuilder decodeInput = new StringBuilder("Function: ")
                            .append(contractMethodInfo.getMethodName()).append("<br/>")
                            .append("MethodID: ").append(methodId).append("<br/>");
                    String params = original.substring(10);
                    int paramsNum = params.length() / 64;
//					List<String> list = new ArrayList<>();
                    for (int i = 0; i < paramsNum; i++) {
//						list.add(params.substring(i*64,(i+1)*64));
                        decodeInput.append("[").append(i).append("]:  ").append(params.substring(i * 64, (i + 1) * 64)).append("<br/>");
                    }
                    return decodeInput.toString();
                } else {
                    return "0x";
                }
            }
        }
    }
}
