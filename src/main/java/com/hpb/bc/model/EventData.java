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
