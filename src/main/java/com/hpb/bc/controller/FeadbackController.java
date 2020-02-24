package com.hpb.bc.controller;

import com.hpb.bc.entity.Feedback;
import com.hpb.bc.entity.result.Result;
import com.hpb.bc.entity.result.ResultCode;
import com.hpb.bc.service.FeedbackService;
import com.hpb.bc.util.UUIDGeneratorUtil;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/feedback")
public class FeadbackController extends BaseController {
    @Autowired
    private FeedbackService feedbackService;

    @ApiOperation(value = "保存反馈信息")
    @PostMapping("")
    public List<Object> saveFeedback(@RequestBody Feedback feedback) throws Exception {
        boolean flag = feedbackService.saveOrUpdateFeedback(feedback);
        Result<Boolean> result = new Result<>(ResultCode.SUCCESS, flag);
        return result.getValue();
    }

    @ApiOperation(value = "修改反馈信息")
    @PutMapping("")
    public List<Object> updateFeedback(@RequestBody Feedback feedback) throws Exception {
        boolean flag = feedbackService.saveOrUpdateFeedback(feedback);
        Result<Boolean> result = new Result<>(ResultCode.SUCCESS, flag);
        return result.getValue();
    }


    @ApiOperation(value = "根据id获取FeedBack信息")
    @GetMapping("/{id}")
    public List<Object> getFeedbackById(@PathVariable("id") String id) throws Exception {
        Feedback feedbackResult = feedbackService.findFeedbackById(id);
        Result<Feedback> result = new Result<>(ResultCode.SUCCESS, feedbackResult);
        return result.getValue();
    }


    @ApiOperation(value = "根据联系方式获取列表")
    @GetMapping("/list")
    public List<Object> getFeedbackListByContactInformation(@RequestParam(value = "contactInformation", required = true) String contactInformation) throws Exception {
        List<Feedback> feedbackList = feedbackService.findFeedbackByContactInformation(contactInformation);
        Result<List<Feedback>> result = new Result<>(ResultCode.SUCCESS, feedbackList);
        return result.getValue();
    }


}