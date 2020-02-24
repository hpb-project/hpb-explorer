package com.hpb.bc.contracts.abi;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.hpb.bc.entity.ContractEventInfo;
import com.hpb.bc.entity.ContractMethodInfo;
import com.hpb.bc.example.ContractErcStandardInfoExample;
import com.hpb.bc.example.ContractEventInfoExample;
import com.hpb.bc.example.ContractMethodInfoExample;
import com.hpb.bc.mapper.ContractEventInfoMapper;
import com.hpb.bc.mapper.ContractMethodInfoMapper;
import com.hpb.bc.model.HpbEventModel;
import com.hpb.bc.util.ObjectJsonHelper;
import io.hpb.web3.abi.EventEncoder;
import io.hpb.web3.protocol.admin.Admin;
import io.hpb.web3.protocol.core.methods.response.Transaction;
import io.hpb.web3.abi.EventValues;
import io.hpb.web3.abi.FunctionReturnDecoder;
import io.hpb.web3.abi.TypeReference;
import io.hpb.web3.abi.datatypes.*;
import io.hpb.web3.abi.datatypes.generated.AbiTypes;
import io.hpb.web3.protocol.core.methods.response.*;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.math.BigInteger;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import static io.hpb.web3.abi.Utils.convert;
import static io.hpb.web3.tx.Contract.staticExtractEventParameters;

@Component
public class EventHelper {

    @Autowired
    ContractMethodInfoMapper contractMethodInfoMapper;

    @Autowired
    ContractEventInfoMapper contractEventInfoMapper;

    @Autowired
    Admin admin;

    private final static Logger logger = LoggerFactory.getLogger(EventHelper.class);

    private Pattern pattern = Pattern.compile("(\\w+)(?:\\[(.*?)\\])(?:\\[(.*?)\\])?");


