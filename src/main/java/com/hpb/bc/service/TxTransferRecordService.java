package com.hpb.bc.service;


import com.github.pagehelper.PageInfo;
import com.hpb.bc.entity.Erc721Token;
import com.hpb.bc.entity.Erc721TokenModel;
import com.hpb.bc.entity.TxTransferRecord;
import com.hpb.bc.model.Erc20TokenModel;
import com.hpb.bc.model.TxTransferRecordModel;

import java.util.List;

public interface TxTransferRecordService {

    boolean save(TxTransferRecord record);

    List<TxTransferRecord> getTxTransferRecordListByContractAddress(String address);

    PageInfo<TxTransferRecord> queryPageTxTransferRecordListByContractAddress(String address, int pageNum, int pageSize);


    PageInfo<TxTransferRecord> queryPageTxTransferRecordListByTxTransferRecord(TxTransferRecordModel record, int pageNum, int pageSize);

    List<Erc20TokenModel> queryErc20TokenModelListByModel(String address);

    PageInfo<Erc721Token> getHrc721StoragePage(Erc721TokenModel model, int currentPage, int pageSize);

    Erc721Token getErc721TokenInfoById(Long index, String address);

    PageInfo<TxTransferRecord> getErc721TokenTransferPage(String address, Long tokenId, int currentPage, int pageSize);

}
