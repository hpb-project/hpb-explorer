package com.hpb.bc.service;

import com.hpb.bc.entity.BlockInfo;
import com.hpb.bc.model.TransactionDetailModel;
import io.hpb.web3.protocol.core.methods.response.TransactionReceipt;

import java.util.List;
import java.util.Map;


public interface AccountService {
    /*Get Hpb Balance for a single Address*/
    String getSingleBalanceByAddress(Map<String, String> param);

    /*Get hpb Balance for multiple Addresses in a single call,
     * Separate addresses by comma, up to a maxium of 20 accounts in a single batch
     */
    List<Map<String, String>> getBalanceMultiByMultipleAddresses(Map<String, String> param);

    /*Get a list of 'Normal' Transactions By Address*/
    List<Map<String, String>> getNormalTransactionsByAddress1(Map<String, String> param);

    /*Get a list of 'Normal' Transactions By Address and Page*/
    List<Map<String, String>> getNormalTransactionsByAddressAndPage(Map<String, String> param);

    /*Get "Internal Transactions" by Transaction Hash*/
    TransactionReceipt getTransactionsByTransactionHash(Map<String, String> param);

    /*Get a list of "ERC20 - Token Transfer Events" by Address*/
    List<Map<String, String>> getTransactionsByERC20TokenByAddress(Map<String, String> param);

    /*/*Get a list of "ERC20 - Token Transfer Events" by contractAddress and Page*/
    List<Map<String, String>> getTransactionsByERC20TokenByContractAddressAndPage(Map<String, String> param);

    /*Get list of Blocks Mined by Address*/
    List<Map<String, String>> getBlocksByMinedAddress(Map<String, String> param);

    /*Get list of Blocks Mined by Address and Page*/
    List<Map<String, String>> getBlocksByMinedAddressAndPage(Map<String, String> param);


}
