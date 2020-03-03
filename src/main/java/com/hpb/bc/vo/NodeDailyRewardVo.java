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
public class NodeDailyRewardVo {

    @Excel(name = "节点地址", orderNum = "0", width=30)
    private String nodeAddress;
    @Excel(name = "节点名称", orderNum = "1", width=30)
    private String nodeName;
    @Excel(name = "总奖励量", orderNum = "1", width=30)
    private String totalAmount;
    @Excel(name = "日期", orderNum = "1", width=30)
    private String dateStr;


    public String getNodeAddress() {
        return nodeAddress;
    }

    public void setNodeAddress(String nodeAddress) {
        this.nodeAddress = nodeAddress;
    }

    public String getNodeName() {
        return nodeName;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getDateStr() {
        return dateStr;
    }

    public void setDateStr(String dateStr) {
        this.dateStr = dateStr;
    }
}



