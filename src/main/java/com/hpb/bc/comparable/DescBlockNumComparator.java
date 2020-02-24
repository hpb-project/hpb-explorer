package com.hpb.bc.comparable;

import com.hpb.bc.entity.BlockInfo;

import java.util.Comparator;

/**
 * 区块排序比较器
 *
 * @author will
 */

public class DescBlockNumComparator implements Comparator<BlockInfo> {
    @Override
    public int compare(BlockInfo p1, BlockInfo p2) {
        return p2.getNumber().intValue() - p1.getNumber().intValue();
    }
}
