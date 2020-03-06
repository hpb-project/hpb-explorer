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

package com.hpb.bc.propeller;

import com.hpb.bc.model.EventData;
import com.hpb.bc.propeller.event.EventTransfer_address_address_uint256;
import com.hpb.bc.rpc.HpbFacade;
import com.hpb.bc.rpc.RpcHpbFacadeProvider;
import com.hpb.bc.solidity.CompilationResult;
import com.hpb.bc.solidity.SolidityContractDetails;
import com.hpb.bc.solidity.SolidityEvent;
import com.hpb.bc.solidity.TypedSolidityEvent;
import com.hpb.bc.solidity.abi.AbiEntry;
import com.hpb.bc.solidity.values.HpbAbi;
import com.hpb.bc.solidity.values.HpbAddress;
import com.hpb.bc.solidity.values.HpbValue;
import com.hpb.bc.util.GsonUtil;
import io.hpb.web3.abi.EventEncoder;
import io.hpb.web3.abi.TypeReference;
import io.hpb.web3.abi.datatypes.Address;
import io.hpb.web3.abi.datatypes.Event;
import io.hpb.web3.abi.datatypes.generated.Uint256;
import io.hpb.web3.protocol.Web3Service;
import io.hpb.web3.protocol.core.DefaultBlockParameter;
import io.hpb.web3.protocol.http.HttpService;
import io.hpb.web3.protocol.ipc.UnixIpcService;
import io.hpb.web3.protocol.ipc.WindowsIpcService;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.net.URISyntaxException;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static com.hpb.bc.constant.BcConstant.WEB3J_TIMEOUT;

@Component
public class HpbFacadeHelper {

    private final static Logger logger = LoggerFactory.getLogger(HpbFacadeHelper.class);

    private static Web3Service buildService(String clientAddress) {
        Web3Service Web3Service;
        if (clientAddress == null || clientAddress.equals("")) {
            Web3Service = new HttpService(createOkHttpClient());
        } else if (clientAddress.startsWith("http")) {
            Web3Service = new HttpService(clientAddress, createOkHttpClient(), false);
        } else if (System.getProperty("os.name").toLowerCase().startsWith("win")) {
            Web3Service = new WindowsIpcService(clientAddress);
        } else {
            Web3Service = new UnixIpcService(clientAddress);
        }

        return Web3Service;
    }

