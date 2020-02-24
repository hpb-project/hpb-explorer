package com.hpb.bc.service.impl;

import com.hpb.bc.entity.CommonDictionary;
import com.hpb.bc.example.CommonDictionaryExample;
import com.hpb.bc.mapper.CommonDictionaryMapper;
import com.hpb.bc.service.CommonDictionaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommonDictionaryServiceImpl implements CommonDictionaryService {

    @Autowired
    CommonDictionaryMapper commonDictionaryMapper;

    @Override
    public List<CommonDictionary> getDictionaryByGroupName(String group) {

        CommonDictionaryExample commonDictionaryExample = new CommonDictionaryExample();
        commonDictionaryExample.createCriteria().andGroupNameEqualTo(group);
        commonDictionaryExample.setOrderByClause(" display_index asc ");
        commonDictionaryExample.createCriteria().andIsVisibleEqualTo(true);
        return commonDictionaryMapper.selectByExample(commonDictionaryExample);
    }
}
