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

import com.hpb.bc.service.RedisTriggerService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/trigger")
public class RedisTriggerController extends BaseController {

    @Autowired
    RedisTriggerService redisTriggerService;

    @ApiOperation(value = "触发redis定时任务，缓存交易较多的区块", notes = "无参")
    @GetMapping("/pushBlock")
    public String pushBlockIndoRedis(HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            redisTriggerService.pushBlockIntoRedis();
            return "success";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "fail";
    }


}