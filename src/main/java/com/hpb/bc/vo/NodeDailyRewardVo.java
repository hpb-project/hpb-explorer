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



