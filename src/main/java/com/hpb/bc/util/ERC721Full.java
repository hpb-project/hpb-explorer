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

package com.hpb.bc.util;

import io.hpb.web3.abi.EventEncoder;
import io.hpb.web3.abi.FunctionEncoder;
import io.hpb.web3.abi.TypeReference;
import io.hpb.web3.abi.datatypes.Address;
import io.hpb.web3.abi.datatypes.Bool;
import io.hpb.web3.abi.datatypes.DynamicBytes;
import io.hpb.web3.abi.datatypes.Event;
import io.hpb.web3.abi.datatypes.Function;
import io.hpb.web3.abi.datatypes.Type;
import io.hpb.web3.abi.datatypes.Utf8String;
import io.hpb.web3.abi.datatypes.generated.Bytes4;
import io.hpb.web3.abi.datatypes.generated.Uint256;
import io.hpb.web3.crypto.Credentials;
import io.hpb.web3.protocol.Web3;
import io.hpb.web3.protocol.core.DefaultBlockParameter;
import io.hpb.web3.protocol.core.RemoteCall;
import io.hpb.web3.protocol.core.methods.request.HpbFilter;
import io.hpb.web3.protocol.core.methods.response.Log;
import io.hpb.web3.protocol.core.methods.response.TransactionReceipt;
import io.hpb.web3.tx.Contract;
import io.hpb.web3.tx.TransactionManager;
import io.hpb.web3.tx.gas.ContractGasProvider;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import rx.Observable;
import rx.functions.Func1;

/**
 * <p>Auto generated code.
 * <p><strong>Do not modify!</strong>
 * <p>Generated with web3 version none.
 */
