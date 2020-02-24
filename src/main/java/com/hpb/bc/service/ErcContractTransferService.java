package com.hpb.bc.service;

import com.github.pagehelper.PageInfo;
import com.hpb.bc.entity.TxTransferRecord;


import java.util.List;

public interface ErcContractTransferService {

    PageInfo<TxTransferRecord> getErcContractTransferByAddress(String address, String contractAddress, int pageNum, int pageSize);
}
