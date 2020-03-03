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

package com.hpb.bc.vo;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.excel.annotation.ExcelTarget;
import io.swagger.annotations.ApiModel;


@ApiModel("统计返回值")
@ExcelTarget("20")
public class NodeStaticsVo {
    @Excel(name = "节点地址", orderNum = "0", width=30)
    private String address;
    @Excel(name = "总投票数", orderNum = "1", width=30)
    private String totalVoteAmount;
    @Excel(name = "余额", orderNum = "2", width=30)
    private String balance;
    @Excel(name = "投票率", orderNum = "3", width=30)
    private String voteRate;
    @Excel(name = "节点类型",replace = { "高性能_hpbnode", "候选_prenode" }, orderNum = "4", width=30)
    private String nodeType;
    @Excel(name = "挖矿奖励", orderNum = "5", width=30)
    private String minedRewardAmount;
    @Excel(name = "投票奖励", orderNum = "6", width=30)
    private String voteRewardAmount;
    @Excel(name = "总奖励", orderNum = "7", width=30)
    private String totalRewardAmount;
    @Excel(name = "区块号", orderNum = "8", width=30)
    private Long blockNumber;
    @Excel(name = "出块时间", exportFormat = "yyyy-MM-dd",orderNum = "8", width=30)
    private String blockTime;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTotalVoteAmount() {
        return totalVoteAmount;
    }

    public void setTotalVoteAmount(String totalVoteAmount) {
        this.totalVoteAmount = totalVoteAmount;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getVoteRate() {
        return voteRate;
    }

    public void setVoteRate(String voteRate) {
        this.voteRate = voteRate;
    }

    public String getNodeType() {
        return nodeType;
    }

    public void setNodeType(String nodeType) {
        this.nodeType = nodeType;
    }

    public String getMinedRewardAmount() {
        return minedRewardAmount;
    }

    public void setMinedRewardAmount(String minedRewardAmount) {
        this.minedRewardAmount = minedRewardAmount;
    }

    public String getVoteRewardAmount() {
        return voteRewardAmount;
    }

    public void setVoteRewardAmount(String voteRewardAmount) {
        this.voteRewardAmount = voteRewardAmount;
    }

    public String getTotalRewardAmount() {
        return totalRewardAmount;
    }

    public void setTotalRewardAmount(String totalRewardAmount) {
        this.totalRewardAmount = totalRewardAmount;
    }

    public Long getBlockNumber() {
        return blockNumber;
    }

    public void setBlockNumber(Long blockNumber) {
        this.blockNumber = blockNumber;
    }

    public String getBlockTime() {
        return blockTime;
    }

    public void setBlockTime(String blockTime) {
        this.blockTime = blockTime;
    }
}
