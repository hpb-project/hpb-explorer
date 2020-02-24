package com.hpb.bc.model;
import java.util.List;

public class EventData  {
    private  HpbHash transactionHash;
    private  HpbData eventSignature;
    private  HpbData eventArguments;
    private  List<HpbData> indexedArguments;

    public EventData(HpbHash transactionHash, HpbData eventSignature, HpbData eventArguments, List<HpbData> indexedArguments) {
        this.transactionHash = transactionHash;
        this.eventSignature = eventSignature;
        this.eventArguments = eventArguments;
        this.indexedArguments = indexedArguments;
    }

    public List<HpbData> getIndexedArguments() {
        return this.indexedArguments;
    }

    public HpbData getEventSignature() {
        return this.eventSignature;
    }

    public HpbData getEventArguments() {
        return this.eventArguments;
    }

    public HpbHash getTransactionHash() {
        return this.transactionHash;
    }

    public void setTransactionHash(HpbHash transactionHash) {
        this.transactionHash = transactionHash;
    }

    public void setEventSignature(HpbData eventSignature) {
        this.eventSignature = eventSignature;
    }

    public void setEventArguments(HpbData eventArguments) {
        this.eventArguments = eventArguments;
    }

    public void setIndexedArguments(List<HpbData> indexedArguments) {
        this.indexedArguments = indexedArguments;
    }

  @Override
    public String toString() {
        return "EventData {transactionHash=" + this.transactionHash + ", eventSignature=" + this.eventSignature + ", eventArguments=" + this.eventArguments + ", indexedArguments=" + this.indexedArguments + '}';
    }
}
