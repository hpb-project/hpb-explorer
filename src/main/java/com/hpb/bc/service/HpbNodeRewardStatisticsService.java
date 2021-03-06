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