    public List<HpbEventModel> getHpbEventModel(Transaction  tx, TransactionReceipt transactionReceipt, String abi) throws JsonParseException, JsonMappingException, IOException {
        String methodId = tx.getInput().substring(0,10);
        ContractMethodInfoExample example = new ContractMethodInfoExample();
        ContractMethodInfoExample.Criteria criteria = example.createCriteria();
        criteria.andMethodIdEqualTo(methodId);
        List<ContractMethodInfo> contractMethodInfoList =   contractMethodInfoMapper.selectByExampleWithBLOBs(example);
        if(CollectionUtils.isEmpty(contractMethodInfoList)){
            return new ArrayList<>() ;
        }
        ContractMethodInfo contractMethodInfo = contractMethodInfoList.get(0);
        String methodName = contractMethodInfo.getMethodName();

        Log logtop =  transactionReceipt.getLogs().get(0);


        String topic = logtop.getTopics().get(0);
        ContractEventInfoExample contractEventInfoExample = new ContractEventInfoExample();
        ContractEventInfoExample.Criteria contractEventInfoCriteria = contractEventInfoExample.createCriteria();
        contractEventInfoCriteria.andEventHashEqualTo(topic);
        List<ContractEventInfo>   contractEventInfoList = contractEventInfoMapper.selectByExampleWithBLOBs(contractEventInfoExample);
        ContractEventInfo contractEventInfo = null;
        if(CollectionUtils.isNotEmpty(contractEventInfoList)){
            contractEventInfo = contractEventInfoList.get(0);
        }else {
            return new ArrayList<>() ;
        }
        abi = contractEventInfo.getEventAbi();


        AbiDefinition abiDefinition = (AbiDefinition) ObjectJsonHelper.deserialize(abi, AbiDefinition.class);
        List<AbiDefinition.NamedType> inputs = abiDefinition.getInputs();


        List<String> nameAndTypeKeyList = new ArrayList<>();
        for(int i = 0;i<inputs.size();i++){
           AbiDefinition.NamedType namedType =  inputs.get(i);
           String inputNameAndType =   namedType.getType() +" " + namedType.getName();
           nameAndTypeKeyList.add(inputNameAndType);
          logger.info(" namedType ==="+inputNameAndType);
        }

        String eventName = abiDefinition.getName();
        List<TypeReference<?>> inputParameterTypes = buildTypeReferenceTypes(inputs);
        Event event = new Event(eventName, inputParameterTypes);
        List<EventValuesWithLog> list = (List) transactionReceipt.getLogs().stream().map((log) -> {
            return extractEventParametersWithLog(event, log);
        }).filter(Objects::nonNull).collect(Collectors.toList());

        List<String>  paramValue = new ArrayList<>();

        for(int i =0; i< list.size();i++){
            EventValuesWithLog evl = list.get(i);
            List<Type> idxValues = evl.getIndexedValues();
            for(int j  =0 ;j< idxValues.size();j ++){
              Type t =   idxValues.get(j);
              logger.info(" getIndexedValues =="+evl.getIndexedValues().get(j).getTypeAsString() +" "+ evl.getIndexedValues().get(j).getValue());
              paramValue.add(t.getValue().toString());
            }
            List<Type> nidxValue = evl.getNonIndexedValues();
            for(int j  =0 ;j< nidxValue.size();j ++){
                Type t = nidxValue.get(j);
                logger.info(" getNonIndexedValues =="+nidxValue.get(j).getTypeAsString() +" "+ nidxValue.get(j).getValue());
                paramValue.add(t.getValue().toString());
            }
        }

        Map<String, Object> detailParaMap =  setEventDetailParam(logtop, nameAndTypeKeyList,paramValue);


        List<HpbEventModel> resultList = null;
        try {
            HpbBlock.Block hpbBlock = admin.hpbGetBlockByHash(tx.getBlockHash(),false).sendAsync().get(2, TimeUnit.MINUTES).getBlock();

            resultList = new ArrayList<>();
            HpbEventModel hpbEventModel = new HpbEventModel();
            hpbEventModel.setMethodId(methodId);
            hpbEventModel.setMethodName(methodName);
            hpbEventModel.setBlockNumber(tx.getBlockNumber());
            hpbEventModel.setBlockTimestamp(hpbBlock.getTimestamp());
            hpbEventModel.setTxHash(tx.getHash());
            hpbEventModel.setLogList(transactionReceipt.getLogs());
            hpbEventModel.setEventName(contractEventInfo.getEventName());
            hpbEventModel.setDetailParaMap(detailParaMap);
            resultList.add(hpbEventModel);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
        return resultList;
    }

    private Map<String,Object> setEventDetailParam(Log logtop,  List<String> nameAndTypeKeyList,  List<String> paramValue) {
        Map<String,Object> detailParaMap = new HashMap<>();
        List<String> topics = logtop.getTopics();
        String value = logtop.getData();
        for(int i =0; i< nameAndTypeKeyList.size(); i ++){
         String nameAndTypeKey =   nameAndTypeKeyList.get(i);
            detailParaMap.put(nameAndTypeKey,paramValue.get(i));
        }
        detailParaMap.put("hex_value",value);
        return detailParaMap;
    }


    public List<TypeReference<?>> buildTypeReferenceTypes(List<AbiDefinition.NamedType> namedTypes) {
        List<TypeReference<?>> result = new ArrayList(namedTypes.size());
        for (int i = 0; i < namedTypes.size(); ++i) {
            String type = ((AbiDefinition.NamedType) namedTypes.get(i)).getType();
            result.add(buildTypeName(type, ((AbiDefinition.NamedType) namedTypes.get(i)).isIndexed()));
        }
        return result;
    }


    public TypeReference<Type> buildTypeName(String typeDeclaration, boolean isIndexed) {
        final String trimStorageDeclaration = trimStorageDeclaration(typeDeclaration);
        Matcher matcher = pattern.matcher(trimStorageDeclaration);
        if (matcher.find()) {
            String type = matcher.group(1);
            String firstArrayDimension = matcher.group(2);
            String secondArrayDimension = matcher.group(3);
            TypeReference typeReference = null;
            if ("".equals(firstArrayDimension)) {
                typeReference = AllReferenceType.getDynamicType(type, isIndexed);
            } else {
                typeReference = AllReferenceType.getStaticType(type, Integer.valueOf(firstArrayDimension), isIndexed);
            }
            if(secondArrayDimension != null) {
                ;
            }
            return typeReference;
        } else {
            return new TypeReference<Type>(isIndexed) {
                public java.lang.reflect.Type getType() {
                    return AbiTypes.getType(trimStorageDeclaration);
                }
            };
        }
    }

    public String trimStorageDeclaration(String type) {
        return !type.endsWith(" storage") && !type.endsWith(" memory") ? type : type.split(" ")[0];
    }

    public EventValuesWithLog extractEventParametersWithLog(Event event, Log log) {
        EventValues eventValues = staticExtractEventParameters(event, log);
        return eventValues == null ? null : new EventValuesWithLog(eventValues, log);
    }

}
