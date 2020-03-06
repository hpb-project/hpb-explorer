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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author lij <email=jian.li@hpb.io>
 * @time 2019/9/6 11:33
 * @Desc 类说明
 */
public class DebuggerLogUtil {

    private static final Logger logger = LoggerFactory.getLogger(DebuggerLogUtil.class);

    /**
     * 协助debug的日志
     *
     * @param msg
     * @param params
     */
    public static void debug(String msg, Boolean flag, Object... params) {
        logger.error("\r\n--------------------------------------------------------------------\r\n--------------------------------------------------------------------\r\n--------------------------------------------------------------------\r\n\r\n\r\n" + msg + "\r\n\r\n\r\n--------------------------------------------------------------------\r\n--------------------------------------------------------------------\r\n--------------------------------------------------------------------", params);
        if (flag != null && flag) {
            throw new RuntimeException("到此结束");
        }
    }

    /**
     * 协助debug的日志
     *
     * @param msg
     * @param params
     */
    public static void debug(String msg, Object... params) {
        debug(msg, true, params);
    }



}
