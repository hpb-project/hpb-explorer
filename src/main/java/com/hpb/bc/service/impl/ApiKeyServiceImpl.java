package com.hpb.bc.service.impl;

import com.hpb.bc.entity.ApiKey;
import com.hpb.bc.mapper.ApiKeyMapper;
import com.hpb.bc.service.ApiKeyService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ApiKeyServiceImpl implements ApiKeyService {

    @Autowired
    ApiKeyMapper apikeyMapper;

    @Override
    public ApiKey findApiKeyById(String id) {
        return apikeyMapper.selectByPrimaryKey(id);
    }

    @Override
    public ApiKey findApiKeyByApiKey(String apikey) {
        return apikeyMapper.selectApiKeyByKey(apikey);
    }

    @Override
    public void save(ApiKey apikey) {

        if (StringUtils.isNotEmpty(apikey.getId())) {
            apikeyMapper.updateByPrimaryKeySelective(apikey);
        } else {
            apikeyMapper.insert(apikey);
        }
    }
}
