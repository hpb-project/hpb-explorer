package com.hpb.bc.controller;

import com.hpb.bc.util.LogProcessUtil;
import com.hpb.bc.util.UUIDGeneratorUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;


/**
 * @author ThinkPad
 */
public abstract class CompileBaseController {

    private static final Logger log = LoggerFactory.getLogger(BaseController.class);

    protected Logger getLog() {
        return LogProcessUtil.getLog(getBeanClass());
    }

    protected abstract Class<?> getBeanClass();
}