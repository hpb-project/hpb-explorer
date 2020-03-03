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
import com.hpb.bc.constant.ContractConstant;
import com.hpb.bc.entity.ContractEventInfo;
import com.hpb.bc.entity.TxTransferRecord;
import com.hpb.bc.entity.result.Result;
import com.hpb.bc.entity.result.ResultCode;
import com.hpb.bc.model.Erc20ContractTransferModel;
import com.hpb.bc.service.ErcContractTransferService;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author LiXing
 * @version v1.0
 * date 2019/8/20 16:26
 **/
@RestController
@RequestMapping("/ercContractTransfer")
public class ErcContractTransferController {
    @Autowired
    ErcContractTransferService ercContractTransferService;

    @ApiOperation(value = "获取ERC智能合约交易记录",
            notes = "根据fromAddress和ToAddress获取ERC智能合约交易记录")
    @PostMapping("/getErcContractTransferByAddress")
    public List<Object> getErcContractTransferByAddress(@RequestBody Erc20ContractTransferModel model) throws Exception {

        PageInfo<TxTransferRecord> pageInfo = ercContractTransferService.getErcContractTransferByAddress(model.getAddress(), model.getContractAddress(), model.currentPage, model.pageSize);
        Result<PageInfo<TxTransferRecord>> result = new Result<>(ResultCode.SUCCESS, pageInfo);
        return result.getValue();
    }
}
