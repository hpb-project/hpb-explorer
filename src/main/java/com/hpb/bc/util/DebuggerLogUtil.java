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