    private static OkHttpClient createOkHttpClient() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        configureLogging(builder);
        configureTimeouts(builder);
        return builder.build();
    }

    private static void configureLogging(OkHttpClient.Builder builder) {
        if (logger.isDebugEnabled()) {
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor(logger::debug);
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
            builder.addInterceptor(logging);
        }
    }

    private static void configureTimeouts(OkHttpClient.Builder builder) {
        builder.connectTimeout(WEB3J_TIMEOUT, TimeUnit.SECONDS);
        builder.readTimeout(WEB3J_TIMEOUT, TimeUnit.SECONDS); // Sets the socket timeout too
        builder.writeTimeout(WEB3J_TIMEOUT, TimeUnit.SECONDS);
    }

    public void testPropeller() {
        String hpbNodeIp = "http://47.75.252.65:8545";
        Web3Service web3Service = buildService(hpbNodeIp);
        HpbFacade hpbFacade = RpcHpbFacadeProvider.forRemoteNode(hpbNodeIp, RpcHpbFacadeProvider.MAIN_CHAIN_ID);
        HpbAddress hpbAddress = HpbAddress.of("0x9f0e4a9860b5ee81dfbbe09baa0d64e8d009d854");
        HpbValue hpbValue = hpbFacade.getBalance(hpbAddress);
        logger.info("hpbValue.inWei ==" + hpbValue.inWei());
        logger.info("hpbValue.inHpb ==" + hpbValue.inHpb());
        // (Optional<DefaultBlockParameter> fromBlock, Optional<DefaultBlockParameter> toBlock, SolidityEvent eventDefiniton, HpbAddress address, String... optionalTopics)

        DefaultBlockParameter startBlock = DefaultBlockParameter.valueOf(BigInteger.valueOf(3000000));

        DefaultBlockParameter toBlock = io.hpb.web3.protocol.core.DefaultBlockParameterName.LATEST;

        HpbAddress contractAddress = HpbAddress.of("0x2072f300c98539760be185b05b738f9e94d2e48a");
        // 0x9108a228990eb762b7022da28e99067da2458327
        contractAddress = HpbAddress.of("0xa112125702b5b54362a37c7aa633692ad480f20e");
        String abi = "";
        HpbAbi hpbAbi = HpbAbi.of(abi);
        String FUNC_TRANSFER = "transfer";
        Event TRANSFER_EVENT = new Event("Transfer", Arrays.<TypeReference<?>>asList(new TypeReference<Address>(true) {
                                                                                     },
                new TypeReference<Address>(true) {
                }, new TypeReference<Uint256>(false) {
                }));
        ;


        //compireContractDetail(hpbFacade);


    }

    private String getResult() {

        String str = "{\"contracts\":{\"BasicToken\":{\"abi\":\"[{\\\"constant\\\":true,\\\"inputs\\\":[],\\\"name\\\":\\\"totalSupply\\\",\\\"outputs\\\":[{\\\"name\\\":\\\"\\\",\\\"type\\\":\\\"uint256\\\"}],\\\"payable\\\":false,\\\"stateMutability\\\":\\\"view\\\",\\\"type\\\":\\\"function\\\"},{\\\"constant\\\":true,\\\"inputs\\\":[{\\\"name\\\":\\\"_owner\\\",\\\"type\\\":\\\"address\\\"}],\\\"name\\\":\\\"balanceOf\\\",\\\"outputs\\\":[{\\\"name\\\":\\\"balance\\\",\\\"type\\\":\\\"uint256\\\"}],\\\"payable\\\":false,\\\"stateMutability\\\":\\\"view\\\",\\\"type\\\":\\\"function\\\"},{\\\"constant\\\":false,\\\"inputs\\\":[{\\\"name\\\":\\\"_to\\\",\\\"type\\\":\\\"address\\\"},{\\\"name\\\":\\\"_value\\\",\\\"type\\\":\\\"uint256\\\"}],\\\"name\\\":\\\"transfer\\\",\\\"outputs\\\":[{\\\"name\\\":\\\"\\\",\\\"type\\\":\\\"bool\\\"}],\\\"payable\\\":false,\\\"stateMutability\\\":\\\"nonpayable\\\",\\\"type\\\":\\\"function\\\"},{\\\"anonymous\\\":false,\\\"inputs\\\":[{\\\"indexed\\\":true,\\\"name\\\":\\\"from\\\",\\\"type\\\":\\\"address\\\"},{\\\"indexed\\\":true,\\\"name\\\":\\\"to\\\",\\\"type\\\":\\\"address\\\"},{\\\"indexed\\\":false,\\\"name\\\":\\\"value\\\",\\\"type\\\":\\\"uint256\\\"}],\\\"name\\\":\\\"Transfer\\\",\\\"type\\\":\\\"event\\\"}]\",\"bin\":\"608060405234801561001057600080fd5b50610281806100206000396000f3006080604052600436106100565763ffffffff7c010000000000000000000000000000000000000000000000000000000060003504166318160ddd811461005b57806370a0823114610082578063a9059cbb146100b0575b600080fd5b34801561006757600080fd5b506100706100f5565b60408051918252519081900360200190f35b34801561008e57600080fd5b5061007073ffffffffffffffffffffffffffffffffffffffff600435166100fb565b3480156100bc57600080fd5b506100e173ffffffffffffffffffffffffffffffffffffffff60043516602435610123565b604080519115158252519081900360200190f35b60005481565b73ffffffffffffffffffffffffffffffffffffffff1660009081526001602052604090205490565b600073ffffffffffffffffffffffffffffffffffffffff8316151561014757600080fd5b3360009081526001602052604090205482111561016357600080fd5b33600090815260016020526040902054610183908363ffffffff61022d16565b336000908152600160205260408082209290925573ffffffffffffffffffffffffffffffffffffffff8516815220546101c2908363ffffffff61023f16565b73ffffffffffffffffffffffffffffffffffffffff84166000818152600160209081526040918290209390935580518581529051919233927fddf252ad1be2c89b69c2b068fc378daa952ba7f163c4a11628f55a4df523b3ef9281900390910190a350600192915050565b60008282111561023957fe5b50900390565b60008282018381101561024e57fe5b93925050505600a165627a7a7230582035c8e12390c07523c89006aec2830d01f5d6f6b8bdbe19072e7b2de0758a6b690029\"},\"ERC20\":{\"abi\":\"[{\\\"constant\\\":false,\\\"inputs\\\":[{\\\"name\\\":\\\"spender\\\",\\\"type\\\":\\\"address\\\"},{\\\"name\\\":\\\"value\\\",\\\"type\\\":\\\"uint256\\\"}],\\\"name\\\":\\\"approve\\\",\\\"outputs\\\":[{\\\"name\\\":\\\"\\\",\\\"type\\\":\\\"bool\\\"}],\\\"payable\\\":false,\\\"stateMutability\\\":\\\"nonpayable\\\",\\\"type\\\":\\\"function\\\"},{\\\"constant\\\":true,\\\"inputs\\\":[],\\\"name\\\":\\\"totalSupply\\\",\\\"outputs\\\":[{\\\"name\\\":\\\"\\\",\\\"type\\\":\\\"uint256\\\"}],\\\"payable\\\":false,\\\"stateMutability\\\":\\\"view\\\",\\\"type\\\":\\\"function\\\"},{\\\"constant\\\":false,\\\"inputs\\\":[{\\\"name\\\":\\\"from\\\",\\\"type\\\":\\\"address\\\"},{\\\"name\\\":\\\"to\\\",\\\"type\\\":\\\"address\\\"},{\\\"name\\\":\\\"value\\\",\\\"type\\\":\\\"uint256\\\"}],\\\"name\\\":\\\"transferFrom\\\",\\\"outputs\\\":[{\\\"name\\\":\\\"\\\",\\\"type\\\":\\\"bool\\\"}],\\\"payable\\\":false,\\\"stateMutability\\\":\\\"nonpayable\\\",\\\"type\\\":\\\"function\\\"},{\\\"constant\\\":true,\\\"inputs\\\":[{\\\"name\\\":\\\"who\\\",\\\"type\\\":\\\"address\\\"}],\\\"name\\\":\\\"balanceOf\\\",\\\"outputs\\\":[{\\\"name\\\":\\\"\\\",\\\"type\\\":\\\"uint256\\\"}],\\\"payable\\\":false,\\\"stateMutability\\\":\\\"view\\\",\\\"type\\\":\\\"function\\\"},{\\\"constant\\\":false,\\\"inputs\\\":[{\\\"name\\\":\\\"to\\\",\\\"type\\\":\\\"address\\\"},{\\\"name\\\":\\\"value\\\",\\\"type\\\":\\\"uint256\\\"}],\\\"name\\\":\\\"transfer\\\",\\\"outputs\\\":[{\\\"name\\\":\\\"\\\",\\\"type\\\":\\\"bool\\\"}],\\\"payable\\\":false,\\\"stateMutability\\\":\\\"nonpayable\\\",\\\"type\\\":\\\"function\\\"},{\\\"constant\\\":true,\\\"inputs\\\":[{\\\"name\\\":\\\"owner\\\",\\\"type\\\":\\\"address\\\"},{\\\"name\\\":\\\"spender\\\",\\\"type\\\":\\\"address\\\"}],\\\"name\\\":\\\"allowance\\\",\\\"outputs\\\":[{\\\"name\\\":\\\"\\\",\\\"type\\\":\\\"uint256\\\"}],\\\"payable\\\":false,\\\"stateMutability\\\":\\\"view\\\",\\\"type\\\":\\\"function\\\"},{\\\"anonymous\\\":false,\\\"inputs\\\":[{\\\"indexed\\\":true,\\\"name\\\":\\\"owner\\\",\\\"type\\\":\\\"address\\\"},{\\\"indexed\\\":true,\\\"name\\\":\\\"spender\\\",\\\"type\\\":\\\"address\\\"},{\\\"indexed\\\":false,\\\"name\\\":\\\"value\\\",\\\"type\\\":\\\"uint256\\\"}],\\\"name\\\":\\\"Approval\\\",\\\"type\\\":\\\"event\\\"},{\\\"anonymous\\\":false,\\\"inputs\\\":[{\\\"indexed\\\":true,\\\"name\\\":\\\"from\\\",\\\"type\\\":\\\"address\\\"},{\\\"indexed\\\":true,\\\"name\\\":\\\"to\\\",\\\"type\\\":\\\"address\\\"},{\\\"indexed\\\":false,\\\"name\\\":\\\"value\\\",\\\"type\\\":\\\"uint256\\\"}],\\\"name\\\":\\\"Transfer\\\",\\\"type\\\":\\\"event\\\"}]\",\"bin\":\"\"},\"ERC20Basic\":{\"abi\":\"[{\\\"constant\\\":true,\\\"inputs\\\":[],\\\"name\\\":\\\"totalSupply\\\",\\\"outputs\\\":[{\\\"name\\\":\\\"\\\",\\\"type\\\":\\\"uint256\\\"}],\\\"payable\\\":false,\\\"stateMutability\\\":\\\"view\\\",\\\"type\\\":\\\"function\\\"},{\\\"constant\\\":true,\\\"inputs\\\":[{\\\"name\\\":\\\"who\\\",\\\"type\\\":\\\"address\\\"}],\\\"name\\\":\\\"balanceOf\\\",\\\"outputs\\\":[{\\\"name\\\":\\\"\\\",\\\"type\\\":\\\"uint256\\\"}],\\\"payable\\\":false,\\\"stateMutability\\\":\\\"view\\\",\\\"type\\\":\\\"function\\\"},{\\\"constant\\\":false,\\\"inputs\\\":[{\\\"name\\\":\\\"to\\\",\\\"type\\\":\\\"address\\\"},{\\\"name\\\":\\\"value\\\",\\\"type\\\":\\\"uint256\\\"}],\\\"name\\\":\\\"transfer\\\",\\\"outputs\\\":[{\\\"name\\\":\\\"\\\",\\\"type\\\":\\\"bool\\\"}],\\\"payable\\\":false,\\\"stateMutability\\\":\\\"nonpayable\\\",\\\"type\\\":\\\"function\\\"},{\\\"anonymous\\\":false,\\\"inputs\\\":[{\\\"indexed\\\":true,\\\"name\\\":\\\"from\\\",\\\"type\\\":\\\"address\\\"},{\\\"indexed\\\":true,\\\"name\\\":\\\"to\\\",\\\"type\\\":\\\"address\\\"},{\\\"indexed\\\":false,\\\"name\\\":\\\"value\\\",\\\"type\\\":\\\"uint256\\\"}],\\\"name\\\":\\\"Transfer\\\",\\\"type\\\":\\\"event\\\"}]\",\"bin\":\"\"},\"LinGangToken\":{\"abi\":\"[{\\\"constant\\\":true,\\\"inputs\\\":[],\\\"name\\\":\\\"name\\\",\\\"outputs\\\":[{\\\"name\\\":\\\"\\\",\\\"type\\\":\\\"string\\\"}],\\\"payable\\\":false,\\\"stateMutability\\\":\\\"view\\\",\\\"type\\\":\\\"function\\\"},{\\\"constant\\\":false,\\\"inputs\\\":[{\\\"name\\\":\\\"_spender\\\",\\\"type\\\":\\\"address\\\"},{\\\"name\\\":\\\"_value\\\",\\\"type\\\":\\\"uint256\\\"}],\\\"name\\\":\\\"approve\\\",\\\"outputs\\\":[{\\\"name\\\":\\\"\\\",\\\"type\\\":\\\"bool\\\"}],\\\"payable\\\":false,\\\"stateMutability\\\":\\\"nonpayable\\\",\\\"type\\\":\\\"function\\\"},{\\\"constant\\\":true,\\\"inputs\\\":[],\\\"name\\\":\\\"totalSupply\\\",\\\"outputs\\\":[{\\\"name\\\":\\\"\\\",\\\"type\\\":\\\"uint256\\\"}],\\\"payable\\\":false,\\\"stateMutability\\\":\\\"view\\\",\\\"type\\\":\\\"function\\\"},{\\\"constant\\\":false,\\\"inputs\\\":[{\\\"name\\\":\\\"_from\\\",\\\"type\\\":\\\"address\\\"},{\\\"name\\\":\\\"_to\\\",\\\"type\\\":\\\"address\\\"},{\\\"name\\\":\\\"_value\\\",\\\"type\\\":\\\"uint256\\\"}],\\\"name\\\":\\\"transferFrom\\\",\\\"outputs\\\":[{\\\"name\\\":\\\"\\\",\\\"type\\\":\\\"bool\\\"}],\\\"payable\\\":false,\\\"stateMutability\\\":\\\"nonpayable\\\",\\\"type\\\":\\\"function\\\"},{\\\"constant\\\":true,\\\"inputs\\\":[],\\\"name\\\":\\\"decimals\\\",\\\"outputs\\\":[{\\\"name\\\":\\\"\\\",\\\"type\\\":\\\"uint256\\\"}],\\\"payable\\\":false,\\\"stateMutability\\\":\\\"view\\\",\\\"type\\\":\\\"function\\\"},{\\\"constant\\\":true,\\\"inputs\\\":[{\\\"name\\\":\\\"_owner\\\",\\\"type\\\":\\\"address\\\"}],\\\"name\\\":\\\"balanceOf\\\",\\\"outputs\\\":[{\\\"name\\\":\\\"balance\\\",\\\"type\\\":\\\"uint256\\\"}],\\\"payable\\\":false,\\\"stateMutability\\\":\\\"view\\\",\\\"type\\\":\\\"function\\\"},{\\\"constant\\\":true,\\\"inputs\\\":[],\\\"name\\\":\\\"symbol\\\",\\\"outputs\\\":[{\\\"name\\\":\\\"\\\",\\\"type\\\":\\\"string\\\"}],\\\"payable\\\":false,\\\"stateMutability\\\":\\\"view\\\",\\\"type\\\":\\\"function\\\"},{\\\"constant\\\":false,\\\"inputs\\\":[{\\\"name\\\":\\\"_to\\\",\\\"type\\\":\\\"address\\\"},{\\\"name\\\":\\\"_value\\\",\\\"type\\\":\\\"uint256\\\"}],\\\"name\\\":\\\"transfer\\\",\\\"outputs\\\":[{\\\"name\\\":\\\"\\\",\\\"type\\\":\\\"bool\\\"}],\\\"payable\\\":false,\\\"stateMutability\\\":\\\"nonpayable\\\",\\\"type\\\":\\\"function\\\"},{\\\"constant\\\":true,\\\"inputs\\\":[{\\\"name\\\":\\\"_owner\\\",\\\"type\\\":\\\"address\\\"},{\\\"name\\\":\\\"_spender\\\",\\\"type\\\":\\\"address\\\"}],\\\"name\\\":\\\"allowance\\\",\\\"outputs\\\":[{\\\"name\\\":\\\"\\\",\\\"type\\\":\\\"uint256\\\"}],\\\"payable\\\":false,\\\"stateMutability\\\":\\\"view\\\",\\\"type\\\":\\\"function\\\"},{\\\"inputs\\\":[],\\\"payable\\\":false,\\\"stateMutability\\\":\\\"nonpayable\\\",\\\"type\\\":\\\"constructor\\\"},{\\\"anonymous\\\":false,\\\"inputs\\\":[{\\\"indexed\\\":true,\\\"name\\\":\\\"owner\\\",\\\"type\\\":\\\"address\\\"},{\\\"indexed\\\":true,\\\"name\\\":\\\"spender\\\",\\\"type\\\":\\\"address\\\"},{\\\"indexed\\\":false,\\\"name\\\":\\\"value\\\",\\\"type\\\":\\\"uint256\\\"}],\\\"name\\\":\\\"Approval\\\",\\\"type\\\":\\\"event\\\"},{\\\"anonymous\\\":false,\\\"inputs\\\":[{\\\"indexed\\\":true,\\\"name\\\":\\\"from\\\",\\\"type\\\":\\\"address\\\"},{\\\"indexed\\\":true,\\\"name\\\":\\\"to\\\",\\\"type\\\":\\\"address\\\"},{\\\"indexed\\\":false,\\\"name\\\":\\\"value\\\",\\\"type\\\":\\\"uint256\\\"}],\\\"name\\\":\\\"Transfer\\\",\\\"type\\\":\\\"event\\\"}]\",\"bin\":\"608060405234801561001057600080fd5b506b033b2e3c9fd0803ce8000000600081815533815260016020908152604091829020929092558051808201909152600c8082527f4c696e47616e67546f6b656e00000000000000000000000000000000000000009190920190815261007991600391906100c9565b506040805180820190915260078082527f4c696e47616e670000000000000000000000000000000000000000000000000060209092019182526100be916004916100c9565b506012600555610164565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f1061010a57805160ff1916838001178555610137565b82800160010185558215610137579182015b8281111561013757825182559160200191906001019061011c565b50610143929150610147565b5090565b61016191905b80821115610143576000815560010161014d565b90565b610697806101736000396000f3006080604052600436106100985763ffffffff7c010000000000000000000000000000000000000000000000000000000060003504166306fdde03811461009d578063095ea7b31461012757806318160ddd1461015f57806323b872dd14610186578063313ce567146101b057806370a08231146101c557806395d89b41146101e6578063a9059cbb146101fb578063dd62ed3e1461021f575b600080fd5b3480156100a957600080fd5b506100b2610246565b6040805160208082528351818301528351919283929083019185019080838360005b838110156100ec5781810151838201526020016100d4565b50505050905090810190601f1680156101195780820380516001836020036101000a031916815260200191505b509250505060405180910390f35b34801561013357600080fd5b5061014b600160a060020a03600435166024356102d4565b604080519115158252519081900360200190f35b34801561016b57600080fd5b5061017461033a565b60408051918252519081900360200190f35b34801561019257600080fd5b5061014b600160a060020a0360043581169060243516604435610340565b3480156101bc57600080fd5b506101746104b9565b3480156101d157600080fd5b50610174600160a060020a03600435166104bf565b3480156101f257600080fd5b506100b26104da565b34801561020757600080fd5b5061014b600160a060020a0360043516602435610535565b34801561022b57600080fd5b50610174600160a060020a0360043581169060243516610618565b6003805460408051602060026001851615610100026000190190941693909304601f810184900484028201840190925281815292918301828280156102cc5780601f106102a1576101008083540402835291602001916102cc565b820191906000526020600020905b8154815290600101906020018083116102af57829003601f168201915b505050505081565b336000818152600260209081526040808320600160a060020a038716808552908352818420869055815186815291519394909390927f8c5be1e5ebec7d5bd14f71427d1e84f3dd0314c0f7b2291e5b200ac8c7c3b925928290030190a350600192915050565b60005481565b6000600160a060020a038316151561035757600080fd5b600160a060020a03841660009081526001602052604090205482111561037c57600080fd5b600160a060020a03841660009081526002602090815260408083203384529091529020548211156103ac57600080fd5b600160a060020a0384166000908152600160205260409020546103d5908363ffffffff61064316565b600160a060020a03808616600090815260016020526040808220939093559085168152205461040a908363ffffffff61065516565b600160a060020a03808516600090815260016020908152604080832094909455918716815260028252828120338252909152205461044e908363ffffffff61064316565b600160a060020a03808616600081815260026020908152604080832033845282529182902094909455805186815290519287169391927fddf252ad1be2c89b69c2b068fc378daa952ba7f163c4a11628f55a4df523b3ef929181900390910190a35060019392505050565b60055481565b600160a060020a031660009081526001602052604090205490565b6004805460408051602060026001851615610100026000190190941693909304601f810184900484028201840190925281815292918301828280156102cc5780601f106102a1576101008083540402835291602001916102cc565b6000600160a060020a038316151561054c57600080fd5b3360009081526001602052604090205482111561056857600080fd5b33600090815260016020526040902054610588908363ffffffff61064316565b3360009081526001602052604080822092909255600160a060020a038516815220546105ba908363ffffffff61065516565b600160a060020a0384166000818152600160209081526040918290209390935580518581529051919233927fddf252ad1be2c89b69c2b068fc378daa952ba7f163c4a11628f55a4df523b3ef9281900390910190a350600192915050565b600160a060020a03918216600090815260026020908152604080832093909416825291909152205490565b60008282111561064f57fe5b50900390565b60008282018381101561066457fe5b93925050505600a165627a7a723058208cb964e820f89803a3b6debfd341c207877ea609ef7eec03b7dc4ae9c3fd59800029\"},\"SafeMath\":{\"abi\":\"[]\",\"bin\":\"604c602c600b82828239805160001a60731460008114601c57601e565bfe5b5030600052607381538281f30073000000000000000000000000000000000000000030146080604052600080fd00a165627a7a72305820d448a0f6a66e1390f30632214436b5cca6b794a31ed2725e13d0586f37f4450c0029\"},\"StandardToken\":{\"abi\":\"[{\\\"constant\\\":false,\\\"inputs\\\":[{\\\"name\\\":\\\"_spender\\\",\\\"type\\\":\\\"address\\\"},{\\\"name\\\":\\\"_value\\\",\\\"type\\\":\\\"uint256\\\"}],\\\"name\\\":\\\"approve\\\",\\\"outputs\\\":[{\\\"name\\\":\\\"\\\",\\\"type\\\":\\\"bool\\\"}],\\\"payable\\\":false,\\\"stateMutability\\\":\\\"nonpayable\\\",\\\"type\\\":\\\"function\\\"},{\\\"constant\\\":true,\\\"inputs\\\":[],\\\"name\\\":\\\"totalSupply\\\",\\\"outputs\\\":[{\\\"name\\\":\\\"\\\",\\\"type\\\":\\\"uint256\\\"}],\\\"payable\\\":false,\\\"stateMutability\\\":\\\"view\\\",\\\"type\\\":\\\"function\\\"},{\\\"constant\\\":false,\\\"inputs\\\":[{\\\"name\\\":\\\"_from\\\",\\\"type\\\":\\\"address\\\"},{\\\"name\\\":\\\"_to\\\",\\\"type\\\":\\\"address\\\"},{\\\"name\\\":\\\"_value\\\",\\\"type\\\":\\\"uint256\\\"}],\\\"name\\\":\\\"transferFrom\\\",\\\"outputs\\\":[{\\\"name\\\":\\\"\\\",\\\"type\\\":\\\"bool\\\"}],\\\"payable\\\":false,\\\"stateMutability\\\":\\\"nonpayable\\\",\\\"type\\\":\\\"function\\\"},{\\\"constant\\\":true,\\\"inputs\\\":[{\\\"name\\\":\\\"_owner\\\",\\\"type\\\":\\\"address\\\"}],\\\"name\\\":\\\"balanceOf\\\",\\\"outputs\\\":[{\\\"name\\\":\\\"balance\\\",\\\"type\\\":\\\"uint256\\\"}],\\\"payable\\\":false,\\\"stateMutability\\\":\\\"view\\\",\\\"type\\\":\\\"function\\\"},{\\\"constant\\\":false,\\\"inputs\\\":[{\\\"name\\\":\\\"_to\\\",\\\"type\\\":\\\"address\\\"},{\\\"name\\\":\\\"_value\\\",\\\"type\\\":\\\"uint256\\\"}],\\\"name\\\":\\\"transfer\\\",\\\"outputs\\\":[{\\\"name\\\":\\\"\\\",\\\"type\\\":\\\"bool\\\"}],\\\"payable\\\":false,\\\"stateMutability\\\":\\\"nonpayable\\\",\\\"type\\\":\\\"function\\\"},{\\\"constant\\\":true,\\\"inputs\\\":[{\\\"name\\\":\\\"_owner\\\",\\\"type\\\":\\\"address\\\"},{\\\"name\\\":\\\"_spender\\\",\\\"type\\\":\\\"address\\\"}],\\\"name\\\":\\\"allowance\\\",\\\"outputs\\\":[{\\\"name\\\":\\\"\\\",\\\"type\\\":\\\"uint256\\\"}],\\\"payable\\\":false,\\\"stateMutability\\\":\\\"view\\\",\\\"type\\\":\\\"function\\\"},{\\\"anonymous\\\":false,\\\"inputs\\\":[{\\\"indexed\\\":true,\\\"name\\\":\\\"owner\\\",\\\"type\\\":\\\"address\\\"},{\\\"indexed\\\":true,\\\"name\\\":\\\"spender\\\",\\\"type\\\":\\\"address\\\"},{\\\"indexed\\\":false,\\\"name\\\":\\\"value\\\",\\\"type\\\":\\\"uint256\\\"}],\\\"name\\\":\\\"Approval\\\",\\\"type\\\":\\\"event\\\"},{\\\"anonymous\\\":false,\\\"inputs\\\":[{\\\"indexed\\\":true,\\\"name\\\":\\\"from\\\",\\\"type\\\":\\\"address\\\"},{\\\"indexed\\\":true,\\\"name\\\":\\\"to\\\",\\\"type\\\":\\\"address\\\"},{\\\"indexed\\\":false,\\\"name\\\":\\\"value\\\",\\\"type\\\":\\\"uint256\\\"}],\\\"name\\\":\\\"Transfer\\\",\\\"type\\\":\\\"event\\\"}]\",\"bin\":\"608060405234801561001057600080fd5b506104d3806100206000396000f3006080604052600436106100775763ffffffff7c0100000000000000000000000000000000000000000000000000000000600035041663095ea7b3811461007c57806318160ddd146100b457806323b872dd146100db57806370a0823114610105578063a9059cbb14610126578063dd62ed3e1461014a575b600080fd5b34801561008857600080fd5b506100a0600160a060020a0360043516602435610171565b604080519115158252519081900360200190f35b3480156100c057600080fd5b506100c96101d7565b60408051918252519081900360200190f35b3480156100e757600080fd5b506100a0600160a060020a03600435811690602435166044356101dd565b34801561011157600080fd5b506100c9600160a060020a0360043516610356565b34801561013257600080fd5b506100a0600160a060020a0360043516602435610371565b34801561015657600080fd5b506100c9600160a060020a0360043581169060243516610454565b336000818152600260209081526040808320600160a060020a038716808552908352818420869055815186815291519394909390927f8c5be1e5ebec7d5bd14f71427d1e84f3dd0314c0f7b2291e5b200ac8c7c3b925928290030190a350600192915050565b60005481565b6000600160a060020a03831615156101f457600080fd5b600160a060020a03841660009081526001602052604090205482111561021957600080fd5b600160a060020a038416600090815260026020908152604080832033845290915290205482111561024957600080fd5b600160a060020a038416600090815260016020526040902054610272908363ffffffff61047f16565b600160a060020a0380861660009081526001602052604080822093909355908516815220546102a7908363ffffffff61049116565b600160a060020a0380851660009081526001602090815260408083209490945591871681526002825282812033825290915220546102eb908363ffffffff61047f16565b600160a060020a03808616600081815260026020908152604080832033845282529182902094909455805186815290519287169391927fddf252ad1be2c89b69c2b068fc378daa952ba7f163c4a11628f55a4df523b3ef929181900390910190a35060019392505050565b600160a060020a031660009081526001602052604090205490565b6000600160a060020a038316151561038857600080fd5b336000908152600160205260409020548211156103a457600080fd5b336000908152600160205260409020546103c4908363ffffffff61047f16565b3360009081526001602052604080822092909255600160a060020a038516815220546103f6908363ffffffff61049116565b600160a060020a0384166000818152600160209081526040918290209390935580518581529051919233927fddf252ad1be2c89b69c2b068fc378daa952ba7f163c4a11628f55a4df523b3ef9281900390910190a350600192915050565b600160a060020a03918216600090815260026020908152604080832093909416825291909152205490565b60008282111561048b57fe5b50900390565b6000828201838110156104a057fe5b93925050505600a165627a7a72305820f23cb21f36dd3eb188681979773f8e67aa25f5da9df35393a61a8cc0547bbdf50029\"}},\"version\":\"0.4.25+commit.59dbf8f1.Linux.g++\"}";

        return str;
    }

