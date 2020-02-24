package com.hpb.bc.service;

import com.github.pagehelper.PageInfo;
import com.hpb.bc.model.EventData;
import com.hpb.bc.model.EventInfo;
import com.hpb.bc.model.HpbEventModel;
import com.hpb.bc.model.TxTransferHashModel;
import com.hpb.bc.propeller.model.EvmdiffLog;
import com.hpb.bc.propeller.model.StateLog;

public interface ContractEventService {

    PageInfo<EventData> queryPageEventDataByContractAddress(String contractAddress, int pageNum, int pageSize);


    PageInfo<EventData> queryPageEventDataByTxHash(String txHash, int pageNum, int pageSize);


    PageInfo<EventInfo> queryPageEventInfoByTxHash(String txHash, int pageNum, int pageSize);


    PageInfo<StateLog> queryPageStateLogByTxHash(TxTransferHashModel model);


    PageInfo<EvmdiffLog> queryPageEvmdiffLogByTxHash(TxTransferHashModel model);

    PageInfo<HpbEventModel> queryPageHpbEventModelByTxHash(String txHash, int pageNum, int pageSize);

    PageInfo<HpbEventModel> queryPageHpbEventModelByAddress(String address, int pageNum, int pageSize);

}
