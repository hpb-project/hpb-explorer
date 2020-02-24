package com.hpb.bc.contracts;

import io.hpb.web3.abi.FunctionEncoder;
import io.hpb.web3.abi.FunctionReturnDecoder;
import io.hpb.web3.abi.TypeReference;
import io.hpb.web3.abi.datatypes.Address;
import io.hpb.web3.abi.datatypes.Function;
import io.hpb.web3.abi.datatypes.Type;
import io.hpb.web3.abi.datatypes.generated.Uint256;
import io.hpb.web3.protocol.admin.Admin;
import io.hpb.web3.protocol.core.DefaultBlockParameterName;
import io.hpb.web3.protocol.core.methods.request.Transaction;
import io.hpb.web3.protocol.core.methods.response.HpbCall;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class HpbContractProxyHelper {

    public static final String FUNC_GETLASTESTBALLOTADDRANDINDEX = "getLastestBallotAddrAndIndex";

    public static final String FUNC_NODEBALLOTADDRINSERVICE = "nodeBallotAddrInService";

    @Autowired
    Admin admin;
    public Logger log = LoggerFactory.getLogger(HpbContractProxyHelper.class);

   public Map<String,Object> getLastestBallotAddrAndIndex(){
       Map<String,Object> stringObjectMap = new HashMap<>();
       try {
           final Function function = new Function(FUNC_GETLASTESTBALLOTADDRANDINDEX,
                   Arrays.<Type>asList(),
                   Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Uint256>() {}));
           String encodedFunction = FunctionEncoder.encode(function);
           Transaction hpbCallTransaction = Transaction.createHpbCallTransaction("0xdcdf1ac0b9f1a8ac4cbf03beb864cfcee6e92f18", "0xdcdf1ac0b9f1a8ac4cbf03beb864cfcee6e92f18", encodedFunction);
           HpbCall hpbCall = admin.hpbCall(hpbCallTransaction, DefaultBlockParameterName.LATEST).send();
           String value = hpbCall.getValue();
           List<Type> results = FunctionReturnDecoder.decode(value, function.getOutputParameters());
           if(CollectionUtils.isNotEmpty(results)){
                      String address =    (String) results.get(0).getValue();
                      BigInteger termBigInteger = (BigInteger) results.get(1).getValue();
               stringObjectMap.put("address",address);
               stringObjectMap.put("term",termBigInteger);
           }

       } catch (IOException e) {
           e.printStackTrace();
       }
       return  stringObjectMap;
   };


   public   String getNodeBallotAddressInService(){
       String address = "";
       try {
           final Function function = new Function(FUNC_NODEBALLOTADDRINSERVICE,
                   Arrays.<Type>asList(),
                   Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
           String encodedFunction = FunctionEncoder.encode(function);
           Transaction hpbCallTransaction = Transaction.createHpbCallTransaction("0xdcdf1ac0b9f1a8ac4cbf03beb864cfcee6e92f18", "0xdcdf1ac0b9f1a8ac4cbf03beb864cfcee6e92f18", encodedFunction);
           HpbCall hpbCall = admin.hpbCall(hpbCallTransaction, DefaultBlockParameterName.LATEST).send();
           String value = hpbCall.getValue();
           List<Type> results = FunctionReturnDecoder.decode(value, function.getOutputParameters());
           if(CollectionUtils.isNotEmpty(results)){
               address = results.get(0).getValue().toString();
           }
       } catch (IOException e) {
           e.printStackTrace();
       }

       return  address;
   }

    public String getNodeContractAddressInCampaign() {
        Map<String, Object> lastestBallotAddrAndIndex = this.getLastestBallotAddrAndIndex();
        String address = "";
        if(lastestBallotAddrAndIndex.containsKey("address")){
            address =  lastestBallotAddrAndIndex.get("address").toString();
        }
        if(lastestBallotAddrAndIndex.containsKey("term")){
            BigInteger termBigInteger = BigInteger.valueOf(Long.valueOf(lastestBallotAddrAndIndex.get("term").toString()));
        }
        return  address;
    }




}