@SuppressWarnings("rawtypes")
public class ERC721Full extends Contract {
    private static final String BINARY = "6080604052604051620013a7380380620013a7833981018060405260408110156200002957600080fd5b8101908080516401000000008111156200004257600080fd5b820160208101848111156200005657600080fd5b81516401000000008111828201871017156200007157600080fd5b505092919060200180516401000000008111156200008e57600080fd5b82016020810184811115620000a257600080fd5b8151640100000000811182820187101715620000bd57600080fd5b50909350849250839150620000fd90507f01ffc9a700000000000000000000000000000000000000000000000000000000640100000000620001cf810204565b620001317f80ac58cd00000000000000000000000000000000000000000000000000000000640100000000620001cf810204565b620001657f780e9d6300000000000000000000000000000000000000000000000000000000640100000000620001cf810204565b81516200017a9060099060208501906200023c565b5080516200019090600a9060208401906200023c565b50620001c57f5b5e139f00000000000000000000000000000000000000000000000000000000640100000000620001cf810204565b50505050620002e1565b7fffffffff000000000000000000000000000000000000000000000000000000008082161415620001ff57600080fd5b7fffffffff00000000000000000000000000000000000000000000000000000000166000908152602081905260409020805460ff19166001179055565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f106200027f57805160ff1916838001178555620002af565b82800160010185558215620002af579182015b82811115620002af57825182559160200191906001019062000292565b50620002bd929150620002c1565b5090565b620002de91905b80821115620002bd5760008155600101620002c8565b90565b6110b680620002f16000396000f3fe6080604052600436106100ea577c0100000000000000000000000000000000000000000000000000000000600035046301ffc9a781146100ef57806306fdde031461014c578063081812fc146101d6578063095ea7b31461021c57806318160ddd1461025757806323b872dd1461027e5780632f745c59146102c157806342842e0e146102fa5780634f6ccce71461033d5780636352211e146103675780636c3360141461039157806370a08231146103d657806395d89b4114610409578063a22cb4651461041e578063b88d4fde14610459578063c87b56dd1461052c578063e985e9c514610556575b600080fd5b3480156100fb57600080fd5b506101386004803603602081101561011257600080fd5b50357bffffffffffffffffffffffffffffffffffffffffffffffffffffffff1916610591565b604080519115158252519081900360200190f35b34801561015857600080fd5b506101616105c5565b6040805160208082528351818301528351919283929083019185019080838360005b8381101561019b578181015183820152602001610183565b50505050905090810190601f1680156101c85780820380516001836020036101000a031916815260200191505b509250505060405180910390f35b3480156101e257600080fd5b50610200600480360360208110156101f957600080fd5b503561065c565b60408051600160a060020a039092168252519081900360200190f35b34801561022857600080fd5b506102556004803603604081101561023f57600080fd5b50600160a060020a03813516906020013561068e565b005b34801561026357600080fd5b5061026c610744565b60408051918252519081900360200190f35b34801561028a57600080fd5b50610255600480360360608110156102a157600080fd5b50600160a060020a0381358116916020810135909116906040013561074a565b3480156102cd57600080fd5b5061026c600480360360408110156102e457600080fd5b50600160a060020a0381351690602001356107d8565b34801561030657600080fd5b506102556004803603606081101561031d57600080fd5b50600160a060020a03813581169160208101359091169060400135610825565b34801561034957600080fd5b5061026c6004803603602081101561036057600080fd5b5035610846565b34801561037357600080fd5b506102006004803603602081101561038a57600080fd5b503561087b565b34801561039d57600080fd5b50610255600480360360608110156103b457600080fd5b50600160a060020a0381358116916020810135909116906040013515156108a5565b3480156103e257600080fd5b5061026c600480360360208110156103f957600080fd5b5035600160a060020a031661092c565b34801561041557600080fd5b5061016161095f565b34801561042a57600080fd5b506102556004803603604081101561044157600080fd5b50600160a060020a03813516906020013515156109c0565b34801561046557600080fd5b506102556004803603608081101561047c57600080fd5b600160a060020a038235811692602081013590911691604082013591908101906080810160608201356401000000008111156104b757600080fd5b8201836020820111156104c957600080fd5b803590602001918460018302840111640100000000831117156104eb57600080fd5b91908080601f016020809104026020016040519081016040528093929190818152602001838380828437600092019190915250929550610a44945050505050565b34801561053857600080fd5b506101616004803603602081101561054f57600080fd5b5035610a6c565b34801561056257600080fd5b506101386004803603604081101561057957600080fd5b50600160a060020a0381358116916020013516610b21565b7bffffffffffffffffffffffffffffffffffffffffffffffffffffffff191660009081526020819052604090205460ff1690565b60098054604080516020601f60026000196101006001881615020190951694909404938401819004810282018101909252828152606093909290918301828280156106515780601f1061062657610100808354040283529160200191610651565b820191906000526020600020905b81548152906001019060200180831161063457829003601f168201915b505050505090505b90565b600061066782610b4f565b151561067257600080fd5b50600090815260026020526040902054600160a060020a031690565b60006106998261087b565b9050600160a060020a0383811690821614156106b457600080fd5b33600160a060020a03821614806106d057506106d08133610b21565b15156106db57600080fd5b600082815260026020526040808220805473ffffffffffffffffffffffffffffffffffffffff1916600160a060020a0387811691821790925591518593918516917f8c5be1e5ebec7d5bd14f71427d1e84f3dd0314c0f7b2291e5b200ac8c7c3b92591a4505050565b60075490565b6107543382610b6c565b151561075f57600080fd5b600160a060020a038216151561077457600080fd5b61077e8382610bcb565b6107888382610c3c565b6107928282610d3e565b8082600160a060020a031684600160a060020a03167fddf252ad1be2c89b69c2b068fc378daa952ba7f163c4a11628f55a4df523b3ef60405160405180910390a4505050565b60006107e38361092c565b82106107ee57600080fd5b600160a060020a038316600090815260056020526040902080548390811061081257fe5b9060005260206000200154905092915050565b6108418383836020604051908101604052806000815250610a44565b505050565b6000610850610744565b821061085b57600080fd5b600780548390811061086957fe5b90600052602060002001549050919050565b600081815260016020526040812054600160a060020a031680151561089f57600080fd5b92915050565b600160a060020a0382811690841614156108be57600080fd5b600160a060020a03838116600081815260046020908152604080832094871680845294825291829020805460ff1916861515908117909155825190815291517f17307eab39ab6107e8899845ad3d59bd9653f200f220920489ca2b5937696c319281900390910190a3505050565b6000600160a060020a038216151561094357600080fd5b50600160a060020a031660009081526003602052604090205490565b600a8054604080516020601f60026000196101006001881615020190951694909404938401819004810282018101909252828152606093909290918301828280156106515780601f1061062657610100808354040283529160200191610651565b600160a060020a0382163314156109d657600080fd5b336000818152600460209081526040808320600160a060020a03871680855290835292819020805460ff1916861515908117909155815190815290519293927f17307eab39ab6107e8899845ad3d59bd9653f200f220920489ca2b5937696c31929181900390910190a35050565b610a4f84848461074a565b610a5b84848484610d84565b1515610a6657600080fd5b50505050565b6060610a7782610b4f565b1515610a8257600080fd5b6000828152600b602090815260409182902080548351601f600260001961010060018616150201909316929092049182018490048402810184019094528084529091830182828015610b155780601f10610aea57610100808354040283529160200191610b15565b820191906000526020600020905b815481529060010190602001808311610af857829003601f168201915b50505050509050919050565b600160a060020a03918216600090815260046020908152604080832093909416825291909152205460ff1690565b600090815260016020526040902054600160a060020a0316151590565b600080610b788361087b565b905080600160a060020a031684600160a060020a03161480610bb3575083600160a060020a0316610ba88461065c565b600160a060020a0316145b80610bc35750610bc38185610b21565b949350505050565b81600160a060020a0316610bde8261087b565b600160a060020a031614610bf157600080fd5b600081815260026020526040902054600160a060020a031615610c38576000818152600260205260409020805473ffffffffffffffffffffffffffffffffffffffff191690555b5050565b610c468282610f00565b600081815260066020908152604080832054600160a060020a03861684526005909252822054909190610c8090600163ffffffff610f9616565b600160a060020a03851660009081526005602052604081208054929350909183908110610ca957fe5b90600052602060002001549050806005600087600160a060020a0316600160a060020a0316815260200190815260200160002084815481101515610ce957fe5b6000918252602080832090910192909255600160a060020a0387168152600590915260409020805490610d2090600019830161104d565b50600093845260066020526040808520859055908452909220555050565b610d488282610fa8565b600160a060020a039091166000908152600560209081526040808320805460018101825590845282842081018590559383526006909152902055565b6000610d9884600160a060020a0316611038565b1515610da657506001610bc3565b6040517f150b7a020000000000000000000000000000000000000000000000000000000081523360048201818152600160a060020a03888116602485015260448401879052608060648501908152865160848601528651600095928a169463150b7a029490938c938b938b939260a4019060208501908083838e5b83811015610e39578181015183820152602001610e21565b50505050905090810190601f168015610e665780820380516001836020036101000a031916815260200191505b5095505050505050602060405180830381600087803b158015610e8857600080fd5b505af1158015610e9c573d6000803e3d6000fd5b505050506040513d6020811015610eb257600080fd5b50517bffffffffffffffffffffffffffffffffffffffffffffffffffffffff19167f150b7a020000000000000000000000000000000000000000000000000000000014915050949350505050565b81600160a060020a0316610f138261087b565b600160a060020a031614610f2657600080fd5b600160a060020a038216600090815260036020526040902054610f5090600163ffffffff610f9616565b600160a060020a03909216600090815260036020908152604080832094909455918152600190915220805473ffffffffffffffffffffffffffffffffffffffff19169055565b600082821115610fa257fe5b50900390565b600081815260016020526040902054600160a060020a031615610fca57600080fd5b6000818152600160208181526040808420805473ffffffffffffffffffffffffffffffffffffffff1916600160a060020a038816908117909155845260039091529091205461101891611040565b600160a060020a0390921660009081526003602052604090209190915550565b6000903b1190565b8181018281101561089f57fe5b8154818355818111156108415760008381526020902061084191810190830161065991905b808211156110865760008155600101611072565b509056fea165627a7a7230582001472e77724025e1a77a2f4ff8102d0e52656d71ecff1d1db2ece533b4d0cec00029";

