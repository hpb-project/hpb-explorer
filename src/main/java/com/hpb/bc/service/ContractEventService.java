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

package com.hpb.bc.service;

import com.github.pagehelper.PageInfo;
import com.hpb.bc.model.EventData;
import com.hpb.bc.model.EventInfo;
import com.hpb.bc.model.HpbEventModel;
import com.hpb.bc.model.TxTransferHashModel;
import com.hpb.bc.propeller.model.EvmdiffLog;
import com.hpb.bc.propeller.model.StateLog;

public interface ContractEventService {

    PageInfo<EventData> queryPageEventDataByContractAddress(String contractAddress, int pageNum, int pageSize);


    PageInfo<EventData> queryPageEventDataByTxHash(String txHash, int pageNum, int pageSize);


    PageInfo<EventInfo> queryPageEventInfoByTxHash(String txHash, int pageNum, int pageSize);


    PageInfo<StateLog> queryPageStateLogByTxHash(TxTransferHashModel model);


    PageInfo<EvmdiffLog> queryPageEvmdiffLogByTxHash(TxTransferHashModel model);

    PageInfo<HpbEventModel> queryPageHpbEventModelByTxHash(String txHash, int pageNum, int pageSize);

    PageInfo<HpbEventModel> queryPageHpbEventModelByAddress(String address, int pageNum, int pageSize);

}
