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

package com.hpb.bc.service.impl;

import com.hpb.bc.entity.Feedback;
import com.hpb.bc.example.FeedbackExample;
import com.hpb.bc.mapper.FeedbackMapper;
import com.hpb.bc.service.FeedbackService;
import com.hpb.bc.util.UUIDGeneratorUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class FeedbackServiceImpl implements FeedbackService {
    @Autowired
    FeedbackMapper feedbackMapper;

    @Override
    public Feedback findFeedbackById(String id) {
        return feedbackMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<Feedback> findFeedbackByContactInformation(String contractInfo) {
        List<Feedback> feedbackList = new ArrayList<>();
        FeedbackExample example = new FeedbackExample();
        example.createCriteria().andContactInformationEqualTo(contractInfo);
        feedbackList = feedbackMapper.selectByExample(example);
        return feedbackList;
    }

    @Override
    public boolean saveOrUpdateFeedback(Feedback feedback) {
        if (feedback == null) {
            return false;
        }
        if (StringUtils.isEmpty(feedback.getId())) {
            feedback.setId(UUIDGeneratorUtil.get32UUID());
            int x = feedbackMapper.insert(feedback);
            if (x > 0) {
                return true;
            }
            {
                return false;
            }
        } else {
            int r = feedbackMapper.updateByPrimaryKey(feedback);
            if (r > 0) {
                return true;
            }
            {
                return false;
            }
        }
    }
}