    public static final String FUNC_SUPPORTSINTERFACE = "supportsInterface";

    public static final String FUNC_NAME = "name";

    public static final String FUNC_GETAPPROVED = "getApproved";

    public static final String FUNC_APPROVE = "approve";

    public static final String FUNC_TOTALSUPPLY = "totalSupply";

    public static final String FUNC_TRANSFERFROM = "transferFrom";

    public static final String FUNC_TOKENOFOWNERBYINDEX = "tokenOfOwnerByIndex";

    public static final String FUNC_SAFETRANSFERFROM = "safeTransferFrom";

    public static final String FUNC_TOKENBYINDEX = "tokenByIndex";

    public static final String FUNC_OWNEROF = "ownerOf";

    public static final String FUNC_SETAPPROVALFORALLWITHTOKENOWNER = "setApprovalForAllWithTokenOwner";

    public static final String FUNC_BALANCEOF = "balanceOf";

    public static final String FUNC_SYMBOL = "symbol";

    public static final String FUNC_SETAPPROVALFORALL = "setApprovalForAll";

    public static final String FUNC_TOKENURI = "tokenURI";

    public static final String FUNC_ISAPPROVEDFORALL = "isApprovedForAll";

    public static final Event TRANSFER_EVENT = new Event("Transfer",
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>(true) {
            }, new TypeReference<Address>(true) {
            }, new TypeReference<Uint256>(true) {
            }));
    ;

    public static final Event APPROVAL_EVENT = new Event("Approval",
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>(true) {
            }, new TypeReference<Address>(true) {
            }, new TypeReference<Uint256>(true) {
            }));
    ;

    public static final Event APPROVALFORALL_EVENT = new Event("ApprovalForAll",
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>(true) {
            }, new TypeReference<Address>(true) {
            }, new TypeReference<Bool>() {
            }));
    ;

    protected ERC721Full(String contractAddress, Web3 web3, Credentials credentials,
                         BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3, getTransactionManager(web3, credentials), getContractGasProvider(gasPrice, gasLimit));
    }

    protected ERC721Full(String contractAddress, Web3 web3, TransactionManager transactionManager,
                         BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3, transactionManager, getContractGasProvider(gasPrice, gasLimit));
    }

    protected ERC721Full(String contractAddress, Web3 web3, TransactionManager transactionManager,
                         ContractGasProvider gasProvider) {
        super(BINARY, contractAddress, web3, transactionManager, gasProvider);
    }

    public RemoteCall<Bool> supportsInterface(Bytes4 interfaceId) {
        final Function function = new Function(FUNC_SUPPORTSINTERFACE,
                Arrays.<Type>asList(interfaceId),
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {
                }));
        return executeRemoteCallSingleValueReturn(function);
    }

    public RemoteCall<Utf8String> name() {
        final Function function = new Function(FUNC_NAME,
                Arrays.<Type>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {
                }));
        return executeRemoteCallSingleValueReturn(function);
    }

    public RemoteCall<Address> getApproved(Uint256 tokenId) {
        final Function function = new Function(FUNC_GETAPPROVED,
                Arrays.<Type>asList(tokenId),
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {
                }));
        return executeRemoteCallSingleValueReturn(function);
    }

    public RemoteCall<TransactionReceipt> approve(Address to, Uint256 tokenId) {
        final Function function = new Function(
                FUNC_APPROVE,
                Arrays.<Type>asList(to, tokenId),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<Uint256> totalSupply() {
        final Function function = new Function(FUNC_TOTALSUPPLY,
                Arrays.<Type>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {
                }));
        return executeRemoteCallSingleValueReturn(function);
    }

    public RemoteCall<TransactionReceipt> transferFrom(Address from, Address to, Uint256 tokenId) {
        final Function function = new Function(
                FUNC_TRANSFERFROM,
                Arrays.<Type>asList(from, to, tokenId),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<Uint256> tokenOfOwnerByIndex(Address owner, Uint256 index) {
        final Function function = new Function(FUNC_TOKENOFOWNERBYINDEX,
                Arrays.<Type>asList(owner, index),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {
                }));
        return executeRemoteCallSingleValueReturn(function);
    }

    public RemoteCall<TransactionReceipt> safeTransferFrom(Address from, Address to,
                                                           Uint256 tokenId) {
        final Function function = new Function(
                FUNC_SAFETRANSFERFROM,
                Arrays.<Type>asList(from, to, tokenId),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<Uint256> tokenByIndex(Uint256 index) {
        final Function function = new Function(FUNC_TOKENBYINDEX,
                Arrays.<Type>asList(index),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {
                }));
        return executeRemoteCallSingleValueReturn(function);
    }

    public RemoteCall<Address> ownerOf(Uint256 tokenId) {
        final Function function = new Function(FUNC_OWNEROF,
                Arrays.<Type>asList(tokenId),
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {
                }));
        return executeRemoteCallSingleValueReturn(function);
    }

    public RemoteCall<TransactionReceipt> setApprovalForAllWithTokenOwner(Address tokenOwner,
                                                                          Address to, Bool approved) {
        final Function function = new Function(
                FUNC_SETAPPROVALFORALLWITHTOKENOWNER,
                Arrays.<Type>asList(tokenOwner, to, approved),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<Uint256> balanceOf(Address owner) {
        final Function function = new Function(FUNC_BALANCEOF,
                Arrays.<Type>asList(owner),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {
                }));
        return executeRemoteCallSingleValueReturn(function);
    }

    public RemoteCall<Utf8String> symbol() {
        final Function function = new Function(FUNC_SYMBOL,
                Arrays.<Type>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {
                }));
        return executeRemoteCallSingleValueReturn(function);
    }

    public RemoteCall<TransactionReceipt> setApprovalForAll(Address to, Bool approved) {
        final Function function = new Function(
                FUNC_SETAPPROVALFORALL,
                Arrays.<Type>asList(to, approved),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<TransactionReceipt> safeTransferFrom(Address from, Address to,
                                                           Uint256 tokenId, DynamicBytes _data) {
        final Function function = new Function(
                FUNC_SAFETRANSFERFROM,
                Arrays.<Type>asList(from, to, tokenId, _data),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<Utf8String> tokenURI(Uint256 tokenId) {
        final Function function = new Function(FUNC_TOKENURI,
                Arrays.<Type>asList(tokenId),
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {
                }));
        return executeRemoteCallSingleValueReturn(function);
    }

    public RemoteCall<Bool> isApprovedForAll(Address owner, Address operator) {
        final Function function = new Function(FUNC_ISAPPROVEDFORALL,
                Arrays.<Type>asList(owner, operator),
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {
                }));
        return executeRemoteCallSingleValueReturn(function);
    }

    public static RemoteCall<ERC721Full> deploy(Web3 web3, Credentials credentials,
                                                BigInteger gasPrice, BigInteger gasLimit, BigInteger initialWeiValue, Utf8String name,
                                                Utf8String symbol) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(name, symbol));
        return deployRemoteCall(ERC721Full.class, web3, credentials, gasPrice, gasLimit, BINARY, encodedConstructor, initialWeiValue);
    }

    public static RemoteCall<ERC721Full> deploy(Web3 web3, TransactionManager transactionManager,
                                                BigInteger gasPrice, BigInteger gasLimit, BigInteger initialWeiValue, Utf8String name,
                                                Utf8String symbol) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(name, symbol));
        return deployRemoteCall(ERC721Full.class, web3, transactionManager, gasPrice, gasLimit, BINARY, encodedConstructor, initialWeiValue);
    }

    public List<TransferEventResponse> getTransferEvents(TransactionReceipt transactionReceipt) {
        List<EventValuesWithLog> valueList = extractEventParametersWithLog(TRANSFER_EVENT, transactionReceipt);
        ArrayList<TransferEventResponse> responses = new ArrayList<TransferEventResponse>(valueList.size());
        for (EventValuesWithLog eventValues : valueList) {
            TransferEventResponse typedResponse = new TransferEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.from = (Address) eventValues.getIndexedValues().get(0);
            typedResponse.to = (Address) eventValues.getIndexedValues().get(1);
            typedResponse.tokenId = (Uint256) eventValues.getIndexedValues().get(2);
            responses.add(typedResponse);
        }
        return responses;
    }

    public Observable<TransferEventResponse> transferEventObservable(HpbFilter filter) {
        return web3.hpbLogObservable(filter).map(new Func1<Log, TransferEventResponse>() {
            @Override
            public TransferEventResponse call(Log log) {
                EventValuesWithLog eventValues = extractEventParametersWithLog(TRANSFER_EVENT, log);
                TransferEventResponse typedResponse = new TransferEventResponse();
                typedResponse.log = log;
                typedResponse.from = (Address) eventValues.getIndexedValues().get(0);
                typedResponse.to = (Address) eventValues.getIndexedValues().get(1);
                typedResponse.tokenId = (Uint256) eventValues.getIndexedValues().get(2);
                return typedResponse;
            }
        });
    }

    public Observable<TransferEventResponse> transferEventObservable(DefaultBlockParameter startBlock,
                                                                     DefaultBlockParameter endBlock) {
        HpbFilter filter = new HpbFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(TRANSFER_EVENT));
        return transferEventObservable(filter);
    }

    public List<ApprovalEventResponse> getApprovalEvents(TransactionReceipt transactionReceipt) {
        List<EventValuesWithLog> valueList = extractEventParametersWithLog(APPROVAL_EVENT, transactionReceipt);
        ArrayList<ApprovalEventResponse> responses = new ArrayList<ApprovalEventResponse>(valueList.size());
        for (EventValuesWithLog eventValues : valueList) {
            ApprovalEventResponse typedResponse = new ApprovalEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.owner = (Address) eventValues.getIndexedValues().get(0);
            typedResponse.approved = (Address) eventValues.getIndexedValues().get(1);
            typedResponse.tokenId = (Uint256) eventValues.getIndexedValues().get(2);
            responses.add(typedResponse);
        }
        return responses;
    }

    public Observable<ApprovalEventResponse> approvalEventObservable(HpbFilter filter) {
        return web3.hpbLogObservable(filter).map(new Func1<Log, ApprovalEventResponse>() {
            @Override
            public ApprovalEventResponse call(Log log) {
                EventValuesWithLog eventValues = extractEventParametersWithLog(APPROVAL_EVENT, log);
                ApprovalEventResponse typedResponse = new ApprovalEventResponse();
                typedResponse.log = log;
                typedResponse.owner = (Address) eventValues.getIndexedValues().get(0);
                typedResponse.approved = (Address) eventValues.getIndexedValues().get(1);
                typedResponse.tokenId = (Uint256) eventValues.getIndexedValues().get(2);
                return typedResponse;
            }
        });
    }

    public Observable<ApprovalEventResponse> approvalEventObservable(DefaultBlockParameter startBlock,
                                                                     DefaultBlockParameter endBlock) {
        HpbFilter filter = new HpbFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(APPROVAL_EVENT));
        return approvalEventObservable(filter);
    }

    public List<ApprovalForAllEventResponse> getApprovalForAllEvents(TransactionReceipt transactionReceipt) {
        List<EventValuesWithLog> valueList = extractEventParametersWithLog(APPROVALFORALL_EVENT, transactionReceipt);
        ArrayList<ApprovalForAllEventResponse> responses = new ArrayList<ApprovalForAllEventResponse>(valueList.size());
        for (EventValuesWithLog eventValues : valueList) {
            ApprovalForAllEventResponse typedResponse = new ApprovalForAllEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.owner = (Address) eventValues.getIndexedValues().get(0);
            typedResponse.operator = (Address) eventValues.getIndexedValues().get(1);
            typedResponse.approved = (Bool) eventValues.getNonIndexedValues().get(0);
            responses.add(typedResponse);
        }
        return responses;
    }

    public Observable<ApprovalForAllEventResponse> approvalForAllEventObservable(HpbFilter filter) {
        return web3.hpbLogObservable(filter).map(new Func1<Log, ApprovalForAllEventResponse>() {
            @Override
            public ApprovalForAllEventResponse call(Log log) {
                EventValuesWithLog eventValues = extractEventParametersWithLog(APPROVALFORALL_EVENT, log);
                ApprovalForAllEventResponse typedResponse = new ApprovalForAllEventResponse();
                typedResponse.log = log;
                typedResponse.owner = (Address) eventValues.getIndexedValues().get(0);
                typedResponse.operator = (Address) eventValues.getIndexedValues().get(1);
                typedResponse.approved = (Bool) eventValues.getNonIndexedValues().get(0);
                return typedResponse;
            }
        });
    }

    public Observable<ApprovalForAllEventResponse> approvalForAllEventObservable(DefaultBlockParameter startBlock,
                                                                                 DefaultBlockParameter endBlock) {
        HpbFilter filter = new HpbFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(APPROVALFORALL_EVENT));
        return approvalForAllEventObservable(filter);
    }

    public static ERC721Full load(String contractAddress, Web3 web3, Credentials credentials,
                                  BigInteger gasPrice, BigInteger gasLimit) {
        return new ERC721Full(contractAddress, web3, credentials, gasPrice, gasLimit);
    }

    public static ERC721Full load(String contractAddress, Web3 web3,
                                  TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new ERC721Full(contractAddress, web3, transactionManager, gasPrice, gasLimit);
    }

    public static class TransferEventResponse {
        public Log log;

        public Address from;

        public Address to;

        public Uint256 tokenId;
    }

    public static class ApprovalEventResponse {
        public Log log;

        public Address owner;

        public Address approved;

        public Uint256 tokenId;
    }

    public static class ApprovalForAllEventResponse {
        public Log log;

        public Address owner;

        public Address operator;

        public Bool approved;
    }
}
