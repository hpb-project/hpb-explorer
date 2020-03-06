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
