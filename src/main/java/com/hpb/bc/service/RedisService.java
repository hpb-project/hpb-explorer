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
