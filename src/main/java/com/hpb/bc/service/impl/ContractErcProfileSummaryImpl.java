package com.hpb.bc.service.impl;

import com.alibaba.fastjson.JSON;
import com.hpb.bc.entity.ContractErcProfileSummary;
import com.hpb.bc.entity.ContractErcProfileSummaryApprove;
import com.hpb.bc.entity.ContractErcStandardInfo;
import com.hpb.bc.example.ContractErcProfileSummaryApproveExample;
import com.hpb.bc.mapper.ContractErcProfileSummaryApproveMapper;
import com.hpb.bc.mapper.ContractErcProfileSummaryMapper;
import com.hpb.bc.mapper.ContractErcStandardInfoMapper;
import com.hpb.bc.model.ContractApproveModel;
import com.hpb.bc.model.ContractErcProfileSummaryAproveModel;
import com.hpb.bc.model.ContractErcProfileSummaryModel;
import com.hpb.bc.service.ContractErcProfileSummaryService;
import com.hpb.bc.service.OSSImageService;
import com.hpb.bc.util.Sign;
import io.hpb.web3.protocol.admin.Admin;
import io.hpb.web3.rlp.RlpDecoder;
import io.hpb.web3.rlp.RlpList;
import io.hpb.web3.rlp.RlpString;
import io.hpb.web3.utils.Numeric;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.util.Date;
import java.util.List;


@Service
public class ContractErcProfileSummaryImpl implements ContractErcProfileSummaryService {

    private static final Logger logger = LoggerFactory.getLogger(ContractErcProfileSummaryImpl.class);

    @Autowired
    ContractErcProfileSummaryMapper contractErcProfileSummaryMapper;

    @Autowired
    ContractErcProfileSummaryApproveMapper  contractErcProfileSummaryApproveMapper;

    @Autowired
    ContractErcStandardInfoMapper contractErcStandardInfoMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean submitContractMessageHolders(ContractErcProfileSummaryModel model) {
        String sign = model.getSign();
        String form = model.getForm();
        ContractErcProfileSummaryModel.ContractErcProfileSummaryInner formContent = getFormContent(form);
        if("Y".equals(model.getApproveFlag())){
            ContractErcProfileSummaryAproveModel.ContractErcProfileSummaryInner x = new ContractErcProfileSummaryAproveModel.ContractErcProfileSummaryInner();
            BeanUtils.copyProperties(formContent,x);
            insert2ContractErcProfileSummaryAproviceTable(x);
            return true;
        }else{
            boolean hasNotAlerted = verifyHasNotAlerted(form, sign);
            if (hasNotAlerted == false) {
                return false;
            }

            boolean validate = validate(formContent, sign);
            if (validate == false) {
                return false;
            }

            String contractAddress = formContent.getContractAddress();
            boolean isNotContractHolder = verifyIsNotContractHolder(contractAddress, sign);
            if (isNotContractHolder == false) {
                return false;
            }
            insert2ContractErcProfileSummaryTable(formContent);
            insert2ContractErcStandardInfoTable(formContent);
            return true;
        }

    }

    private void insert2ContractErcStandardInfoTable(ContractErcProfileSummaryModel.ContractErcProfileSummaryInner formContent) {
        contractErcStandardInfoMapper.updateSymbolUrl(formContent.getContractAddress(), formContent.getTokenSymbolImageUrl());
    }

    private void insert2ContractErcProfileSummaryTable(ContractErcProfileSummaryModel.ContractErcProfileSummaryInner formContent) {
        formContent.setUpdateTime(new Date());
        int i = contractErcProfileSummaryMapper.updateMoreInfo(formContent);
        if (i <= 0) {
            setContractInfo(formContent);
            formContent.setCreateTime(new Date());
            contractErcProfileSummaryMapper.saveMoreInfo(formContent);
        }
    }

    private void insert2ContractErcProfileSummaryAproviceTable(ContractErcProfileSummaryAproveModel.ContractErcProfileSummaryInner formContent) {
        formContent.setUpdateTime(new Date());
        formContent.setApproveStatus("T");// T 待审批
        int i = contractErcProfileSummaryApproveMapper.updateMoreInfo(formContent);
        if (i <= 0) {
            setApproveContractInfo(formContent);
            formContent.setCreateTime(new Date());
            formContent.setApproveStatus("T");
            contractErcProfileSummaryApproveMapper.saveMoreInfo(formContent);
        }
    }

    private void setContractInfo(ContractErcProfileSummaryModel.ContractErcProfileSummaryInner formContent) {
        String contractAddress = formContent.getContractAddress();
        ContractErcStandardInfo info = contractErcStandardInfoMapper.selectByContractAddress(contractAddress);
        if (info != null) {
            String contractType = info.getContractType();
            String tokenSymbol = info.getTokenSymbol();
            formContent.setContractType(contractType);
            formContent.setTokenName(tokenSymbol);
        }
    }

    private void setApproveContractInfo(ContractErcProfileSummaryAproveModel.ContractErcProfileSummaryInner formContent) {
        String contractAddress = formContent.getContractAddress();
        ContractErcStandardInfo info = contractErcStandardInfoMapper.selectByContractAddress(contractAddress);
        if (info != null) {
            String contractType = info.getContractType();
            String tokenSymbol = info.getTokenSymbol();
            formContent.setContractType(contractType);
            formContent.setTokenName(tokenSymbol);
        }
    }



