package com.hpb.bc.solidity;


import com.hpb.bc.model.EventData;
import com.hpb.bc.solidity.abi.AbiEntry;
import com.hpb.bc.solidity.converters.decoders.SolidityTypeDecoder;

import java.util.List;

public abstract class SolidityEvent<T> {
    private final AbiEntry description;
    private final List<List<SolidityTypeDecoder>> decoders;

    public SolidityEvent(AbiEntry description, List<List<SolidityTypeDecoder>> decoders) {
        this.description = description;
        this.decoders = decoders;
    }

    public AbiEntry getDescription() {
        return description;
    }

    public List<List<SolidityTypeDecoder>> getDecoders() {
        return decoders;
    }

    public abstract T parseEvent(EventData eventData);

    public boolean match(EventData data) {
        return data.getEventSignature().equals(description.signature()) || data.getEventSignature().equals(description.signatureLong());
    }

}
