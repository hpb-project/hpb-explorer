package com.hpb.bc.contracts;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import io.hpb.web3.abi.EventEncoder;
import io.hpb.web3.abi.TypeReference;
import io.hpb.web3.abi.datatypes.Address;
import io.hpb.web3.abi.datatypes.Event;
import io.hpb.web3.abi.datatypes.Function;
import io.hpb.web3.abi.datatypes.Type;
import io.hpb.web3.abi.datatypes.Utf8String;
import io.hpb.web3.crypto.Credentials;
import io.hpb.web3.protocol.Web3;
import io.hpb.web3.protocol.core.DefaultBlockParameter;
import io.hpb.web3.protocol.core.RemoteCall;
import io.hpb.web3.protocol.core.methods.request.HpbFilter;
import io.hpb.web3.protocol.core.methods.response.Log;
import io.hpb.web3.protocol.core.methods.response.TransactionReceipt;
import io.hpb.web3.tx.Contract;
import io.hpb.web3.tx.RawTransactionManager;
import io.hpb.web3.tx.TransactionManager;
import io.hpb.web3.tx.gas.ContractGasProvider;
import io.hpb.web3.tx.gas.StaticGasProvider;

import rx.Observable;
import rx.functions.Func1;

@SuppressWarnings("rawtypes")
public class FechHpbBallotAddr extends Contract {
    private static final String BINARY = "60008054600160a060020a031916905560c0604052600a60808190527f307863393031383965340000000000000000000000000000000000000000000060a090815261004e9160019190610098565b5034801561005b57600080fd5b506002805433600160a060020a03199182161791829055600160a060020a0390911660008181526003602052604090208054909216179055610133565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f106100d957805160ff1916838001178555610106565b82800160010185558215610106579182015b828111156101065782518255916020019190600101906100eb565b50610112929150610116565b5090565b61013091905b80821115610112576000815560010161011c565b90565b61071c806101426000396000f3006080604052600436106100ae5763ffffffff7c010000000000000000000000000000000000000000000000000000000060003504166315a5d9d881146100b357806327e1f7df146100e4578063286d2e3a146101075780634216503c1461012857806370480275146101815780637421a8dc146101a25780638da5cb5b1461022c5780639e45593914610241578063dbbc830b14610256578063ebb332be14610277578063f2fde38b1461028c575b600080fd5b3480156100bf57600080fd5b506100c86102ad565b60408051600160a060020a039092168252519081900360200190f35b3480156100f057600080fd5b50610105600160a060020a03600435166102bc565b005b34801561011357600080fd5b50610105600160a060020a036004351661032d565b34801561013457600080fd5b506040805160206004803580820135601f81018490048402850184019095528484526101059436949293602493928401919081908401838280828437509497506103a99650505050505050565b34801561018d57600080fd5b50610105600160a060020a0360043516610468565b3480156101ae57600080fd5b506101b76104b6565b6040805160208082528351818301528351919283929083019185019080838360005b838110156101f15781810151838201526020016101d9565b50505050905090810190601f16801561021e5780820380516001836020036101000a031916815260200191505b509250505060405180910390f35b34801561023857600080fd5b506100c8610543565b34801561024d57600080fd5b506100c8610552565b34801561026257600080fd5b506100c8600160a060020a0360043516610562565b34801561028357600080fd5b506101b761057d565b34801561029857600080fd5b50610105600160a060020a0360043516610612565b600054600160a060020a031681565b600254600160a060020a031633146102d357600080fd5b600160a060020a038082166000908152600360205260409020541615156102f957600080fd5b600160a060020a03166000908152600360205260409020805473ffffffffffffffffffffffffffffffffffffffff19169055565b33600090815260036020526040902054600160a060020a0316151561035157600080fd5b6000805473ffffffffffffffffffffffffffffffffffffffff1916600160a060020a0383169081178255604051909133917f36076b36b44fabca66166285ed49b74b11067e0e42399f5a648d3ae6f9fa480a9190a350565b33600090815260036020526040902054600160a060020a031615156103cd57600080fd5b80516103e0906001906020840190610658565b50806040518082805190602001908083835b602083106104115780518252601f1990920191602091820191016103f2565b5181516020939093036101000a60001901801990911692169190911790526040519201829003822093503392507fa8eebb346c0a02b999643fb7a97c4a4412ab0b5a9abb310b85c5e820b47bdd639160009150a350565b600254600160a060020a0316331461047f57600080fd5b600160a060020a03166000818152600360205260409020805473ffffffffffffffffffffffffffffffffffffffff19169091179055565b60018054604080516020600284861615610100026000190190941693909304601f8101849004840282018401909252818152929183018282801561053b5780601f106105105761010080835404028352916020019161053b565b820191906000526020600020905b81548152906001019060200180831161051e57829003601f168201915b505050505081565b600254600160a060020a031681565b600054600160a060020a03165b90565b600360205260009081526040902054600160a060020a031681565b60018054604080516020601f600260001961010087891615020190951694909404938401819004810282018101909252828152606093909290918301828280156106085780601f106105dd57610100808354040283529160200191610608565b820191906000526020600020905b8154815290600101906020018083116105eb57829003601f168201915b5050505050905090565b600254600160a060020a0316331461062957600080fd5b6002805473ffffffffffffffffffffffffffffffffffffffff1916600160a060020a0392909216919091179055565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f1061069957805160ff19168380011785556106c6565b828001600101855582156106c6579182015b828111156106c65782518255916020019190600101906106ab565b506106d29291506106d6565b5090565b61055f91905b808211156106d257600081556001016106dc5600a165627a7a723058203497539adf159a05ab4d233b83ba6285fe707318c4f31ab7424a269fce1c4a8a0029";

