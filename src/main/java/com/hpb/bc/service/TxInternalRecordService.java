package com.hpb.bc.service;

import com.github.pagehelper.PageInfo;
import com.hpb.bc.entity.TxInternalRecord;
import com.hpb.bc.model.TxInternalRecordModel;

import java.util.List;

public interface TxInternalRecordService {

    PageInfo<TxInternalRecord> queryPageTxInternalRecordListByTxInternalRecord(TxInternalRecordModel model);

}
