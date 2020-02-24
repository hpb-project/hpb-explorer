package com.hpb.bc.solidity.values;


import com.hpb.bc.model.HpbData;

import java.util.Objects;

public class HpbTransactionRequest {
    private final HpbAccount account;
    private final HpbAddress address;
    private final HpbValue value;
    private final HpbData data;
    private final GasUsage gasLimit;
    /*    private final GasPrice gasPrice;*/

    public HpbTransactionRequest(HpbAccount account, HpbAddress address, HpbValue value, HpbData data, GasUsage gasLimit) {
        this.account = account;
        this.address = address;
        this.value = value;
        this.data = data;
        this.gasLimit = gasLimit;
        /*  this.gasPrice = gasPrice;*/
    }

    public HpbAccount getAccount() {
        return account;
    }

    public HpbAddress getAddress() {
        return address;
    }

    public HpbValue getValue() {
        return value;
    }

    public HpbData getData() {
        return data;
    }

    public GasUsage getGasLimit() {
        return gasLimit;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof HpbTransactionRequest)) return false;
        HpbTransactionRequest that = (HpbTransactionRequest) o;
        return Objects.equals(getAccount(), that.getAccount()) &&
                Objects.equals(getAddress(), that.getAddress()) &&
                Objects.equals(getValue(), that.getValue()) &&
                Objects.equals(getData(), that.getData()) &&
                Objects.equals(getGasLimit(), that.getGasLimit());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getAccount(), getAddress(), getValue(), getData(), getGasLimit());
    }

    @Override
    public String toString() {
        return "HpbTransactionRequest{" +
                "account=" + account +
                ", address=" + address +
                ", value=" + value +
                ", data=" + data +
                ", gasLimit=" + gasLimit +
                '}';
    }


    /*
    public GasPrice getGasPrice() {
        return gasPrice;
    }*/



/*    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        HpbTransactionRequest that = (HpbTransactionRequest) o;

        if (account != null ? !account.equals(that.account) : that.account != null) return false;
        if (address != null ? !address.equals(that.address) : that.address != null) return false;
        if (value != null ? !value.equals(that.value) : that.value != null) return false;
        if (data != null ? !data.equals(that.data) : that.data != null) return false;
        if (gasLimit != null ? !gasLimit.equals(that.gasLimit) : that.gasLimit != null) return false;
        return gasPrice != null ? gasPrice.equals(that.gasPrice) : that.gasPrice == null;
    }

    @Override
    public int hashCode() {
        int result = account != null ? account.hashCode() : 0;
        result = 31 * result + (address != null ? address.hashCode() : 0);
        result = 31 * result + (value != null ? value.hashCode() : 0);
        result = 31 * result + (data != null ? data.hashCode() : 0);
        result = 31 * result + (gasLimit != null ? gasLimit.hashCode() : 0);
        result = 31 * result + (gasPrice != null ? gasPrice.hashCode() : 0);
        return result;
    }*/
}