    public static final String FUNC_ADDADMIN = "addAdmin";

    public static final String FUNC_SETCONTRACTADDR = "setContractAddr";

    public static final String FUNC_DELETEADMIN = "deleteAdmin";

    public static final String FUNC_SETFUNSTR = "setFunStr";

    public static final String FUNC_TRANSFEROWNERSHIP = "transferOwnership";

    public static final String FUNC_ADMINMAP = "adminMap";

    public static final String FUNC_CONTRACTADDR = "contractAddr";

    public static final String FUNC_FUNSTR = "funStr";

    public static final String FUNC_GETCONTRACTADDR = "getContractAddr";

    public static final String FUNC_GETFUNSTR = "getFunStr";

    public static final String FUNC_OWNER = "owner";

    public static final Event SETFUNSTR_EVENT = new Event("SetFunStr",
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>(true) {
            }, new TypeReference<Utf8String>(true) {
            }));
    ;

    public static final Event SETCONTRACTADDR_EVENT = new Event("SetContractAddr",
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>(true) {
            }, new TypeReference<Address>(true) {
            }));
    ;

    protected FechHpbBallotAddr(String contractAddress, Web3 web3, Credentials credentials,
                                BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3, new RawTransactionManager(web3, credentials), new StaticGasProvider(gasPrice, gasLimit));
    }

    protected FechHpbBallotAddr(String contractAddress, Web3 web3,
                                TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3, transactionManager, new StaticGasProvider(gasPrice, gasLimit));
    }

    protected FechHpbBallotAddr(String contractAddress, Web3 web3, TransactionManager transactionManager,
                                ContractGasProvider gasProvider) {
        super(BINARY, contractAddress, web3, transactionManager, gasProvider);
    }

    public List<SetFunStrEventResponse> getSetFunStrEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(SETFUNSTR_EVENT, transactionReceipt);
        ArrayList<SetFunStrEventResponse> responses = new ArrayList<SetFunStrEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            SetFunStrEventResponse typedResponse = new SetFunStrEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.from = (Address) eventValues.getIndexedValues().get(0);
            typedResponse._funStr = (Utf8String) eventValues.getIndexedValues().get(1);
            responses.add(typedResponse);
        }
        return responses;
    }

    public Observable<SetFunStrEventResponse> setFunStrEventObservable(HpbFilter filter) {
        return web3.hpbLogObservable(filter).map(new Func1<Log, SetFunStrEventResponse>() {
            @Override
            public SetFunStrEventResponse call(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(SETFUNSTR_EVENT, log);
                SetFunStrEventResponse typedResponse = new SetFunStrEventResponse();
                typedResponse.log = log;
                typedResponse.from = (Address) eventValues.getIndexedValues().get(0);
                typedResponse._funStr = (Utf8String) eventValues.getIndexedValues().get(1);
                return typedResponse;
            }
        });
    }

    public Observable<SetFunStrEventResponse> setFunStrEventObservable(DefaultBlockParameter startBlock,
                                                                       DefaultBlockParameter endBlock) {
        HpbFilter filter = new HpbFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(SETFUNSTR_EVENT));
        return setFunStrEventObservable(filter);
    }

    public RemoteCall<TransactionReceipt> addAdmin(Address addr) {
        final Function function = new Function(
                FUNC_ADDADMIN,
                Arrays.<Type>asList(addr),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<TransactionReceipt> setContractAddr(Address _contractAddr) {
        final Function function = new Function(
                FUNC_SETCONTRACTADDR,
                Arrays.<Type>asList(_contractAddr),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public List<SetContractAddrEventResponse> getSetContractAddrEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(SETCONTRACTADDR_EVENT, transactionReceipt);
        ArrayList<SetContractAddrEventResponse> responses = new ArrayList<SetContractAddrEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            SetContractAddrEventResponse typedResponse = new SetContractAddrEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.from = (Address) eventValues.getIndexedValues().get(0);
            typedResponse._contractAddr = (Address) eventValues.getIndexedValues().get(1);
            responses.add(typedResponse);
        }
        return responses;
    }

    public Observable<SetContractAddrEventResponse> setContractAddrEventObservable(HpbFilter filter) {
        return web3.hpbLogObservable(filter).map(new Func1<Log, SetContractAddrEventResponse>() {
            @Override
            public SetContractAddrEventResponse call(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(SETCONTRACTADDR_EVENT, log);
                SetContractAddrEventResponse typedResponse = new SetContractAddrEventResponse();
                typedResponse.log = log;
                typedResponse.from = (Address) eventValues.getIndexedValues().get(0);
                typedResponse._contractAddr = (Address) eventValues.getIndexedValues().get(1);
                return typedResponse;
            }
        });
    }

    public Observable<SetContractAddrEventResponse> setContractAddrEventObservable(DefaultBlockParameter startBlock,
                                                                                   DefaultBlockParameter endBlock) {
        HpbFilter filter = new HpbFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(SETCONTRACTADDR_EVENT));
        return setContractAddrEventObservable(filter);
    }

    public RemoteCall<TransactionReceipt> deleteAdmin(Address addr) {
        final Function function = new Function(
                FUNC_DELETEADMIN,
                Arrays.<Type>asList(addr),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<TransactionReceipt> setFunStr(Utf8String _funStr) {
        final Function function = new Function(
                FUNC_SETFUNSTR,
                Arrays.<Type>asList(_funStr),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<TransactionReceipt> transferOwnership(Address newOwner) {
        final Function function = new Function(
                FUNC_TRANSFEROWNERSHIP,
                Arrays.<Type>asList(newOwner),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public static RemoteCall<FechHpbBallotAddr> deploy(Web3 web3, Credentials credentials,
                                                       BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(FechHpbBallotAddr.class, web3, credentials, gasPrice, gasLimit, BINARY, "");
    }

    public static RemoteCall<FechHpbBallotAddr> deploy(Web3 web3,
                                                       TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(FechHpbBallotAddr.class, web3, transactionManager, gasPrice, gasLimit, BINARY, "");
    }

    public RemoteCall<Address> adminMap(Address param0) {
        final Function function = new Function(FUNC_ADMINMAP,
                Arrays.<Type>asList(param0),
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {
                }));
        return executeRemoteCallSingleValueReturn(function);
    }

    public RemoteCall<Address> contractAddr() {
        final Function function = new Function(FUNC_CONTRACTADDR,
                Arrays.<Type>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {
                }));
        return executeRemoteCallSingleValueReturn(function);
    }

    public RemoteCall<Utf8String> funStr() {
        final Function function = new Function(FUNC_FUNSTR,
                Arrays.<Type>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {
                }));
        return executeRemoteCallSingleValueReturn(function);
    }

    public RemoteCall<Address> getContractAddr() {
        final Function function = new Function(FUNC_GETCONTRACTADDR,
                Arrays.<Type>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {
                }));
        return executeRemoteCallSingleValueReturn(function);
    }

    public RemoteCall<Utf8String> getFunStr() {
        final Function function = new Function(FUNC_GETFUNSTR,
                Arrays.<Type>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {
                }));
        return executeRemoteCallSingleValueReturn(function);
    }

    public RemoteCall<Address> owner() {
        final Function function = new Function(FUNC_OWNER,
                Arrays.<Type>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {
                }));
        return executeRemoteCallSingleValueReturn(function);
    }

    public static FechHpbBallotAddr load(String contractAddress, Web3 web3, Credentials credentials,
                                         BigInteger gasPrice, BigInteger gasLimit) {
        return new FechHpbBallotAddr(contractAddress, web3, credentials, gasPrice, gasLimit);
    }

    public static FechHpbBallotAddr load(String contractAddress, Web3 web3,
                                         TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new FechHpbBallotAddr(contractAddress, web3, transactionManager, gasPrice, gasLimit);
    }

    public static class SetFunStrEventResponse {
        public Log log;

        public Address from;

        public Utf8String _funStr;
    }

    public static class SetContractAddrEventResponse {
        public Log log;

        public Address from;

        public Address _contractAddr;
    }
}
