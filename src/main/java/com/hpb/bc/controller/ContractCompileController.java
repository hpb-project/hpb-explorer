package com.hpb.bc.controller;

import com.hpb.bc.configure.ContractConfig;
import com.hpb.bc.configure.Web3Properties;
import com.hpb.bc.entity.ContractInfo;
import com.hpb.bc.entity.result.Result;
import com.hpb.bc.entity.result.ResultCode;
import com.hpb.bc.model.ContractVerifyModel;
import com.hpb.bc.service.ContractService;
import com.hpb.bc.solidity.SolidityCompiler;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


@RestController
@EnableAutoConfiguration
@RequestMapping("/validate-contract")
public class ContractCompileController extends CompileBaseController {
    public Logger log = LoggerFactory.getLogger("contractCompileAppenderLogger");
    @Autowired
    SolidityCompiler solidityCompiler;
    @Autowired
    Web3Properties web3Properties;
    @Autowired
    private ContractConfig contractConfig;
    @Autowired
    private ContractService contractService;


    @Override
    protected Class<?> getBeanClass() {
        return this.getClass();
    }

    @ApiOperation(value = "testCompiler", notes = "testCompiler")
    @PostMapping("/testCompiler")
    public List<Object> testCompiler(HttpServletRequest request) {
        contractService.compileSrc();
        Result<String> result = new Result<>(ResultCode.SUCCESS, "Su");
        return result.getValue();
    }


    @ApiOperation(value = "testCompilerDb", notes = "testCompilerDb")
    @PostMapping("/testCompilerDb")
    public List<Object> testCompilerDb(@RequestBody ContractVerifyModel contractVerifyModel) {
        compilar(contractVerifyModel);
        Result<String> result = new Result<>(ResultCode.SUCCESS, "Su");
        return result.getValue();
    }

    private void compilar(ContractVerifyModel contractVerifyModel) {
        String dockerSolcCmd = contractConfig.getSolcCmd();
        log.info("soliditySrcCode getContractCompilerVersionNumber ==" + contractVerifyModel.getContractCompilerVersionNumber());
        if (StringUtils.isNotEmpty(contractVerifyModel.getContractCompilerVersionNumber())) {
            dockerSolcCmd += contractVerifyModel.getContractCompilerVersionNumber();
        }
        log.info("testCompilerDb dockerSolcCmd ===" + dockerSolcCmd);
        log.info(" solidityCompiler ");
        ContractInfo contractInfo = contractService.getContractInfoByContractAddrs(contractVerifyModel.getContractAddr());
        String soliditySrcCode = contractInfo.getContractSrc();
        log.info("soliditySrcCode ==" + soliditySrcCode);

    }

    @ApiOperation(value = "验证并开源智能合约代码", notes = "新方法便于ContractLibraryAddress,根据编译得到的compileBin 与客户提交的bin一致，就验证通过")
    @PostMapping("/validate/contract")
    public Object validateContractInfoByService(@RequestBody ContractVerifyModel contractVerifyModel) {
        ResponseEntity<Object> objectResponseEntity = contractService.contractVerify(contractVerifyModel);
        return objectResponseEntity.getBody();
    }

}