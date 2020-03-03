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
import com.hpb.bc.entity.*;
import com.hpb.bc.entity.result.Result;
import com.hpb.bc.entity.result.ResultCode;
import com.hpb.bc.model.*;
import com.hpb.bc.service.*;
import com.hpb.bc.util.Sign;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/erc")
public class ErcContractController extends BaseController {
    @Autowired
    private ContractErcStandardInfoService contractErcStandardInfoService;

    @Autowired
    private TxTransferRecordService txTransferRecordService;

    @Autowired
    private AddressErcHolderService addressErcHolderService;

    @Autowired
    private ContractErcProfileSummaryService contractErcProfileSummaryService;
    @Autowired
    private OSSImageService oSSImageService;

    @ApiOperation(value = "根据智能合约的地址获取智能合约的详细信息", notes = "to address 为空的时候，调用该方法")
    @GetMapping("/contractAddress/{address}")
    public List<Object> getContractAddress(@PathVariable("address") String address) {
        ContractErcStandardInfo contractErcStandardInfo = contractErcStandardInfoService.getContractErcStandardInfoByContractAddress(address);
        Result<ContractErcStandardInfo> result = new Result<>(ResultCode.SUCCESS, contractErcStandardInfo);
        return result.getValue();
    }

    @ApiOperation(value = "ERC20合约列表", notes = "查询ERC20列表信息")
    @PostMapping("/contractAddressList")
    public List<Object> contractAddressList(@RequestBody ContractErcStandardInfoModel model) {
        ContractErcStandardInfo record = new ContractErcStandardInfo();
        BeanUtils.copyProperties(model, record);
        PageInfo<ContractErcStandardInfo> pageInfo = contractErcStandardInfoService.queryPageContractErcStandardInfo(record, model.getCurrentPage(), model.getPageSize());
        Result<PageInfo<ContractErcStandardInfo>> result = new Result<>(ResultCode.SUCCESS, pageInfo);
        return result.getValue();
    }


    @ApiOperation(value = "查询智能合约下的transfer", notes = "根据智能合约地址，查询该合约下的transfers")
    @PostMapping("/transfers")
    public List<Object> getTransfers(@RequestBody TxTransferRecordModel model) {
        PageInfo<TxTransferRecord> pageInfo = txTransferRecordService.queryPageTxTransferRecordListByContractAddress(model.getContractAddress(), model.getCurrentPage(), model.getPageSize());
        Result<PageInfo<TxTransferRecord>> result = new Result<>(ResultCode.SUCCESS, pageInfo);
        return result.getValue();
    }

    @ApiOperation(value = "查询智能合约下的addressHolder", notes = "根据智能合约地址查询addressHolder")
    @PostMapping("/erc-contract/addressHolder")
    public List<Object> getAddressHolders(@RequestBody AddressErcHolderModel model) {
        AddressErcHolder addressErcHolder = new AddressErcHolder();
        BeanUtils.copyProperties(model, addressErcHolder);
        PageInfo<AddressErcHolder> pageInfo = addressErcHolderService.queryPageAddressErcHolder(addressErcHolder, model.getCurrentPage(), model.getPageSize());
        Result<PageInfo<AddressErcHolder>> result = new Result<>(ResultCode.SUCCESS, pageInfo);
        return result.getValue();
    }

    @ApiOperation(value = "查询erc20合约下的contractMessageHolders", notes = "根据erc20合约名称查询contractMessageHolders")
    @GetMapping("/erc-contract/queryContractMessageHolders/{contractAddress}")
    public List<Object> getContractMessageHolders(@PathVariable("contractAddress") String contractAddress) {
        ContractErcProfileSummary contractErcProfileSummary = contractErcProfileSummaryService.queryContractMessageHolders(contractAddress);
        Result<ContractErcProfileSummary> result = new Result<>(ResultCode.SUCCESS, contractErcProfileSummary);
        return result.getValue();
    }

    @ApiOperation(value = "提交erc20合约下的contractMessageHolders", notes = "提交contractMessageHolders")
    @PostMapping("/erc-contract/submitContractMessageHolders")
    public List<Object> submitContractMessageHolders(@RequestBody ContractErcProfileSummaryModel contractErcProfileSummaryModel) throws Exception {
        boolean flag = contractErcProfileSummaryService.submitContractMessageHolders(contractErcProfileSummaryModel);
        Result<Boolean> result = new Result<>(ResultCode.SUCCESS, flag);
        return result.getValue();
    }

    @ApiOperation(value = "上传图片", notes = "上传图片")
    @PostMapping("/erc-contract/upload")
    public List<Object> upload(@RequestBody MultipartFile file) throws Exception {
        String url = oSSImageService.upload(file);
        Result<String> result = new Result<>(ResultCode.SUCCESS, url);
        return result.getValue();
    }

    @ApiOperation(value = "HRC721库存分页列表查询", notes = "HRC721库存分页列表查询")
    @PostMapping("/erc-contract/getHrc721StoragePage")
    public List<Object> getHrc721StoragePage(@RequestBody Erc721TokenModel model) {
        PageInfo<Erc721Token> pageInfo = txTransferRecordService.getHrc721StoragePage(model, model.getCurrentPage(), model.getPageSize());
        Result<PageInfo<Erc721Token>> result = new Result<>(ResultCode.SUCCESS, pageInfo);
        return result.getValue();
    }


    @ApiOperation(value = "单个721代币基础信息详情", notes = "单个721代币基础信息详情")
    @PostMapping("/erc-contract/getErc721TokenInfoById")
    public List<Object> getErc721TokenInfoById(@RequestBody Erc721TokenModel model) {
        Erc721Token tokenInfo = txTransferRecordService.getErc721TokenInfoById(model.getTokenId(), model.getParentErc721Address());
        Result<Erc721Token> result = new Result<>(ResultCode.SUCCESS, tokenInfo);
        return result.getValue();
    }

    @ApiOperation(value = "单个721代币转移记录分页查询", notes = "单个721代币转移记录分页查询")
    @PostMapping("/erc-contract/getErc721TokenTransferPage")
    public List<Object> getErc721TokenTransferPage(@RequestBody Erc721TokenModel model) {
        PageInfo<TxTransferRecord> pageInfo = txTransferRecordService.getErc721TokenTransferPage(model.getParentErc721Address(), model.getTokenId(), model.getCurrentPage(), model.getPageSize());
        Result<PageInfo<TxTransferRecord>> result = new Result<>(ResultCode.SUCCESS, pageInfo);
        return result.getValue();
    }


    @ApiOperation(value = "查询待审批的提交记录", notes = "提交contractMessageHolders")
    @PostMapping("/erc-contract/listContractErcProfileSummaryApprove")
    public List<Object> listContractErcProfileSummaryApprove(@RequestBody ContractApproveModel contractApproveModel) throws Exception {
        List<ContractErcProfileSummaryApprove> list  = contractErcProfileSummaryService.queryContractErcProfileSummaryApproveList(contractApproveModel);
        Result<List<ContractErcProfileSummaryApprove>> result = new Result<>(ResultCode.SUCCESS, list);
        return    result.getValue();
    }

    @ApiOperation(value = "查询待审批的提交记录", notes = "提交contractMessageHolders")
    @PostMapping("/erc-contract/aproveProfileSummary")
    public List<Object> aproveProfileSummary(@RequestBody ContractApproveModel contractApproveModel) throws Exception {
        boolean flag =  contractErcProfileSummaryService.approveContractErcProifleSummary(contractApproveModel);
        Result<Boolean> result = new Result<>(ResultCode.SUCCESS, flag);
        return  result.getValue();

    }



}
