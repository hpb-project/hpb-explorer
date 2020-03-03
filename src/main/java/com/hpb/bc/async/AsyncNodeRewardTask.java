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

package com.hpb.bc.async;
import com.hpb.bc.entity.HpbCampaignPeriodVoteResult;
import com.hpb.bc.example.HpbCampaignPeriodVoteResultExample;
import com.hpb.bc.mapper.HpbCampaignPeriodVoteResultMapper;
import com.hpb.bc.service.BlockFacetService;
import com.hpb.bc.vo.NodeDailyRewardVo;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import java.util.*;
import java.util.concurrent.CountDownLatch;

@Component
public class AsyncNodeRewardTask {

    public static final int TIME_OUT = 2;
    @Autowired
    BlockFacetService blockFacetService;

    @Autowired
    HpbCampaignPeriodVoteResultMapper hpbCampaignPeriodVoteResultMapper;
    public Logger log = LoggerFactory.getLogger(AsyncNodeRewardTask.class);

    @Async
    public void syncNodeNameOfNodeList(CountDownLatch countDownLatch, NodeDailyRewardVo nodeDailyRewardVo,List<NodeDailyRewardVo>  list){
        try {
            nodeDailyRewardVo.setNodeName(getNodeNameByAddress(nodeDailyRewardVo.getNodeAddress()));
            list.add(nodeDailyRewardVo);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            countDownLatch.countDown();
        }
    }

    private String getNodeNameByAddress(String address) {
        String nodeName = "";
        HpbCampaignPeriodVoteResultExample example = new HpbCampaignPeriodVoteResultExample();
        HpbCampaignPeriodVoteResultExample.Criteria criteria = example.createCriteria();
        if(StringUtils.isNotEmpty(address)){
            criteria.andNodeAddressEqualTo(address);
        }
        List<HpbCampaignPeriodVoteResult> hpbCampaignPeriodVoteResultList =  hpbCampaignPeriodVoteResultMapper.selectByExample(example);
        if(CollectionUtils.isNotEmpty(hpbCampaignPeriodVoteResultList)){
            HpbCampaignPeriodVoteResult result = hpbCampaignPeriodVoteResultList.get(0);
            nodeName = result.getNodeName();
        }
        return  nodeName;
    }
}
