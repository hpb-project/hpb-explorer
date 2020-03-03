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

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class LogProcessUtil {
    public static final String LOG_ENTRY = IOUtils.LINE_SEPARATOR + "接收请求[标识号:\"{}\"],"
            + IOUtils.LINE_SEPARATOR + "[开始进入class:\"{}#method:{}\"]," +
            IOUtils.LINE_SEPARATOR + "[请求地址:\"{}\"," + IOUtils.LINE_SEPARATOR + "传递参数:{}"
            + IOUtils.LINE_SEPARATOR + "]";
    public static final String LOG_EXIT = IOUtils.LINE_SEPARATOR + "响应请求[标识号:\"{}\"],"
            + IOUtils.LINE_SEPARATOR + "[结束退出class:\"{}#method:{}\"]";
    public static final String LOG_ERROR = IOUtils.LINE_SEPARATOR + "处理请求[标识号:\"{}\"],"
            + IOUtils.LINE_SEPARATOR + "[发生错误class:\"{}#method:{}\"],"
            + IOUtils.LINE_SEPARATOR + "[报错信息:\"{}\"]";
    public static final ThreadLocal<Logger> logHolder = new ThreadLocal<Logger>() {
        @Override
        public Logger initialValue() {
            return super.initialValue();
        }
    };

    public static String getProccessId(Object randomSrc) {
        return UUIDGeneratorUtil.generate(randomSrc);
    }

    public static final Logger getLog(Class<?> c) {
        Logger log = logHolder.get();
        if (log == null) {
            log = LoggerFactory.getLogger(c);
        }
        return log;
    }

    public static String getProccessId() {
        return new UUIDGeneratorUtil().generate();
    }

    public static void LOG_ENTRY(Class<?> c, String methodName,
                                 String proccessId, String requestUrl, Object param) {
        getLog(c).info(LOG_ENTRY, proccessId, c.getName(), methodName,
                requestUrl, AppObjectUtil.toJson(param));
    }

    public static void LOG_EXIT(Class<?> c, String proccessId, String methodName) {
        getLog(c).info(LOG_EXIT, proccessId, c.getName(), methodName);
    }

    public static void LOG_ERROR(Class<?> c, String methodName,
                                 String proccessId, Object param) {
        getLog(c).error(LOG_ERROR, proccessId, c.getName(), methodName, param);
    }
}
