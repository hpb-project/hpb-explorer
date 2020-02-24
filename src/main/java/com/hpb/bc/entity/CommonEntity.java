package com.hpb.bc.entity;

import java.io.Serializable;

import com.hpb.bc.common.SpringBootContext;
import com.hpb.bc.configure.Web3Properties;

public abstract class CommonEntity implements Serializable {
    private static final long serialVersionUID = 6306701881305622216L;
    private Long tableSuffix;
    private Integer fechNum;
    private Long endBlock;

    public abstract Long getbNumber();

    public void setTableSuffix(Long tableSuffix) {
        this.tableSuffix = tableSuffix;
    }

    public Integer getFechNum() {
        if (fechNum == null) {
            return 20;
        }
        return fechNum;
    }

    public void setFechNum(Integer fechNum) {
        this.fechNum = fechNum;
    }

    public Long getEndBlock() {
        return endBlock;
    }

    public void setEndBlock(Long endBlock) {
        this.endBlock = endBlock;
    }
}
