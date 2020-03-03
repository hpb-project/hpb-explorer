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

import com.hpb.bc.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;

@Service
public class RedisServiceImpl implements RedisService {

    @Autowired
    RedisTemplate<Object, Object> redisTemplate;

    @Override
    public Object getObject(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    @Override
    public <T> T get(String key, Class<? extends Serializable> requiredType) {
        Serializable val = (Serializable) redisTemplate.opsForValue().get(key);
        if (val == null) {
            return null;
        }
        return ((T) val);
    }

    @Override
    public boolean remove(String key) {
        return redisTemplate.delete(key);
    }

    @Override
    public void saveObject(String key, Object object) {
        redisTemplate.opsForValue().set(key, object);
    }

    @Override
    public void saveWithExpireTime(String key, Object object, long timeout, TimeUnit unit) {
        redisTemplate.opsForValue().set(key, object, timeout, unit);
    }

    @Override
    public boolean hasKey(String key) {
        return redisTemplate.hasKey(key);
    }
}
