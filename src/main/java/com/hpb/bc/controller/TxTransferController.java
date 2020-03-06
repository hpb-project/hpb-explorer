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

package com.hpb.bc.controller;

import com.github.pagehelper.PageInfo;
import com.hpb.bc.entity.ContractErcStandardInfo;
import com.hpb.bc.entity.TxTransferRecord;
import com.hpb.bc.entity.result.Result;
import com.hpb.bc.entity.result.ResultCode;
import com.hpb.bc.model.Erc20TokenModel;
import com.hpb.bc.model.TxTransferHashModel;
import com.hpb.bc.model.TxTransferRecordModel;
import com.hpb.bc.service.TxTransferRecordService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tx-transfer")
public class TxTransferController extends BaseController {

    @Autowired
    private TxTransferRecordService txTransferRecordService;

    @ApiOperation(value = "根据区块,智能合约地址，查询transfer （ERC20代币交易）", notes = "根据区块,智能合约地址，查询transfer （ERC20代币交易）")
    @PostMapping("/txTransfers")
    public List<Object> getBlockTxTransfer(@RequestBody TxTransferRecordModel model) {
        PageInfo<TxTransferRecord> pageInfo = txTransferRecordService.queryPageTxTransferRecordListByTxTransferRecord(model, model.getCurrentPage(), model.getPageSize());
        Result<PageInfo<TxTransferRecord>> result = new Result<>(ResultCode.SUCCESS, pageInfo);
        return result.getValue();
    }

    @ApiOperation(value = "根据交易hash获取该交易内部转移", notes = "根据交易hash获取该交易内部转移")
    @PostMapping("/txHash/txTransfers")
    public List<Object> getTxTransfer(@RequestBody TxTransferHashModel model) {
        TxTransferRecordModel tempModel = new TxTransferRecordModel();
        tempModel.setTxHash(model.getTxHash());
        tempModel.setCurrentPage(model.getCurrentPage());
        tempModel.setPageSize(model.getPageSize());
        PageInfo<TxTransferRecord> pageInfo = txTransferRecordService.queryPageTxTransferRecordListByTxTransferRecord(tempModel, tempModel.getCurrentPage(), tempModel.getPageSize());
        Result<PageInfo<TxTransferRecord>> result = new Result<>(ResultCode.SUCCESS, pageInfo);
        return result.getValue();
    }

    @ApiOperation(value = "根据地址查询其拥有ERC20列表及其数目", notes = "根据地址查询其拥有ERC20列表及其数目")
    @PostMapping("/erc-contract/erc20TokenList/{address}")
    public List<Object> erc20TokenList(@PathVariable("address") String address) {
        List<Erc20TokenModel> erc20TokenModelList = txTransferRecordService.queryErc20TokenModelListByModel(address);
        Result<List<Erc20TokenModel>> result = new Result<>(ResultCode.SUCCESS, erc20TokenModelList);
        return result.getValue();
    }


}
