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
