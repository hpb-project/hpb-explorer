package com.hpb.bc.service;
import com.hpb.bc.entity.HpbNodeRewardRecord;
import com.hpb.bc.model.NodeRewardStaticsQueryModel;
import com.hpb.bc.model.NodeRewardStaticsQueryModel;
import com.hpb.bc.vo.NodeDailyRewardVo;
import com.hpb.bc.vo.NodeStaticsVo;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public interface HpbNodeRewardStatisticsService {

    List<HpbNodeRewardRecord> queryNodeReward(NodeRewardStaticsQueryModel nodeRewardQueryModel);

    List<NodeStaticsVo> queryNodeRewardList(NodeRewardStaticsQueryModel nodeRewardQueryModel);

    List<NodeDailyRewardVo> queryNodeDailyRewardVoList(NodeRewardStaticsQueryModel nodeRewardQueryModel);

    List<NodeDailyRewardVo> queryNodeMonthRewardVoList(NodeRewardStaticsQueryModel nodeRewardQueryModel);


    List<NodeDailyRewardVo> queryNodeDailyRewardAssignedList(String queryDateTime);


    List<NodeDailyRewardVo> queryNodeMonthRewardAssignedList(String queryDateTime);







}
