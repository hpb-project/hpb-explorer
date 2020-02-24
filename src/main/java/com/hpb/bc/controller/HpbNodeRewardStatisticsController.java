package com.hpb.bc.controller;

import com.hpb.bc.entity.result.Result;
import com.hpb.bc.entity.result.ResultCode;
import com.hpb.bc.mapper.HpbCampaignPeriodVoteResultMapper;
import com.hpb.bc.model.NodeRewardStaticsQueryModel;
import com.hpb.bc.model.RewardAssignedQueryModel;
import com.hpb.bc.service.HpbNodeRewardStatisticsService;
import com.hpb.bc.util.ExcelUtils;
import com.hpb.bc.vo.NodeDailyRewardVo;
import com.hpb.bc.vo.NodeStaticsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping("/reward-statistics")
public class HpbNodeRewardStatisticsController {


    @Autowired
    HpbNodeRewardStatisticsService hpbNodeRewardStatisticsService;

    @Autowired
    HpbCampaignPeriodVoteResultMapper hpbCampaignPeriodVoteResultMapper;

    @PostMapping("/down-queryNodeRewardList")
    public String downQueryNodeRewardList(HttpServletResponse response, @RequestBody NodeRewardStaticsQueryModel nodeRewardStaticsQueryModel) {
        try {
            //调用service查询方法返回结果集
            List<NodeStaticsVo> hpbNodeRewardRecordList = hpbNodeRewardStatisticsService.queryNodeRewardList(nodeRewardStaticsQueryModel);
            ExcelUtils.exportExcel(hpbNodeRewardRecordList, nodeRewardStaticsQueryModel.getAddress(), "reward", NodeStaticsVo.class, nodeRewardStaticsQueryModel.getAddress() + "_reward.xls", response);
            return "Success";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "Fail";

    }
    // // 导出操作
    //


    @PostMapping("/queryNodeRewardList")
    public List<Object> queryNodeRewardList(HttpServletResponse response, @RequestBody NodeRewardStaticsQueryModel nodeRewardStaticsQueryModel) {
        List<NodeStaticsVo> hpbNodeRewardRecordList = hpbNodeRewardStatisticsService.queryNodeRewardList(nodeRewardStaticsQueryModel);
        Result<Object> result = new Result<>(ResultCode.SUCCESS, hpbNodeRewardRecordList);
        return result.getValue();
    }


    @PostMapping("/down-queryNodeDailyReward")
    public String downQueryNodeDailyReward(HttpServletRequest request, HttpServletResponse response, @RequestBody NodeRewardStaticsQueryModel nodeRewardStaticsQueryModel) {
        try {
            List<NodeDailyRewardVo> rewardVoList = hpbNodeRewardStatisticsService.queryNodeDailyRewardVoList(nodeRewardStaticsQueryModel);
            //
            ExcelUtils.exportExcel(rewardVoList, nodeRewardStaticsQueryModel.getAddress(), "reward", NodeDailyRewardVo.class, nodeRewardStaticsQueryModel.getAddress() + "_DailyReward.xls", response);
            return "Success";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "Fail";
    }

    @PostMapping("/queryNodeDailyReward")
    public List<Object> queryNodeDailyReward(@RequestBody NodeRewardStaticsQueryModel nodeRewardStaticsQueryModel) {
        List<NodeDailyRewardVo> rewardVoList = hpbNodeRewardStatisticsService.queryNodeDailyRewardVoList(nodeRewardStaticsQueryModel);
        Result<Object> result = new Result<>(ResultCode.SUCCESS, rewardVoList);
        return result.getValue();
    }


    @PostMapping("/down-queryNodeMonthReward")
    public String downQueryNodeMonthReward(@RequestBody NodeRewardStaticsQueryModel nodeRewardStaticsQueryModel, HttpServletResponse response) {
        try {
            List<NodeDailyRewardVo> rewardVoList = hpbNodeRewardStatisticsService.queryNodeMonthRewardVoList(nodeRewardStaticsQueryModel);
            ExcelUtils.exportExcel(rewardVoList, nodeRewardStaticsQueryModel.getAddress(), "reward", NodeDailyRewardVo.class, nodeRewardStaticsQueryModel.getAddress() + "_MonthReward.xls", response);
            return "Success";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "Fail";
    }

    @PostMapping("/queryNodeMonthReward")
    public List<Object> queryNodeMonthReward(@RequestBody NodeRewardStaticsQueryModel nodeRewardStaticsQueryModel) {
        List<NodeDailyRewardVo> rewardVoList = hpbNodeRewardStatisticsService.queryNodeMonthRewardVoList(nodeRewardStaticsQueryModel);
        Result<Object> result = new Result<>(ResultCode.SUCCESS, rewardVoList);
        return result.getValue();
    }

    @PostMapping("/down-queryNodeDailyRewardAssignedList")
    public String downQueryNodeDailyRewardAssignedList(@RequestBody RewardAssignedQueryModel rewardAssignedQueryModel, HttpServletResponse response) {
        try {
            List<NodeDailyRewardVo> rewardVoList = hpbNodeRewardStatisticsService.queryNodeDailyRewardAssignedList(rewardAssignedQueryModel.getQueryDateTime());
            ExcelUtils.exportExcel(rewardVoList, "节点日奖励分配情况", "reward", NodeDailyRewardVo.class, "_MonthReward.xls", response);
            return "Success";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "Fail";
    }


    @PostMapping("/queryNodeDailyRewardAssignedList")
    public List<Object> queryNodeDailyRewardAssignedList(@RequestBody RewardAssignedQueryModel rewardAssignedQueryModel) {
        List<NodeDailyRewardVo> rewardVoList = hpbNodeRewardStatisticsService.queryNodeDailyRewardAssignedList(rewardAssignedQueryModel.getQueryDateTime());
        Result<Object> result = new Result<>(ResultCode.SUCCESS, rewardVoList);
        return result.getValue();
    }

    @PostMapping("/queryNodeMonthRewardAssignedList")
    public List<Object> queryNodeMonthRewardAssignedList(@RequestBody RewardAssignedQueryModel rewardAssignedQueryModel) {
        List<NodeDailyRewardVo> rewardVoList = hpbNodeRewardStatisticsService.queryNodeMonthRewardAssignedList(rewardAssignedQueryModel.getQueryDateTime());
        Result<Object> result = new Result<>(ResultCode.SUCCESS, rewardVoList);
        return result.getValue();
    }


    @PostMapping("/down-queryNodeMonthRewardAssignedList")
    public String downQueryNodeMonthRewardAssignedList(@RequestBody RewardAssignedQueryModel rewardAssignedQueryModel, HttpServletResponse response) {
        try {

            List<NodeDailyRewardVo> rewardVoList = hpbNodeRewardStatisticsService.queryNodeMonthRewardAssignedList(rewardAssignedQueryModel.getQueryDateTime());
            ExcelUtils.exportExcel(rewardVoList, "节点月奖励分配情况", "reward", NodeDailyRewardVo.class, "_MonthReward.xls", response);
            return "Success";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "Fail";
    }


}
