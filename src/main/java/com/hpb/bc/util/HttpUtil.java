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

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.druid.support.json.JSONUtils;
import com.hpb.bc.constant.AccountConstant;
import org.apache.commons.collections4.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.hpb.bc.common.RestTemplateConfig;
import com.hpb.bc.common.SpringBootContext;

@SuppressWarnings({"unchecked", "rawtypes"})
public abstract class HttpUtil {
    private static final Logger log = LoggerFactory.getLogger(HttpUtil.class);
    private static RestTemplate restTemplate;

    public static RestTemplate getRestTemplate() {
        if (restTemplate == null) {
            if (SpringBootContext.getAplicationContext() != null) {
                RestTemplateConfig restTemplateConfig = SpringBootContext.getBean("restTemplateConfig",
                        RestTemplateConfig.class);
                restTemplate = restTemplateConfig.restTemplate();
            } else {
                restTemplate = new RestTemplateConfig().restTemplate();
            }
        }
        return restTemplate;
    }

    public static List<String> getForObject(String url, List<String> param) {
        return getRestTemplate().getForObject(url, List.class, param);
    }

    public static List<String> getForEntity(String url, List<String> param) {
        ResponseEntity<List> responseEntity1 = getRestTemplate().getForEntity(url, List.class, param);
        HttpStatus statusCode = responseEntity1.getStatusCode();
        log.info("statusCode:{}", statusCode);
        HttpHeaders header = responseEntity1.getHeaders();
        log.info("header:{}", header);
        return responseEntity1.getBody();
    }

    public static List<String> exchange(String url, List<String> param) throws Exception {
        ResponseEntity<List> responseEntity1 = getRestTemplate().exchange(RequestEntity.get(new URI(url)).build(),
                List.class);
        HttpStatus statusCode = responseEntity1.getStatusCode();
        log.info("statusCode:{}", statusCode);
        HttpHeaders header = responseEntity1.getHeaders();
        log.info("header:{}", header);
        return responseEntity1.getBody();
    }

    public static List<String> postForObject(String url, List<String> param) throws Exception {
        return getRestTemplate().postForObject(url, param, List.class);
    }

    public static String getForString(String url) throws Exception {
        return getRestTemplate().getForObject(url, String.class);
    }


    public static List<String> postForEntity(String url, List<String> param) throws Exception {
        ResponseEntity<List> responseEntity1 = getRestTemplate().postForEntity(url, param, List.class);
        return responseEntity1.getBody();
    }

    public static List<String> postExchange(String url, List<String> param) throws Exception {
        RequestEntity<List<String>> requestEntity = RequestEntity.post(new URI(url)).body(param);
        ResponseEntity<List> responseEntity1 = getRestTemplate().exchange(requestEntity, List.class);
        return responseEntity1.getBody();
    }


    public static Map<String, Object> getHPBInfoOfMarketCap() {
        Map<String, Object> map = new HashMap<>();
        try {
            String result = HttpUtil.getForString("https://api.coinmarketcap.com/v2/ticker/2345/");
            map = (Map<String, Object>) JSONUtils.parse(result);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return map;
        }
    }

    public static Double getInstantPriceOfHPB(String uri) {
        Double price = 0.00d;
        try {
            String result = HttpUtil.getForString(uri);
            Map<String, Object> map = (Map<String, Object>) JSONUtils.parse(result);
            map = (Map<String, Object>) MapUtils.getObject(map, "data");
            map = (Map<String, Object>) MapUtils.getObject(map, "quotes");
            map = (Map<String, Object>) MapUtils.getObject(map, "USD");
            price = (Double) MapUtils.getObject(map, "price");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return price;
        }
    }

    public static Map<String, Object> getHpbPriceInfoOfBibox(String uri) {
        Map<String, Object> map = new HashMap<>();
        try {
            String result = HttpUtil.getForString(uri);
            map = (Map<String, Object>) JSONUtils.parse(result);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return map;
        }
    }

    public static Double getInstantRateCorrUSD(String uri) {
        Double rate = 0.00d;
        try {
            String result = HttpUtil.getForString(uri);
            String[] wh = result.split("#");
            rate = new Double(wh[4]);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return rate;
        }
    }

    public static void main(String args[]) throws Exception {
        List<String> list = new ArrayList<String>();
        list.add("queryTrade");
        Map<String, String> param = new HashMap<String, String>();
        param.put(AccountConstant.FROM_ACCOUNT_ID, "0x5f38056f45091ee992298e53681b0a60c999ff95");
        param.put("queryType", "byAddress");
        list.add(SecurityUtils.getEncodeParamByEecodeKey(param, "1234"));
        list.add("");
        list.add("123");
        list.add("456");
        postForObject("http://localhost:38080/HpbBcBrowser/invokeHpbCmd", list);
    }


}