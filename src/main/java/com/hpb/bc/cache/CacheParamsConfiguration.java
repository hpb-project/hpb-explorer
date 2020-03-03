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

package com.hpb.bc.cache;

import org.apache.ibatis.cache.CacheException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

import com.hpb.bc.entity.cache.CacheParam;


@Configuration
@EnableCaching
@EnableAsync
@CacheConfig(cacheNames = "cacheParams")
public class CacheParamsConfiguration {

    public static final String CACHE_NAME = "cacheParams";
    // 这里的单引号不能少，否则会报错，被识别是一个对象
    public static final String CACHE_KEY_PREFIX = "'cacheParam'";
    private static final Logger log = LoggerFactory.getLogger(CacheParamsConfiguration.class);

    // 删除缓存数据
    @CacheEvict(value = CACHE_NAME, key = CACHE_KEY_PREFIX + "+#proccessId") // 这是清除缓存
    public void delete(String proccessId) {

    }

    // 更新缓存数据
    @CachePut(value = CACHE_NAME, key = CACHE_KEY_PREFIX + "+#cacheParam.getProccessId()")
    public CacheParam update(CacheParam cacheParam) throws CacheException {
        log.info("更新缓存[{}-{}]", CACHE_KEY_PREFIX, cacheParam.getProccessId());
        return cacheParam;
    }

}