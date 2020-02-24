package com.hpb.bc.service;

import com.hpb.bc.entity.Feedback;
import com.hpb.bc.example.FeedbackExample;

import java.util.List;

/**
 * 消息反馈
 */
public interface FeedbackService {
    /***
     * @param id
     * 根据查询反馈记录
     */
    Feedback findFeedbackById(String id);

    /***
     * @param contractInfo
     *    根据联系方式查找反馈；
     */
    List<Feedback> findFeedbackByContactInformation(String contractInfo);

    /**
     * 保存反馈记录
     *
     * @Para feedback
     */
    boolean saveOrUpdateFeedback(Feedback feedback);
}
