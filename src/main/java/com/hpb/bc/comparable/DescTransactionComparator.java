package com.hpb.bc.comparable;

import io.hpb.web3.protocol.core.methods.response.Transaction;

import java.util.Comparator;

/**
 * 区块排序比较器
 *
 * @author will
 */

public class DescTransactionComparator implements Comparator<Transaction> {
    @Override
    public int compare(Transaction p1, Transaction p2) {
        int cr = 0;
        int a = p2.getBlockNumber().intValue() - p1.getBlockNumber().intValue();
        if (a != 0) {
            cr = (a > 0) ? 3 : -1;
        } else {
            a = p2.getTransactionIndex().intValue() - p1.getTransactionIndex().intValue();
            if (a != 0) {
                cr = (a > 0) ? 2 : -2;
            }
        }
        return cr;
    }
}
