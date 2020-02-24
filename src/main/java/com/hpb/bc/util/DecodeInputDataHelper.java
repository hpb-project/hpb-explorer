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
