package com.hpb.bc.service;


import com.hpb.bc.entity.ApiKey;

public interface ApiKeyService {


    ApiKey findApiKeyById(String id);

    /***
     *   根据公钥apiKey 查找记录；
     *
     */
    ApiKey findApiKeyByApiKey(String apikey);

    /**
     * 保持apiKey
     *
     * @Para ApiKey apikey
     */
    void save(ApiKey apikey);
}