/*    private SolidityContractDetails compireContractDetail(HpbFacade hpbFacade) {
        try {
            String rawJson = new String(this.getResult().getBytes(HpbFacade.CHARSET), HpbFacade.CHARSET);
            CompilationResult result = CompilationResult.parse(rawJson);
            *//**
             *  合约文件中，所有contract关键词的对象，被解析成合约detail;
             *//*
            Collection<? extends SolidityContractDetails> contracts = result.getContracts();
            Iterator<SolidityContractDetails> solidityContractDetailsIteratorolidityContractDetailsI = (Iterator<SolidityContractDetails>) contracts.iterator();
            while (solidityContractDetailsIteratorolidityContractDetailsI.hasNext()) {
                SolidityContractDetails details = solidityContractDetailsIteratorolidityContractDetailsI.next();
                logger.info("getAbi ===" + GsonUtil.gsonString(details.getAbi()));
                for (AbiEntry abiEntry : details.getAbi()) {
                    logger.info("Abi.AbiEntry ===" + GsonUtil.gsonString(abiEntry));
                }
                logger.info("getBinary ===" + details.getBinary());
                logger.info("getMetadata ===" + GsonUtil.gsonString(details.getMetadata()));
                Optional<TypedSolidityEvent<EventTransfer_address_address_uint256>> transferEvent = hpbFacade.findEventDefinition(details, "Transfer", EventTransfer_address_address_uint256.class);
                if (transferEvent != null && transferEvent.isPresent()) {
                    logger.info("transferEvent =====" + transferEvent.get().rawDefinition());
                }
                DefaultBlockParameter startBlock = DefaultBlockParameter.valueOf(BigInteger.valueOf(3000000));
                DefaultBlockParameter toBlock = io.hpb.web3.protocol.core.DefaultBlockParameterName.LATEST;
                HpbAddress contractAddress = HpbAddress.of("0xa112125702b5b54362a37c7aa633692ad480f20e");
                Event event = new Event("Transfer",
                        Arrays.<TypeReference<?>>asList(
                                new TypeReference<Address>(true) {
                                },
                                new TypeReference<Address>(true) {
                                }, new TypeReference<Uint256>(false) {
                                }
                        )
                );

                String topicData = EventEncoder.encode(event);
                try {
                    hpbFacade.observeEvents(transferEvent.get(), contractAddress);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                //List<EventData> logs = hpbFacade.getLogs(startBlock, toBlock, transferEvent, contractAddress,topicData);
            }
            logger.info("result ===" + result.getContracts());
            return null;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }*/
}
