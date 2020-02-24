package com.hpb.bc.service;

import com.hpb.bc.entity.CommonDictionary;

import java.util.List;

public interface CommonDictionaryService {

    List<CommonDictionary> getDictionaryByGroupName(String group);
}
