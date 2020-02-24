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
