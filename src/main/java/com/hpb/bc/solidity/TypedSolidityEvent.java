package com.hpb.bc.solidity;

import com.hpb.bc.model.EventData;
import com.hpb.bc.solidity.abi.AbiEntry;
import com.hpb.bc.solidity.converters.decoders.SolidityTypeDecoder;

import java.util.List;

/**
 * Created by davidroon on 02.04.17.
 * This code is released under Apache 2 license
 */
public class TypedSolidityEvent<T> extends SolidityEvent<T> {
    private final Class<T> entityClass;

    public TypedSolidityEvent(AbiEntry description, List<List<SolidityTypeDecoder>> decoders, Class<T> entityClass) {
        super(description, decoders);
        this.entityClass = entityClass;
    }

    @Override
    public T parseEvent(EventData eventData) {
        return (T) getDescription().decode(eventData, getDecoders(), entityClass);
    }

    public Class<T> getEntityClass() {
        return entityClass;
    }

    public boolean rawDefinition() {
        return entityClass == null;
    }
}
