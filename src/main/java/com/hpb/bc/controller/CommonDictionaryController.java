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

import com.hpb.bc.entity.CommonDictionary;
import com.hpb.bc.entity.result.Result;
import com.hpb.bc.entity.result.ResultCode;
import com.hpb.bc.service.CommonDictionaryService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/dict")
public class CommonDictionaryController {

    @Autowired
    CommonDictionaryService commonDictionaryService;

    @ApiOperation(value = "查询字典", notes = "根据groupName查看字典")
    @GetMapping("/list")
    public List<Object> getDictionaryByGroupName(@RequestParam(value = "groupName", required = true) String groupName) {

        List<CommonDictionary> commonDictionaryList = commonDictionaryService.getDictionaryByGroupName(groupName);
        Result<Object> result = new Result<>(ResultCode.SUCCESS, commonDictionaryList);
        return result.getValue();
    }
}
