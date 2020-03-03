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