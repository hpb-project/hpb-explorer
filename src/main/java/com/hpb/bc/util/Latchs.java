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

package com.hpb.bc.util;


import com.google.common.collect.Lists;
import org.apache.commons.collections4.CollectionUtils;
import org.elasticsearch.common.util.concurrent.CountDown;
import org.omg.PortableServer.THREAD_POLICY_ID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * @author lij <email=jian.li@hpb.io>
 * @CreatTime 2019/9/5 10:47
 * @Desc 类说明
 */
public class Latchs {

    private static final Logger logger = LoggerFactory.getLogger(Latchs.class);

    /**
     * 获取并发锁
     *
     * @param subThreadAmount
     * @return
     */
    public static CountDownLatch newCountDownLatch(int subThreadAmount) {
        return new CountDownLatch(subThreadAmount);
    }

    public static CountDownLatch newCountDownLatch(List list) {
        if (CollectionUtils.isNotEmpty(list)) {
            return new CountDownLatch(list.size());
        }
        return new CountDownLatch(0);
    }


    public static void await(CountDownLatch latch) {
        try {
            latch.await(30, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 自己手动countDown,自定义表的次数
     * @param list              待遍历的集合
     * @param latchSize         表的长度
     * @param consumer          任务消费者
     * @param <T>
     */
    public static <T> void mapReduceBySelf(List<T> list, int latchSize, BiConsumer<T,CountDownLatch> consumer){
        if (CollectionUtils.isEmpty(list)) {
            return;
        }
        CountDownLatch latch = new CountDownLatch(latchSize);
        for (T t : list) {
            //这里大部分情况下是异步任务
            consumer.accept(t,latch);
        }
        Latchs.await(latch);
    }

    /**
     * 自己手动countDown,自定义表的次数
     * @param list              待遍历的集合
     * @param consumer          任务执行者
     * @param <T>
     */
    public static <T> void mapReduce(List<T> list, BiConsumer<T,CountDownLatch> consumer){
        if (CollectionUtils.isEmpty(list)) {
            return;
        }
        CountDownLatch latch = newCountDownLatch(list.size());
        for (T t : list) {
            //这里大部分情况下是异步任务
            consumer.accept(t,latch);
        }
        Latchs.await(latch);
    }

    /**
     * 同步任务结束后自动countDown,并打印错误日志
     * @param consumer
     * @param <T>
     */
    public static <T> void countDownAfterTask(T t,CountDownLatch latch, Consumer<T> consumer){
        try {
            consumer.accept(t);
        }catch (Throwable ex){
            logger.error(ex.getMessage());
            ex.printStackTrace();
        }finally {
            latch.countDown();
        }
    }

}


