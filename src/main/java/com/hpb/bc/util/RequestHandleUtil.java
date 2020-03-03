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

import com.hpb.bc.constant.ApiConstant;
import com.hpb.bc.constant.ApiParamConstant;
import com.hpb.bc.exception.ApiException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.net.URLDecoder;
import java.util.*;

/**
 * Created with IDEA
 *
 * @ Author：wangm
 * @ Date：Created in  2018/12/4 10:48
 * @ Description：请求内容处理工具类
 */
public class RequestHandleUtil {
    public static final String METHOD_POST = "POST";
    public static final String METHOD_GET = "GET";
    public static final String CONTENT_TYPE_JSON = "application/json";

    /**
     * APi 请求参数校验，1.是否为空，常量值是否正确
     *
     * @param paramMap
     * @return
     */
    public static Map<String, String> commonCheckParamsIsVaild(Map<String, String> paramMap) {
        List<Map<String, String>> ruleMapList = getApiRule();
        Map<String, String> checkMap = new HashMap<>();
        Map<String, String> vaildMap = BasicUtils.convertClassToMap(new ApiParamConstant());

        if (!paramMap.containsKey(ApiConstant.MODULE)) {
            checkMap.put(ApiConstant.MODULE, "请求参数缺失");
        }
        if (!paramMap.containsKey(ApiConstant.ACTION)) {
            checkMap.put(ApiConstant.ACTION, "请求参数缺失");
        }

        ruleMapList.stream().forEach(u -> {
            if (u.size() == paramMap.size()) {
                u.forEach((k, v) -> {
                    paramMap.forEach((k1, v1) -> {
                        if (k.equals(k1)) {
                            if (v.contains("######")) {
                                if (StringUtils.isBlank(v1)) {
                                    if (!checkMap.containsKey(k)) {
                                        checkMap.put(k, "参数值不能为空");
                                    }
                                }
                            } else if (!v.contains("######")) {
                                if (StringUtils.isBlank(v1)) {
                                    if (!checkMap.containsKey(k)) {
                                        checkMap.put(k, "参数值不能为空");
                                    }
                                } else {
                                    vaildMap.forEach((k3, v3) -> {
                                        if (k3.equals(k)) {
                                            if (!v3.contains(v1)) {
                                                if (!checkMap.containsKey(k)) {
                                                    checkMap.put(k, "参数值错误");
                                                }
                                            }
                                        }
                                    });
                                }
                            }
                        }
                    });
                });
            }
        });
        return checkMap;
    }

    private static List<Map<String, String>> getApiRule() {
        List<Map<String, String>> mapList = new LinkedList<>();
        try {
            ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
            Resource ress[] = resolver.getResources("classpath:/apiRule.txt");
            File ruleFile = null;
            for (Resource res : ress) {
                String targetFilePath = System.getProperty("user.home") + File.separator + res.getFilename();
                InputStream stream = res.getInputStream();
                ruleFile = new File(targetFilePath);
                FileUtils.copyInputStreamToFile(stream, ruleFile);
            }

            BufferedReader bfr = new BufferedReader(new InputStreamReader(new FileInputStream(ruleFile), "UTF-8"));
            String lineTxt = null;
            while ((lineTxt = bfr.readLine()) != null) {
                String[] lineStr = lineTxt.split("&");
                Map<String, String> mapStr = new TreeMap<>();
                for (int i = 0; i < lineStr.length; i++) {
                    String[] segmentStr = lineStr[i].split("=");
                    mapStr.put(segmentStr[0], segmentStr[1]);
                }
                mapList.add(mapStr);
            }
            bfr.close();
        } catch (FileNotFoundException e) {
            throw new ApiException(e.getMessage(), e);
        } catch (Exception e) {
            throw new ApiException(e.getMessage(), e);
        }
        return mapList;
    }

    /**
     * 获取请求参数
     *
     * @param req
     * @return 请求参数格式key-value
     */
    public static Map<String, String> getReqParam(HttpServletRequest req) {
        String method = req.getMethod();
        Map<String, String> reqMap = new HashMap<>();
        if (METHOD_GET.equals(method)) {
            reqMap = doGet(req);
        } else if (METHOD_POST.equals(method)) {
            reqMap = doPost(req);
        } else {
            //其他请求方式暂不处理
            return null;
        }
        return reqMap;
    }

    private static Map<String, String> doGet(HttpServletRequest req) {
        String param = req.getQueryString();
        return paramsToMap(param);
    }

    private static Map<String, String> doPost(HttpServletRequest req) {
        String contentType = req.getContentType();
        try {
            if (CONTENT_TYPE_JSON.equals(contentType)) {
                StringBuffer sb = new StringBuffer();
                InputStream is = req.getInputStream();
                InputStreamReader isr = new InputStreamReader(is);
                BufferedReader br = new BufferedReader(isr);
                String s = "";
                while ((s = br.readLine()) != null) {
                    sb.append(s);
                }
                String str = sb.toString();
                return paramsToMap(str);
            } else {
                //其他内容格式的请求暂不处理
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Map<String, String> paramsToMap(String params) {
        Map<String, String> map = new LinkedHashMap<>();
        if (StringUtils.isNotBlank(params)) {
            String[] array = params.split("&");
            for (String pair : array) {
                if ("=".equals(pair.trim())) {
                    continue;
                }
                String[] entity = pair.split("=");
                if (entity.length == 1) {
                    map.put(decode(entity[0]), "");
                } else {
                    map.put(decode(entity[0]), decode(entity[1]));
                }
            }
        }
        return map;
    }

    /**
     * 编码格式转换
     *
     * @param content
     * @return
     */
    public static String decode(String content) {
        try {
            return URLDecoder.decode(content, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }
}
