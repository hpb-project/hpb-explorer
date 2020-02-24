package com.hpb.bc.controller;

import com.hpb.bc.constant.ApiConstant;
import com.hpb.bc.service.ApiFacadeService;
import com.hpb.bc.util.RequestHandleUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Map;

/**
 * @author will
 * api封装统一路由
 */
@RestController
@RequestMapping("/api")
public class ApiController extends BaseController {

    @Autowired
    ApiFacadeService apiFacadeService;
    private static final Logger log = LoggerFactory.getLogger(ApiController.class);

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> doExcute(HttpServletRequest request) {
        Map<String, String> reqParam = RequestHandleUtil.getReqParam(request);
        Object object = null;
        try {
            object = apiFacadeService.doExcute(reqParam);
        } catch (Exception e) {
            log.error("【*********** ApiController exception，error msg {}********** 】", e.getMessage(), e);

        }
        return new ResponseEntity<>(object, HttpStatus.CREATED);
    }
}
