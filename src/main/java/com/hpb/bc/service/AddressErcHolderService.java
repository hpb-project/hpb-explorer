package com.hpb.bc.service;

import com.github.pagehelper.PageInfo;
import com.hpb.bc.entity.AddressErcHolder;
import com.hpb.bc.model.AddressErcHolderModel;

public interface AddressErcHolderService {
    PageInfo<AddressErcHolder> queryPageAddressErcHolder(AddressErcHolder addressErcHolder, int pageNum, int pageSize);
}
