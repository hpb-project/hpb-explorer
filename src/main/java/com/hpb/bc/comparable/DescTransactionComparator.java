/*
 * Copyright 2020 HPB Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
