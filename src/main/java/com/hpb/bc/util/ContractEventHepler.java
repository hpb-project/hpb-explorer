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

package com.hpb.bc.util;


import com.hpb.bc.model.EventData;
import com.hpb.bc.model.EventInfo;
import com.hpb.bc.model.HpbData;
import com.hpb.bc.model.HpbHash;
import com.hpb.bc.solidity.SolidityEvent;
import com.hpb.bc.solidity.values.HpbAddress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import io.hpb.web3.protocol.core.methods.response.Log;

@Component
public class ContractEventHepler {

    public Logger logger = LoggerFactory.getLogger(ContractEventHepler.class);


    public List<EventData> toEventInfos(HpbHash transactionHash, List<Log> logs) {
        return logs.stream().map(log -> this.toEventInfo(transactionHash, log)).collect(Collectors.toList());
    }

    public EventData toEventInfo(HpbHash transactionHash, Log log) {
        List<HpbData> topics = log.getTopics().stream().map(HpbData::of).collect(Collectors.toList());
       /* HpbData eventSignature = topics.get(0);
        HpbData eventArguments = HpbData.of(log.getData());
        return new EventData(transactionHash, eventSignature, eventArguments, topics.subList(1, topics.size()));*/
        if (topics.size() > 0) {
            HpbData eventSignature = topics.get(0);
            logger.info("log.getData() ====" + log.getData());
            HpbData eventArguments = HpbData.of(log.getData());
            logger.info("eventArguments ====" + eventArguments);
            return new EventData(transactionHash, eventSignature, eventArguments, topics.subList(1, topics.size()));
        } else {
            return new EventData(transactionHash, HpbData.empty(), HpbData.empty(), new ArrayList<>());
        }
    }


    public <T> List<EventInfo<T>> getEventsAtTransactionWithInfo(SolidityEvent<T> eventDefinition, HpbAddress address, HpbHash transactionHash) {
    /*  TransactionReceipt receipt = ethereum.getTransactionInfo(transactionHash).flatMap(TransactionInfo::getReceipt).orElseThrow(() -> new ApiException("no Transaction receipt found!"));
        if (address.equals(receipt.receiveAddress)) {
            return receipt.events.stream().filter(eventDefinition::match)
                    .map(data -> new EventInfo<>(data.getTransactionHash(), eventDefinition.parseEvent(data)))
                    .collect(Collectors.toList());
        }
    */
        return new ArrayList<>();
    }


    /*
           logs.stream().map(log -> {
        List<DataWord> topics = log.getTopics();
        HpbData eventSignature = HpbData.of(topics.get(0).getData());
        HpbData eventArguments = HpbData.of(log.getData());
        List<HpbData> indexedArguments = topics.subList(1, topics.size()).stream()
                .map(dw -> HpbData.of(dw.getData()))
                .collect(Collectors.toList());

        return new EventData(transactionHash, eventSignature, eventArguments, indexedArguments);
    }).collect(Collectors.toList());
*/

}
