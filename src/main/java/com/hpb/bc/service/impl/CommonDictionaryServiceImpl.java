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
