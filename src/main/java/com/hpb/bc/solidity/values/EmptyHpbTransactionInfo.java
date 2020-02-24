package com.hpb.bc.solidity.values;

import com.hpb.bc.model.HpbHash;

/**
 * Created by felipe.forbeck on 22.02.19.
 * This code is released under Apache 2 license
 */
public class EmptyHpbTransactionInfo extends HpbTransactionInfo {

    public EmptyHpbTransactionInfo() {
        super(HpbHash.empty(), new EmptyHpbTransactionReceipt(), HpbTransactionStatus.Unknown, HpbHash.empty());
    }

}
