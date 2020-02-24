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
