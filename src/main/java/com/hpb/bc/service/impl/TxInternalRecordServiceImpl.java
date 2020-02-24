package com.hpb.bc.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hpb.bc.entity.TxInternalRecord;
import com.hpb.bc.example.TxInternalRecordExample;
import com.hpb.bc.mapper.TxInternalRecordMapper;
import com.hpb.bc.model.TxInternalRecordModel;
import com.hpb.bc.service.TxInternalRecordService;
import io.hpb.web3.utils.Convert;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class TxInternalRecordServiceImpl implements TxInternalRecordService {

    @Autowired
    TxInternalRecordMapper txInternalRecordMapper;


    @Override
    public PageInfo<TxInternalRecord> queryPageTxInternalRecordListByTxInternalRecord(TxInternalRecordModel record) {
        PageHelper.startPage(record.getCurrentPage(), record.getPageSize());
        TxInternalRecordExample example = new TxInternalRecordExample();
        TxInternalRecordExample.Criteria criterion = example.createCriteria();
        if (StringUtils.isNotBlank(record.getTxHash())) {
            criterion.andTxHashEqualTo(record.getTxHash());
        }
        if (StringUtils.isNotBlank(record.getBlockHash())) {
            criterion.andBlockHashEqualTo(record.getBlockHash());
        }
        if (StringUtils.isNotBlank(record.getContractAddress())) {
            criterion.andContractAddressEqualTo(record.getContractAddress());
        }
        if (StringUtils.isNotBlank(record.getFromAddress())) {
            criterion.andFromAddressEqualTo(record.getFromAddress());
        }
        if (StringUtils.isNotBlank(record.getToAddress())) {
            criterion.andToAddressEqualTo(record.getToAddress());
        }
        example.setOrderByClause(" block_timestamp desc ");
        List<TxInternalRecord> list = txInternalRecordMapper.selectByExample(example);
        PageInfo<TxInternalRecord> pageInfo = new PageInfo<TxInternalRecord>(list);

        List<TxInternalRecord> resultList = new ArrayList<>();
        for (int i = 0; i < pageInfo.getList().size(); i++) {
            TxInternalRecord txInternalRecord = (TxInternalRecord) pageInfo.getList().get(i);
            String quantityHpb = Convert.fromWei(String.valueOf(txInternalRecord.getQuantity()), Convert.Unit.HPB).toPlainString();
            txInternalRecord.setQuantity(quantityHpb);
            resultList.add(txInternalRecord);
        }
        pageInfo.setList(resultList);
        return pageInfo;
    }
}
