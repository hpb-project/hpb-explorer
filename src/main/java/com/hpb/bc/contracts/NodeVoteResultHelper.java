package com.hpb.bc.contracts;

import com.hpb.bc.util.GsonUtil;
import io.hpb.web3.abi.FunctionEncoder;
import io.hpb.web3.abi.FunctionReturnDecoder;
import io.hpb.web3.abi.TypeEncoder;
import io.hpb.web3.abi.TypeReference;
import io.hpb.web3.abi.datatypes.*;
import io.hpb.web3.abi.datatypes.generated.Bytes4;
import io.hpb.web3.abi.datatypes.generated.Uint256;
import io.hpb.web3.protocol.admin.Admin;
import io.hpb.web3.protocol.core.DefaultBlockParameterName;
import io.hpb.web3.protocol.core.DefaultBlockParameterNumber;
import io.hpb.web3.protocol.core.methods.request.Transaction;
import io.hpb.web3.protocol.core.methods.response.HpbCall;
import io.hpb.web3.tuples.generated.Tuple4;
import io.hpb.web3.utils.Numeric;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.math.BigInteger;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class NodeVoteResultHelper {

    @Autowired
    Admin admin;

    @Autowired
    HpbContractProxyHelper hpbContractProxyHelper;
    public void test() {
        String contractAddress = "0x9108a228990eb762b7022da28e99067da2458327";
        String nodeAddress = "0x94724bb354b29f0797019506763e14b9d586e7be";
        int stageNumber = 67;
        try {

            Function functionFechSnapshotBlockByIndex = new Function(Hpbballot.FUNC_FECHSNAPSHOTBLOCKBYINDEX,
                    Arrays.<Type>asList(new Uint256(stageNumber)),
                    Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {
                    }));
            String encodedfunctionFechSnapshotBlock = FunctionEncoder.encode(functionFechSnapshotBlockByIndex);
            io.hpb.web3.protocol.core.methods.response.HpbCall hpbCallFechSnapshotBlock = admin.hpbCall(
                    Transaction.createHpbCallTransaction(nodeAddress, contractAddress, encodedfunctionFechSnapshotBlock), DefaultBlockParameterName.LATEST).send();
            String valueFechSnapshotBlock = hpbCallFechSnapshotBlock.getValue();
            List<Type> uint256ListFechSnapshotBlock = FunctionReturnDecoder.decode(valueFechSnapshotBlock, functionFechSnapshotBlockByIndex.getOutputParameters());
            if (uint256ListFechSnapshotBlock.isEmpty()) {

                System.out.println(" 还没有出票 ");
                return;
            }
            Uint256 snapBlockNumber = (Uint256) uint256ListFechSnapshotBlock.get(0);
            System.out.println("snapBlockNumber ===" + snapBlockNumber.getValue());

       
            Function function2 = new Function(Hpbballot.FUNC_FECHALLVOTERESULTBYSNAPSHOTBLOCK,
                    Arrays.<Type>asList(snapBlockNumber),
                    Arrays.<TypeReference<?>>asList(new TypeReference<DynamicArray<Address>>() {
                    }, new TypeReference<DynamicArray<Uint256>>() {
                    }));

            String function2Encode = FunctionEncoder.encode(function2);
            io.hpb.web3.protocol.core.methods.response.HpbCall hpbCallfechVoteNumForCandidateBySnapshotBlock = admin.hpbCall(
                    Transaction.createHpbCallTransaction(nodeAddress, contractAddress, function2Encode), DefaultBlockParameterName.LATEST)
                    .send();
            String valuefechVoteNumForCandidateBySnapshotBlock = hpbCallfechVoteNumForCandidateBySnapshotBlock.getValue();
            List<Type> decodeList = FunctionReturnDecoder.decode(valuefechVoteNumForCandidateBySnapshotBlock, function2.getOutputParameters());
            if (decodeList.isEmpty()) {
                System.out.println("decodeList is empty");
            }

            Type type = decodeList.get(0);
            Type type2 = decodeList.get(1);
            System.out.println("type   ===" + type.getValue());
            System.out.println("type   ===" + type.getValue());
            try {

                Address address = new Address(nodeAddress);
                Function functionGetVoteInfoByStageFun = new Function(Hpbballot.FUNC_FECHVOTERESULTFORCANDIDATEBYSNAPSHOTBLOCK,
                        Arrays.<Type>asList(address, snapBlockNumber),
                        Arrays.<TypeReference<?>>asList(new TypeReference<DynamicArray<Address>>() {
                        }, new TypeReference<DynamicArray<Uint256>>() {
                        }));
                String functionGetVoteInfoByStageFunEncode = FunctionEncoder.encode(functionGetVoteInfoByStageFun);
                HpbCall functionGetVoteInfoByStageFunEncodeHpbCall = admin.hpbCall(
                        Transaction.createHpbCallTransaction(nodeAddress, contractAddress, functionGetVoteInfoByStageFunEncode), DefaultBlockParameterName.LATEST)
                        .send();
                String functionGetVoteInfoByStageReturnValue = functionGetVoteInfoByStageFunEncodeHpbCall.getValue();
                List<Type> decodeResultList = FunctionReturnDecoder.decode(functionGetVoteInfoByStageReturnValue, functionGetVoteInfoByStageFun.getOutputParameters());
                if (decodeResultList.isEmpty()) {
                    System.out.println("decodeResultList.size ===" + decodeResultList.size());
                } else {
                    DynamicArray<Address> addressList = (DynamicArray<Address>) decodeList.get(0);
                    DynamicArray<Uint256> uint256List = (DynamicArray<Uint256>) decodeList.get(1);
                    System.out.println("addressList.value ==" + addressList.getValue());
                    System.out.println("uint256List.value ==" + uint256List.getValue());
                    if (!addressList.getValue().isEmpty()) {
                        for (int i = 0; i < addressList.getValue().size(); i++) {
                            Address address1 = addressList.getValue().get(i);
                            Uint256 uint256 = uint256List.getValue().get(i);
                            System.out.println("Address [ : " + address1.getValue() + "] , voteNumber [" + uint256.getValue() + "]");
                        }
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<TypeReference<Type>> convert(List<TypeReference<?>> input) {
        List<TypeReference<Type>> result = new ArrayList<>(input.size());
        result.addAll(input.stream()
                .map(typeReference -> (TypeReference<Type>) typeReference)
                .collect(Collectors.toList()));
        return result;
    }
}
