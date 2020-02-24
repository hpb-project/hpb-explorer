package com.hpb.bc.propeller.event;

import com.hpb.bc.solidity.values.HpbAddress;
import io.hpb.web3.abi.datatypes.generated.Uint256;

import java.util.Objects;

public class EventTransfer_address_address_uint256 {
    private HpbAddress from;
    private HpbAddress to;
    private Uint256 value;

    public EventTransfer_address_address_uint256() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EventTransfer_address_address_uint256)) return false;
        EventTransfer_address_address_uint256 that = (EventTransfer_address_address_uint256) o;
        return Objects.equals(from, that.from) &&
                Objects.equals(to, that.to) &&
                Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(from, to, value);
    }

    @Override
    public String toString() {
        return "EventTransfer_address_address_uint256{" +
                "from=" + from +
                ", to=" + to +
                ", value=" + value +
                '}';
    }
}
