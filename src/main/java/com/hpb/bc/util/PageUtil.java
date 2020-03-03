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

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import org.apache.commons.collections4.CollectionUtils;

import java.util.List;

/**
 * @author lij <email=jian.li@hpb.io>
 * @CreatTime 2019/9/4 17:55
 * @Desc 对分页业务的工具封装
 */
public class PageUtil {


    public static <E> Page<E> startPage(int pageNum, int pageSize) {
        Page<E> page = PageHelper.startPage(pageNum, pageSize);
        PageContextHolder.setLocalPage(page);
        return page;
    }


    public static <E> PageInfo<E> transfer(List<E> newList) {
        if (CollectionUtils.isEmpty(newList)) {
            newList = Lists.newArrayList();
        }
        Page<Object> localPage = PageContextHolder.getLocalPage();
        PageInfo<E> newPageInfo = new PageInfo<>(newList);
        newPageInfo.setPageNum(localPage.getPageNum());
        newPageInfo.setPageSize(localPage.getPageSize());
        newPageInfo.setStartRow(localPage.getStartRow());
        newPageInfo.setEndRow(localPage.getEndRow());
        newPageInfo.setTotal(localPage.getTotal());
        newPageInfo.setPages(localPage.getPages());

        PageContextHolder.clearPage();
        return newPageInfo;
    }


    public static class PageContextHolder {
        private static final ThreadLocal<Page> LOCAL_PAGE = new ThreadLocal<Page>();

        /**
         * 设置 Page 参数
         *
         * @param page
         */
        public static void setLocalPage(Page page) {
            LOCAL_PAGE.set(page);
        }

        /**
         * 获取 Page 参数
         *
         * @return
         */
        public static <T> Page<T> getLocalPage() {
            return LOCAL_PAGE.get();
        }

        /**
         * 移除本地变量
         */
        public static void clearPage() {
            LOCAL_PAGE.remove();
        }


    }

}
