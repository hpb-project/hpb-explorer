package com.hpb.bc.solidity;


import com.hpb.bc.model.HpbData;
import com.hpb.bc.solidity.abi.AbiEntry;

import java.util.List;

/**
 * Created by davidroon on 27.03.17.
 * This code is released under Apache 2 license
 */
public interface SolidityContractDetails {
    List<AbiEntry> getAbi();

    HpbData getBinary();

    String getMetadata();
}
