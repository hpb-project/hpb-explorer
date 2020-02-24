package com.hpb.bc.solidity;


import com.hpb.bc.model.EventData;
import com.hpb.bc.solidity.abi.AbiEntry;
import com.hpb.bc.solidity.converters.decoders.SolidityTypeDecoder;
import org.springframework.stereotype.Component;

import java.util.List;
public class RawSolidityEvent extends SolidityEvent<List<?>> {
    private final List<Class<?>> eventParameters;

    public RawSolidityEvent(AbiEntry description, List<List<SolidityTypeDecoder>> decoders, List<Class<?>> eventParameters) {
        super(description, decoders);
        this.eventParameters = eventParameters;
    }

    @Override
    public List<Object> parseEvent(EventData eventData) {
        return getDescription().decodeParameters(eventData, getDecoders(), eventParameters);
    }
}
