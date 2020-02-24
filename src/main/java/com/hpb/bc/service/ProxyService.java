package com.hpb.bc.service;

import com.hpb.bc.entity.result.Result;
import io.hpb.web3.protocol.core.methods.response.HpbBlock;
import io.hpb.web3.protocol.core.methods.response.HpbCall;
import io.hpb.web3.protocol.core.methods.response.Transaction;
import io.hpb.web3.protocol.core.methods.response.TransactionReceipt;

import java.math.BigInteger;

public interface ProxyService {
    BigInteger hpbBlockNumber();

    Result<HpbBlock.Block> hpbGetBlockByNumber(Object blockNumber, boolean full);

    Result<TransactionReceipt> hpbGetTransactionReceipt(String txHash);

    String hpbCall(String to, String data, Object blockNumber);
}
