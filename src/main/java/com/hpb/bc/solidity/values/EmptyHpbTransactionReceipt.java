package com.hpb.bc.solidity.values;

import com.hpb.bc.model.HpbData;
import com.hpb.bc.model.HpbHash;

import java.util.Collections;

/**
 * Created by felipe.forbeck on 20.02.19.
 * This code is released under Apache 2 license
 */
public class EmptyHpbTransactionReceipt extends HpbTransactionReceipt {

    public EmptyHpbTransactionReceipt() {
        super(HpbHash.empty(), HpbHash.empty(), HpbAddress.empty(), HpbAddress.empty(), HpbAddress.empty(), HpbData.empty(), "", HpbData.empty(), false, Collections.EMPTY_LIST, HpbValue.hpber(0l));
    }
}
