package com.hpb.bc.service.impl;

import com.hpb.bc.async.AsyncNodeRewardTask;
import com.hpb.bc.entity.HpbCampaignPeriodVoteResult;
import com.hpb.bc.entity.HpbNodeRewardRecord;
import com.hpb.bc.example.HpbCampaignPeriodVoteResultExample;
import com.hpb.bc.mapper.HpbCampaignPeriodVoteResultMapper;
import com.hpb.bc.mapper.HpbNodeRewardRecordMapper;
import com.hpb.bc.model.NodeRewardStaticsQueryModel;
import com.hpb.bc.service.HpbNodeRewardStatisticsService;
import com.hpb.bc.util.DateUtils;
import com.hpb.bc.vo.NodeDailyRewardVo;
import com.hpb.bc.vo.NodeStaticsVo;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.util.StringUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@Service
public class HpbNodeRewardStatisticsServiceImpl implements HpbNodeRewardStatisticsService {

    @Autowired
    HpbNodeRewardRecordMapper hbNodeRewardRecordMapper;

    @Autowired
    HpbCampaignPeriodVoteResultMapper hpbCampaignPeriodVoteResultMapper;

    @Autowired
    AsyncNodeRewardTask asyncNodeRewardTask;


    @Override
    public List<HpbNodeRewardRecord> queryNodeReward(NodeRewardStaticsQueryModel  nodeRewardQueryModel) {


        List<HpbNodeRewardRecord>  hpbNodeRewardRecordList = hbNodeRewardRecordMapper.selectNodeRewardList(nodeRewardQueryModel.getAddress(),nodeRewardQueryModel.getStartBlockTimestamp(),nodeRewardQueryModel.getEndBlockTimestamp());
      return  hpbNodeRewardRecordList;


    }

    private String getNodeNameByAddress(String address) {
        //查询名称；
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


    @Override
    public List<NodeStaticsVo> queryNodeRewardList(NodeRewardStaticsQueryModel nodeRewardQueryModel) {
        List<HpbNodeRewardRecord> list = this.queryNodeReward(nodeRewardQueryModel);
        List<NodeStaticsVo> resultList = new ArrayList<>();
        resultList = list.stream().map(e ->{
            NodeStaticsVo v = new NodeStaticsVo();
            v.setAddress(e.getNodeAddress());
            v.setTotalVoteAmount(e.getTotalVoteAmount());
            v.setBalance(String.valueOf(e.getBalance()));
            v.setVoteRate(String.valueOf(e.getRewardVotePercentRate()));
            v.setNodeType(String.valueOf(e.getNodeType()));
            v.setMinedRewardAmount(String.valueOf(e.getNodeMintRewardAmount()));
            v.setVoteRewardAmount(String.valueOf(e.getNodeVoteRewardAmount()));
            v.setTotalRewardAmount(String.valueOf(e.getNodeTotalRewardAmount()));
            v.setBlockNumber(e.getBlockNumber());
            v.setBlockTime( DateUtils.timeStamp2Date(String.valueOf(e.getBlockTimestamp()),DateUtils.DATE_FULL_STR));
            return  v;
        }).collect(toList());

        return  resultList;
    }


    @Override
    public List<NodeDailyRewardVo> queryNodeDailyRewardVoList(NodeRewardStaticsQueryModel nodeRewardQueryModel) {
        List<NodeDailyRewardVo> nodeDailyRewardVoList =  hbNodeRewardRecordMapper.selectNodeDailyRewardList(nodeRewardQueryModel.getAddress(),nodeRewardQueryModel.getStartBlockTimestamp(),nodeRewardQueryModel.getEndBlockTimestamp());
        List<NodeDailyRewardVo> resultNodeDailyRewardVoList = getNodeNameOfNodeDailyRewardVos(nodeRewardQueryModel, nodeDailyRewardVoList);
        return  resultNodeDailyRewardVoList;
    }

    @Override
    public List<NodeDailyRewardVo> queryNodeMonthRewardVoList(NodeRewardStaticsQueryModel nodeRewardQueryModel) {
        List<NodeDailyRewardVo>  nodeDailyRewardVoList = hbNodeRewardRecordMapper.selectNodeMonthRewardList(nodeRewardQueryModel.getAddress(),nodeRewardQueryModel.getStartBlockTimestamp(),nodeRewardQueryModel.getEndBlockTimestamp());
        List<NodeDailyRewardVo> resultNodeDailyRewardVoList = getNodeNameOfNodeDailyRewardVos(nodeRewardQueryModel, nodeDailyRewardVoList);
        return  resultNodeDailyRewardVoList;
    }

    private List<NodeDailyRewardVo> getNodeNameOfNodeDailyRewardVos(NodeRewardStaticsQueryModel nodeRewardQueryModel, List<NodeDailyRewardVo> nodeDailyRewardVoList) {
        List<NodeDailyRewardVo>  resultNodeDailyRewardVoList = new ArrayList<>();
        if(CollectionUtils.isNotEmpty(nodeDailyRewardVoList)){
            if(StringUtils.isNotEmpty(nodeRewardQueryModel.getAddress())){
                String nodeName = this.getNodeNameByAddress(nodeRewardQueryModel.getAddress());
                resultNodeDailyRewardVoList  = nodeDailyRewardVoList.stream().map(e ->{
                    if(StringUtil.isEmpty(e.getNodeName())){
                        e.setNodeName(nodeName);
                    }
                    return e;
                }).collect(toList());
            }
        }
        return resultNodeDailyRewardVoList;
    }


    @Override
    public List<NodeDailyRewardVo> queryNodeDailyRewardAssignedList(String queryDateTime) {
        List<NodeDailyRewardVo> list = hbNodeRewardRecordMapper.selectNodeDailyRewardAssignedList(queryDateTime);
        if(CollectionUtils.isNotEmpty(list)){
            setNodeNameOfList(list);
        }
        return  list;
    }

    @Override
    public List<NodeDailyRewardVo> queryNodeMonthRewardAssignedList(String queryDateTime) {

        List<NodeDailyRewardVo> list = hbNodeRewardRecordMapper.selectNodeMonthRewardAssignedList(queryDateTime);
        if(CollectionUtils.isNotEmpty(list)){
            setNodeNameOfList(list);
        }
        return  list;
    }


    private  void setNodeNameOfList(List<NodeDailyRewardVo> nodeDailyRewardVoList){
        try {
            List<NodeDailyRewardVo> resultModelList = Collections.synchronizedList(new ArrayList<NodeDailyRewardVo>());
            CountDownLatch countDownLatch = new CountDownLatch(nodeDailyRewardVoList.size());
            for(int i = 0; i<nodeDailyRewardVoList.size();i++){
                NodeDailyRewardVo nodeDailyRewardVo =  nodeDailyRewardVoList.get(i);
                asyncNodeRewardTask.syncNodeNameOfNodeList(countDownLatch,nodeDailyRewardVo,resultModelList);
            }
            countDownLatch.await(2, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
