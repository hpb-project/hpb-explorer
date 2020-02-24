package com.hpb.bc.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hpb.bc.entity.ContractEventInfo;
import com.hpb.bc.entity.TxTransferRecord;
import com.hpb.bc.entity.result.Result;
import com.hpb.bc.example.TxTransferRecordExample;
import com.hpb.bc.mapper.TxTransferRecordMapper;
import com.hpb.bc.service.ErcContractTransferService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class ErcContractTransferServiceImpl implements ErcContractTransferService {
    @Autowired
    TxTransferRecordMapper txTransferRecordMapper;

    @Override
    public PageInfo<TxTransferRecord> getErcContractTransferByAddress(String address, String contractAddress, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        TxTransferRecordExample txTransferRecordExample = new TxTransferRecordExample();
        txTransferRecordExample.setOrderByClause("create_timestamp desc");
        TxTransferRecordExample.Criteria criteria = txTransferRecordExample.createCriteria();
        TxTransferRecordExample.Criteria criteria1 = txTransferRecordExample.createCriteria();
        if (StringUtils.isNotBlank(address) && StringUtils.isNotBlank(contractAddress)) {
            criteria.andContractAddressEqualTo(contractAddress).andFromAddressEqualTo(address);
            criteria1.andContractAddressEqualTo(contractAddress).andToAddressEqualTo(address);
            txTransferRecordExample.or(criteria1);
        }

        List<TxTransferRecord> txTransferRecords = txTransferRecordMapper.selectByExample(txTransferRecordExample);
        PageInfo<TxTransferRecord> pageInfo = new PageInfo<TxTransferRecord>(txTransferRecords);
        return pageInfo;
    }
}