    private boolean verifyIsNotContractHolder(String contractAddress, String sign) {
        //查询到合约持有人地址
        String contractCreator = getContractCreator(contractAddress);
        if (StringUtils.isEmpty(contractCreator) || contractCreator.length() < 2) {
            return false;
        }
        //验签
        return verifySign(sign, contractCreator.substring(2));
    }

    private String getContractCreator(String contractAddress) {
        ContractErcStandardInfo contractInfo = contractErcStandardInfoMapper.selectByContractAddress(contractAddress);
        if (contractInfo == null) {
            return null;
        }
        return contractInfo.getContractCreater();
    }

    private boolean verifySign(String sign, String holderAddress) {
        try {
            return Sign.verify(sign, holderAddress);
        } catch (SignatureException e) {
            e.printStackTrace();
            return false;
        }
    }

    private ContractErcProfileSummaryModel.ContractErcProfileSummaryInner getFormContent(String form) {
        ContractErcProfileSummaryModel.ContractErcProfileSummaryInner formContent = null;
        try {
            formContent = JSON.parseObject(form, ContractErcProfileSummaryModel.ContractErcProfileSummaryInner.class);
        } catch (Exception e) {
            logger.error("JSON解析失败,错误信息:{}", e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("表单json解析失败");
        }
        return formContent;
    }

    private boolean validate(ContractErcProfileSummaryModel.ContractErcProfileSummaryInner formContent, String sign) {
        String contractAddress = formContent.getContractAddress();
        if (StringUtils.isBlank(contractAddress)) {
            return false;
        }
        if (StringUtils.isBlank(sign)) {
            return false;
        }
        return true;
    }

    private boolean verifyHasNotAlerted(String form, String sign) {
        String plainText = getPlainTextBySign(sign);
        String md5PlainText = mkMd5Msg(form);

        return plainText.equals(md5PlainText);
    }

    private String getPlainTextBySign(String sign) {
        try {
            byte[] message = Numeric.hexStringToByteArray(sign);
            RlpList returnList = RlpDecoder.decode(message);
            RlpList rlpList = (RlpList) returnList.getValues().get(0);
            byte[] msg = ((RlpString) rlpList.getValues().get(0)).getBytes();
            return new String(msg, StandardCharsets.UTF_8);
        } catch (Exception e) {
            return new String();
        }
    }

    private String mkMd5Msg(String msg) {
        if (StringUtils.isEmpty(msg)) {
            return null;
        }
        try {
            MessageDigest digest = MessageDigest.getInstance("md5");
            byte[] result = digest.digest(msg.getBytes());
            StringBuffer buffer = new StringBuffer();
            for (byte b : result) {
                int number = b & 0xff;// 加盐
                String str = Integer.toHexString(number);
                if (str.length() == 1) {
                    buffer.append("0");
                }
                buffer.append(str);
            }
            return buffer.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }

    }

    @Override
    public ContractErcProfileSummary queryContractMessageHolders(String contactAddress) {
        ContractErcProfileSummary summary = contractErcProfileSummaryMapper.selectMoreInfoByAddress(contactAddress);
        if (summary == null) {
            return new ContractErcProfileSummary();
        }
        return summary;
    }


    @Override
    public List<ContractErcProfileSummaryApprove> queryContractErcProfileSummaryApproveList(ContractApproveModel contractApproveModel) {
        ContractErcProfileSummaryApproveExample example = new ContractErcProfileSummaryApproveExample();
        ContractErcProfileSummaryApproveExample.Criteria criteria =  example.createCriteria();
        if(StringUtils.isNotEmpty(contractApproveModel.getApproveStatus())){
            criteria.andApproveStatusEqualTo(contractApproveModel.getApproveStatus());
        }
        return  contractErcProfileSummaryApproveMapper.selectByExample(example);
    }


    @Override
    public boolean approveContractErcProifleSummary( ContractApproveModel contractApproveModel){
        ContractErcProfileSummaryApprove contractErcProfileSummaryApprove = contractErcProfileSummaryApproveMapper.selectMoreInfoByAddressAndApproveStatus(contractApproveModel.getContractAddress(), null);
        ContractErcProfileSummaryAproveModel.ContractErcProfileSummaryInner X = new ContractErcProfileSummaryAproveModel.ContractErcProfileSummaryInner();
        BeanUtils.copyProperties(contractErcProfileSummaryApprove, X);
        if ("N".equals(contractApproveModel.getApproveStatus())) {
            X.setApproveStatus("N");
            int x = contractErcProfileSummaryApproveMapper.updateMoreInfo(X);
            return x > 0 ? true : false;
        }else if("Y".equals(contractApproveModel.getApproveStatus())) {
            X.setApproveStatus("Y");
            int x = contractErcProfileSummaryApproveMapper.updateMoreInfo(X);
            contractErcStandardInfoMapper.updateSymbolUrl(contractApproveModel.getContractAddress(), X.getTokenSymbolImageUrl());
            return true;
        }
        return false;
    }
}
