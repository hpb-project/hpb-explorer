package com.hpb.bc.controller;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import com.hpb.bc.cache.CacheSessionConfiguration;
import com.hpb.bc.constant.BcConstant;
import com.hpb.bc.entity.cache.CacheSession;
import com.hpb.bc.util.UUIDGeneratorUtil;

/**
 * @author ThinkPad
 */
public abstract class BaseController {
    protected static final String PROCESS_ID = "processId";
    protected static final String SERVICE_ID = "serviceId";
    private static final String STATUS_CODE = "javax.servlet.error.status_code";
    private static final int _EXPIRY = -1;
    @Autowired
    protected CacheSessionConfiguration cacheSessionConfiguration;

    protected String getProccessId(HttpServletRequest request) {
        return UUIDGeneratorUtil.generate(request);
    }

    protected HttpStatus getStatus(HttpServletRequest request) {
        Integer statusCode = (Integer) request.getAttribute(STATUS_CODE);
        if (statusCode == null) {
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return HttpStatus.valueOf(statusCode);
    }

    /**
     * 通过某个小型cookie绑定缓存数据key
     *
     * @param request
     * @param response
     * @param methodnName
     * @return
     */
    protected String getSessionId(HttpServletRequest request, HttpServletResponse response) {
        String sessionId = setSessionId(request, response, _EXPIRY);
        CacheSession session = cacheSessionConfiguration.findBySessionId(sessionId);
        if (session != null) {
            cacheSessionConfiguration.update(session);
        }
        return sessionId;
    }

    /**
     * 通过某个小型cookie绑定缓存数据key,并管理缓存的生命周期
     *
     * @param request
     * @param response
     * @param methodnName
     * @param expiry
     * @return
     */
    protected String setSessionId(HttpServletRequest request,
                                  HttpServletResponse response, int expiry) {
        String sessionId = null;
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie c : cookies) {
                if (BcConstant.SESSION_KEY.equals(c.getName())) {
                    sessionId = c.getValue();
                    if (c.getMaxAge() != expiry) {
                        c.setMaxAge(expiry);
                        response.addCookie(c);
                    }
                    break;
                }
            }
        }
        if (StringUtils.isBlank(sessionId)) {
            sessionId = getProccessId(request);
            Cookie cookie = new Cookie(BcConstant.SESSION_KEY, sessionId);
            cookie.setMaxAge(expiry);
            cookie.setHttpOnly(true);
            response.addCookie(cookie);
        }
        return sessionId;
    }

    /**
     * 解析请求对应的参数
     *
     * @param reqStrList
     * @param paramNames
     * @return
     * @throws Exception
     */
    protected Map<String, String> parseReqStrList(List<String> reqStrList,
                                                  List<String> paramNames) throws Exception {
        Map<String, String> reqParam = new LinkedHashMap<String, String>();
        if (CollectionUtils.isNotEmpty(reqStrList)) {
            String processId = (String) reqStrList.get(0);
            reqParam.put(PROCESS_ID, processId);
            if (paramNames != null) {
                int i = 0;
                for (String paramName : paramNames) {
                    i++;
                    reqParam.put(paramName, reqStrList.get(i));
                }
            }
        }
        return reqParam;
    }
}