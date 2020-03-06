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

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hpb.bc.model.ContractVerifyModel;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/6/17.
 */

public class GsonUtil {
    private static Gson gson = null;

    static {
        if (gson == null) {
            gson = new Gson();
        }
    }

    private GsonUtil() {
    }

    /**
     * 转成json
     *
     * @param object
     * @return
     */
    public static String gsonString(Object object) {
        String gsonString = null;
        if (gson != null) {
            gsonString = gson.toJson(object);
        }
        return gsonString;
    }

    /**
     * 转成bean
     *
     * @param gsonString
     * @param cls
     * @return
     */
    public static <T> T gsonToBean(String gsonString, Class<T> cls) {
        T t = null;
        if (gson != null) {
            t = gson.fromJson(gsonString, cls);
        }
        return t;
    }

    /**
     * 转成list
     *
     * @param gsonString
     * @param cls
     * @return
     */
    public static <T> List<T> gsonToList(String gsonString, Class<T> cls) {
        List<T> list = null;
        if (gson != null) {
            list = gson.fromJson(gsonString, new TypeToken<List<T>>() {
            }.getType());
        }
        return list;
    }

    /**
     * 转成list中有map的
     *
     * @param gsonString
     * @return
     */
    public static <T> List<Map<String, T>> gsonToListMaps(String gsonString) {
        List<Map<String, T>> list = null;
        if (gson != null) {
            list = gson.fromJson(gsonString,
                    new TypeToken<List<Map<String, T>>>() {
                    }.getType());
        }
        return list;
    }

    /**
     * 转成map的
     *
     * @param gsonString
     * @return
     */
    public static <T> Map<String, T> gsonToMaps(String gsonString) {
        Map<String, T> map = null;
        if (gson != null) {
            map = gson.fromJson(gsonString, new TypeToken<Map<String, T>>() {
            }.getType());
        }
        return map;
    }

    public static void main(String[] args) {
        // ContractPara cp = new ContractPara();
        ContractVerifyModel cp = new ContractVerifyModel();
        cp.setContractName("Test");
        cp.setOptimizeFlag("Y");
        cp.setContractAddr("0xd1d587b6000c594e46a7cfc73442f984338570ad");
        cp.setTxHash("0xfff158776a06e888162617614fe2a4941bd6a4683c4c38bee5d6bb00b6c55b65");
        String soliditySrcCode = "/**\n" +
                " *Submitted for verification at Etherscan.io on 2018-01-20\n" +
                "*/\n" +
                "\n" +
                "pragma solidity ^0.4.25;\n" +
                "\n" +
                "\n" +
                "contract Test  {\n" +
                "    string public name;\n" +
                "    string public symbol;\n" +
                "    uint public decimals;\n" +
                "\n" +
                "    constructor(string memory _name,string memory _symbol,uint _decimals) public{\n" +
                "        name=_name;\n" +
                "        symbol=_symbol;\n" +
                "        decimals=_decimals;\n" +
                "    }\n" +
                "}";
        cp.setContractSrc(soliditySrcCode);
        cp.setHvmVersion("0.4.25");
        cp.setContractCompilerVersionNumber("0.4.25");
        String gs = GsonUtil.gsonString(cp);

        System.out.println(gs);

/*
        String [] versionStr = { "0.5.9", "0.5.8","0.4.26","0.5.7" };

        List<String > versionList = Arrays.asList(versionStr);
        for(String str:versionList){
            System.out.println("str ====== " + str);
        }*/


    }
}
