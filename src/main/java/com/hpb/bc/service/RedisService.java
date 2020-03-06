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

package com.hpb.bc.service;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;

public interface RedisService {

    /**
     * get object form redis by key
     *
     * @param key
     * @return
     */
    public Object getObject(String key);

    /**
     * get class by key
     *
     * @param key
     * @param requiredType
     * @return
     */
    public <T> T get(String key, Class<? extends Serializable> requiredType);

    /**
     * remove key
     *
     * @param key
     * @return
     */
    public boolean remove(String key);

    /**
     * @param key
     * @param object
     * @return
     */
    public void saveObject(String key, Object object);

    /**
     * @param key
     * @param object
     * @param timeout
     * @param unit
     * @return
     */
    public void saveWithExpireTime(String key, Object object, long timeout, TimeUnit unit);

    /**
     * @param key
     * @return
     */
    public boolean hasKey(String key);
}
