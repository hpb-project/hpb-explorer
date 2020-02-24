package com.hpb.bc.util;

import io.hpb.web3.abi.EventEncoder;
import io.hpb.web3.abi.TypeReference;
import io.hpb.web3.abi.datatypes.Address;
import io.hpb.web3.abi.datatypes.Bool;
import io.hpb.web3.abi.datatypes.DynamicBytes;
import io.hpb.web3.abi.datatypes.Event;
import io.hpb.web3.abi.datatypes.Function;
import io.hpb.web3.abi.datatypes.Type;
import io.hpb.web3.abi.datatypes.Utf8String;
import io.hpb.web3.abi.datatypes.generated.Bytes4;
import io.hpb.web3.abi.datatypes.generated.Uint16;
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
public class HeroAsset extends Contract {
    private static final String BINARY = "600d805460ff19908116909155600f8054909116905560e0604052602d60808190527f68747470733a2f2f7777772e6d7963727970746f6865726f65732e6e65742f6d60a09081527f657461646174612f6865726f2f0000000000000000000000000000000000000060c0526200007a9160109190620002db565b503480156200008857600080fd5b50604080518082018252601381527f4d7943727970746f4865726f65733a4865726f000000000000000000000000006020808301919091528251808401909352600483527f4d4348480000000000000000000000000000000000000000000000000000000090830152908181620001287f01ffc9a70000000000000000000000000000000000000000000000000000000064010000000062000233810204565b6200015c7f80ac58cd0000000000000000000000000000000000000000000000000000000064010000000062000233810204565b620001907f780e9d630000000000000000000000000000000000000000000000000000000064010000000062000233810204565b8151620001a5906009906020850190620002db565b508051620001bb90600a906020840190620002db565b50620001f07f5b5e139f0000000000000000000000000000000000000000000000000000000064010000000062000233810204565b50620002129250600c9150339050640100000000620013da620002a082021704565b6200022d600e33640100000000620013da620002a082021704565b62000380565b7fffffffff0000000000000000000000000000000000000000000000000000000080821614156200026357600080fd5b7fffffffff00000000000000000000000000000000000000000000000000000000166000908152602081905260409020805460ff19166001179055565b600160a060020a0381161515620002b657600080fd5b600160a060020a0316600090815260209190915260409020805460ff19166001179055565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f106200031e57805160ff19168380011785556200034e565b828001600101855582156200034e579182015b828111156200034e57825182559160200191906001019062000331565b506200035c92915062000360565b5090565b6200037d91905b808211156200035c576000815560010162000367565b90565b611b4380620003906000396000f3006080604052600436106101b65763ffffffff7c010000000000000000000000000000000000000000000000000000000060003504166301ffc9a781146101bb57806305d2035b1461020657806306fdde031461021b578063081812fc146102a5578063095ea7b3146102d957806318160ddd146102ff57806323b872dd146103265780632f745c59146103505780633f4ba83a1461037457806340c10f191461038957806342842e0e146103ad57806346fbf68e146103d75780634890087f146103f85780634d5b335d146104245780634f6ccce71461044857806350bb4e7f146104605780635c975abb146104c95780636352211e146104de5780636ef8d66d146104f65780636fa23f731461050b57806370a082311461052d5780637d64bcb41461054e57806382dc1ec4146105635780638450b12e146105845780638456cb59146105a057806395d89b41146105b5578063983b2d56146105ca57806398650275146105eb57806399e0dd7c14610600578063a22cb46514610620578063aa271e1a14610646578063b88d4fde14610667578063c0ac9983146106d6578063c87b56dd146106eb578063e985e9c514610703575b600080fd5b3480156101c757600080fd5b506101f27bffffffffffffffffffffffffffffffffffffffffffffffffffffffff196004351661072a565b604080519115158252519081900360200190f35b34801561021257600080fd5b506101f261075e565b34801561022757600080fd5b50610230610768565b6040805160208082528351818301528351919283929083019185019080838360005b8381101561026a578181015183820152602001610252565b50505050905090810190601f1680156102975780820380516001836020036101000a031916815260200191505b509250505060405180910390f35b3480156102b157600080fd5b506102bd6004356107fe565b60408051600160a060020a039092168252519081900360200190f35b3480156102e557600080fd5b506102fd600160a060020a0360043516602435610830565b005b34801561030b57600080fd5b5061031461084e565b60408051918252519081900360200190f35b34801561033257600080fd5b506102fd600160a060020a0360043581169060243516604435610854565b34801561035c57600080fd5b50610314600160a060020a0360043516602435610874565b34801561038057600080fd5b506102fd6108c1565b34801561039557600080fd5b506101f2600160a060020a036004351660243561091b565b3480156103b957600080fd5b506102fd600160a060020a0360043581169060243516604435610954565b3480156103e357600080fd5b506101f2600160a060020a0360043516610970565b34801561040457600080fd5b5061040d610989565b6040805161ffff9092168252519081900360200190f35b34801561043057600080fd5b506102fd600160a060020a036004351660243561098f565b34801561045457600080fd5b50610314600435610a53565b34801561046c57600080fd5b50604080516020600460443581810135601f81018490048402850184019095528484526101f2948235600160a060020a0316946024803595369594606494920191908190840183828082843750949750610a889650505050505050565b3480156104d557600080fd5b506101f2610acd565b3480156104ea57600080fd5b506102bd600435610ad6565b34801561050257600080fd5b506102fd610afa565b34801561051757600080fd5b506102fd61ffff60043581169060243516610b0d565b34801561053957600080fd5b50610314600160a060020a0360043516610beb565b34801561055a57600080fd5b506101f2610c1e565b34801561056f57600080fd5b506102fd600160a060020a0360043516610c80565b34801561059057600080fd5b5061040d61ffff60043516610cdc565b3480156105ac57600080fd5b506102fd610cf5565b3480156105c157600080fd5b50610230610d51565b3480156105d657600080fd5b506102fd600160a060020a0360043516610db2565b3480156105f757600080fd5b506102fd610e0e565b34801561060c57600080fd5b506102fd6004803560248101910135610e1f565b34801561062c57600080fd5b506102fd600160a060020a03600435166024351515610e3f565b34801561065257600080fd5b506101f2600160a060020a0360043516610e59565b34801561067357600080fd5b50604080516020601f6064356004818101359283018490048402850184019095528184526102fd94600160a060020a038135811695602480359092169560443595369560849401918190840183828082843750949750610e6c9650505050505050565b3480156106e257600080fd5b50610230610e8e565b3480156106f757600080fd5b50610230600435610f1c565b34801561070f57600080fd5b506101f2600160a060020a036004358116906024351661115b565b7bffffffffffffffffffffffffffffffffffffffffffffffffffffffff191660009081526020819052604090205460ff1690565b600d5460ff165b90565b60098054604080516020601f60026000196101006001881615020190951694909404938401819004810282018101909252828152606093909290918301828280156107f45780601f106107c9576101008083540402835291602001916107f4565b820191906000526020600020905b8154815290600101906020018083116107d757829003601f168201915b5050505050905090565b600061080982611189565b151561081457600080fd5b50600090815260026020526040902054600160a060020a031690565b600f5460ff161561084057600080fd5b61084a82826111a6565b5050565b60075490565b600f5460ff161561086457600080fd5b61086f83838361125c565b505050565b600061087f83610beb565b821061088a57600080fd5b600160a060020a03831660009081526005602052604090208054839081106108ae57fe5b9060005260206000200154905092915050565b6108ca33610970565b15156108d557600080fd5b600f5460ff1615156108e657600080fd5b600f805460ff191690556040517fa45f47fdea8a1efdd9029a5691c7f759c32b7c698632b563573e155625d1693390600090a1565b600061092633610e59565b151561093157600080fd5b600d5460ff161561094157600080fd5b61094b83836112ea565b50600192915050565b61086f8383836020604051908101604052806000815250610e6c565b6000610983600e8363ffffffff61133916565b92915050565b61271081565b60008061099b33610e59565b15156109a657600080fd5b6127108304915060016127108461ffff80861660009081526011602052604090205492909106929092039250811690821610610a4357604080517f08c379a000000000000000000000000000000000000000000000000000000000815260206004820152600b60248201527f737570706c79206f766572000000000000000000000000000000000000000000604482015290519081900360640190fd5b610a4d84846112ea565b50505050565b6000610a5d61084e565b8210610a6857600080fd5b6007805483908110610a7657fe5b90600052602060002001549050919050565b6000610a9333610e59565b1515610a9e57600080fd5b600d5460ff1615610aae57600080fd5b610ab8848461091b565b50610ac38383611370565b5060019392505050565b600f5460ff1690565b600081815260016020526040812054600160a060020a031680151561098357600080fd5b610b0b600e3363ffffffff6113a316565b565b610b1633610e59565b1515610b2157600080fd5b61ffff808316600090815260116020526040902054161580610b5a575061ffff8083166000908152601160205260409020548116908216105b1515610bc757604080517f08c379a000000000000000000000000000000000000000000000000000000000815260206004820152601660248201527f5f737570706c794c696d69742069732062696767657200000000000000000000604482015290519081900360640190fd5b61ffff9182166000908152601160205260409020805461ffff191691909216179055565b6000600160a060020a0382161515610c0257600080fd5b50600160a060020a031660009081526003602052604090205490565b6000610c2933610e59565b1515610c3457600080fd5b600d5460ff1615610c4457600080fd5b600d805460ff191660011790556040517fb828d9b5c78095deeeeff2eca2e5d4fe046ce3feb4c99702624a3fd384ad2dbc90600090a150600190565b610c8933610970565b1515610c9457600080fd5b610ca5600e8263ffffffff6113da16565b604051600160a060020a038216907f6719d08c1888103bea251a4ed56406bd0c3e69723c8a1686e017e7bbe159b6f890600090a250565b61ffff9081166000908152601160205260409020541690565b610cfe33610970565b1515610d0957600080fd5b600f5460ff1615610d1957600080fd5b600f805460ff191660011790556040517f9e87fac88ff661f02d44f95383c817fece4bce600a3dab7a54406878b965e75290600090a1565b600a8054604080516020601f60026000196101006001881615020190951694909404938401819004810282018101909252828152606093909290918301828280156107f45780601f106107c9576101008083540402835291602001916107f4565b610dbb33610e59565b1515610dc657600080fd5b610dd7600c8263ffffffff6113da16565b604051600160a060020a038216907f6ae172837ea30b801fbfcdd4108aa1d5bf8ff775444fd70256b44e6bf3dfc3f690600090a250565b610b0b600c3363ffffffff6113a316565b610e2833610e59565b1515610e3357600080fd5b61086f601083836119f1565b600f5460ff1615610e4f57600080fd5b61084a8282611414565b6000610983600c8363ffffffff61133916565b610e77848484610854565b610e8384848484611498565b1515610a4d57600080fd5b6010805460408051602060026001851615610100026000190190941693909304601f81018490048402820184019092528181529291830182828015610f145780601f10610ee957610100808354040283529160200191610f14565b820191906000526020600020905b815481529060010190602001808311610ef757829003601f168201915b505050505081565b606060008082808280871515610f54577f30000000000000000000000000000000000000000000000000000000000000009550610f90565b8794505b6000851115610f905761010086049550600a850660300160f860020a0260010286179550600a85811515610f8857fe5b049450610f58565b6010805460408051602060026001851615610100026000190190941693909304601f810184900484028201840190925281815292918301828280156110165780601f10610feb57610100808354040283529160200191611016565b820191906000526020600020905b815481529060010190602001808311610ff957829003601f168201915b50505050509350602060ff168451016040519080825280601f01601f191660200182016040528015611052578160200160208202803883390190505b50925060009050600091505b83518260ff1610156110db57838260ff1681518110151561107b57fe5b90602001015160f860020a900460f860020a02838260ff1681518110151561109f57fe5b9060200101907effffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff1916908160001a9053506001918201910161105e565b600091505b602060ff8316101561114f578560ff8316602081106110fb57fe5b1a60f860020a02838260ff1681518110151561111357fe5b9060200101907effffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff1916908160001a905350600191820191016110e0565b50909695505050505050565b600160a060020a03918216600090815260046020908152604080832093909416825291909152205460ff1690565b600090815260016020526040902054600160a060020a0316151590565b60006111b182610ad6565b9050600160a060020a0383811690821614156111cc57600080fd5b33600160a060020a03821614806111e857506111e8813361115b565b15156111f357600080fd5b600082815260026020526040808220805473ffffffffffffffffffffffffffffffffffffffff1916600160a060020a0387811691821790925591518593918516917f8c5be1e5ebec7d5bd14f71427d1e84f3dd0314c0f7b2291e5b200ac8c7c3b92591a4505050565b611266338261161a565b151561127157600080fd5b600160a060020a038216151561128657600080fd5b6112908382611679565b61129a83826116e8565b6112a482826117ef565b8082600160a060020a031684600160a060020a03167fddf252ad1be2c89b69c2b068fc378daa952ba7f163c4a11628f55a4df523b3ef60405160405180910390a4505050565b6112f48282611838565b600780546000838152600860205260408120829055600182018355919091527fa66cc928b5edb82af9bd49922954155ab7b0942694bea4ce44661d9a8736c688015550565b6000600160a060020a038216151561135057600080fd5b50600160a060020a03166000908152602091909152604090205460ff1690565b61137982611189565b151561138457600080fd5b6000828152600b60209081526040909120825161086f92840190611a6f565b600160a060020a03811615156113b857600080fd5b600160a060020a0316600090815260209190915260409020805460ff19169055565b600160a060020a03811615156113ef57600080fd5b600160a060020a0316600090815260209190915260409020805460ff19166001179055565b600160a060020a03821633141561142a57600080fd5b336000818152600460209081526040808320600160a060020a03871680855290835292819020805460ff1916861515908117909155815190815290519293927f17307eab39ab6107e8899845ad3d59bd9653f200f220920489ca2b5937696c31929181900390910190a35050565b6000806114ad85600160a060020a0316611893565b15156114bc5760019150611611565b6040517f150b7a020000000000000000000000000000000000000000000000000000000081523360048201818152600160a060020a03898116602485015260448401889052608060648501908152875160848601528751918a169463150b7a0294938c938b938b93909160a490910190602085019080838360005b8381101561154f578181015183820152602001611537565b50505050905090810190601f16801561157c5780820380516001836020036101000a031916815260200191505b5095505050505050602060405180830381600087803b15801561159e57600080fd5b505af11580156115b2573d6000803e3d6000fd5b505050506040513d60208110156115c857600080fd5b50517bffffffffffffffffffffffffffffffffffffffffffffffffffffffff1981167f150b7a020000000000000000000000000000000000000000000000000000000014925090505b50949350505050565b60008061162683610ad6565b905080600160a060020a031684600160a060020a03161480611661575083600160a060020a0316611656846107fe565b600160a060020a0316145b806116715750611671818561115b565b949350505050565b81600160a060020a031661168c82610ad6565b600160a060020a03161461169f57600080fd5b600081815260026020526040902054600160a060020a03161561084a576000908152600260205260409020805473ffffffffffffffffffffffffffffffffffffffff1916905550565b60008060006116f7858561189b565b600084815260066020908152604080832054600160a060020a038916845260059092529091205490935061173290600163ffffffff61193116565b600160a060020a03861660009081526005602052604090208054919350908390811061175a57fe5b90600052602060002001549050806005600087600160a060020a0316600160a060020a031681526020019081526020016000208481548110151561179a57fe5b6000918252602080832090910192909255600160a060020a03871681526005909152604090208054906117d1906000198301611add565b50600093845260066020526040808520859055908452909220555050565b60006117fb8383611948565b50600160a060020a039091166000908152600560209081526040808320805460018101825590845282842081018590559383526006909152902055565b600160a060020a038216151561184d57600080fd5b61185782826117ef565b6040518190600160a060020a038416906000907fddf252ad1be2c89b69c2b068fc378daa952ba7f163c4a11628f55a4df523b3ef908290a45050565b6000903b1190565b81600160a060020a03166118ae82610ad6565b600160a060020a0316146118c157600080fd5b600160a060020a0382166000908152600360205260409020546118eb90600163ffffffff61193116565b600160a060020a03909216600090815260036020908152604080832094909455918152600190915220805473ffffffffffffffffffffffffffffffffffffffff19169055565b6000808383111561194157600080fd5b5050900390565b600081815260016020526040902054600160a060020a03161561196a57600080fd5b6000818152600160208181526040808420805473ffffffffffffffffffffffffffffffffffffffff1916600160a060020a03881690811790915584526003909152909120546119b8916119d8565b600160a060020a0390921660009081526003602052604090209190915550565b6000828201838110156119ea57600080fd5b9392505050565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f10611a325782800160ff19823516178555611a5f565b82800160010185558215611a5f579182015b82811115611a5f578235825591602001919060010190611a44565b50611a6b929150611afd565b5090565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f10611ab057805160ff1916838001178555611a5f565b82800160010185558215611a5f579182015b82811115611a5f578251825591602001919060010190611ac2565b81548183558181111561086f5760008381526020902061086f9181019083015b61076591905b80821115611a6b5760008155600101611b035600a165627a7a72305820f894a9e90ae740668cafc8119de205ad7a166a8ec391d9808200a9dd7c5ebc570029";

    public static final String FUNC_ADDMINTER = "addMinter";

    public static final String FUNC_ADDPAUSER = "addPauser";

    public static final String FUNC_APPROVE = "approve";

    public static final String FUNC_FINISHMINTING = "finishMinting";

    public static final String FUNC_MINT = "mint";

    public static final String FUNC_MINTHEROASSET = "mintHeroAsset";

    public static final String FUNC_MINTWITHTOKENURI = "mintWithTokenURI";

    public static final String FUNC_PAUSE = "pause";

    public static final String FUNC_RENOUNCEMINTER = "renounceMinter";

    public static final String FUNC_RENOUNCEPAUSER = "renouncePauser";

    public static final String FUNC_SAFETRANSFERFROM = "safeTransferFrom";

    public static final String FUNC_SETAPPROVALFORALL = "setApprovalForAll";

    public static final String FUNC_SETSUPPLYLIMIT = "setSupplyLimit";

    public static final String FUNC_SETTOKENURIPREFIX = "setTokenURIPrefix";

    public static final String FUNC_TRANSFERFROM = "transferFrom";

    public static final String FUNC_UNPAUSE = "unpause";

    public static final String FUNC_BALANCEOF = "balanceOf";

    public static final String FUNC_GETAPPROVED = "getApproved";

    public static final String FUNC_GETSUPPLYLIMIT = "getSupplyLimit";

    public static final String FUNC_HERO_TYPE_OFFSET = "HERO_TYPE_OFFSET";

    public static final String FUNC_ISAPPROVEDFORALL = "isApprovedForAll";

    public static final String FUNC_ISMINTER = "isMinter";

    public static final String FUNC_ISPAUSER = "isPauser";

    public static final String FUNC_MINTINGFINISHED = "mintingFinished";

    public static final String FUNC_NAME = "name";

    public static final String FUNC_OWNEROF = "ownerOf";

    public static final String FUNC_PAUSED = "paused";

    public static final String FUNC_SUPPORTSINTERFACE = "supportsInterface";

    public static final String FUNC_SYMBOL = "symbol";

    public static final String FUNC_TOKENBYINDEX = "tokenByIndex";

    public static final String FUNC_TOKENOFOWNERBYINDEX = "tokenOfOwnerByIndex";

    public static final String FUNC_TOKENURI = "tokenURI";

    public static final String FUNC_TOKENURIPREFIX = "tokenURIPrefix";

    public static final String FUNC_TOTALSUPPLY = "totalSupply";

    public static final Event PAUSED_EVENT = new Event("Paused",
            Arrays.<TypeReference<?>>asList());
    ;

    public static final Event UNPAUSED_EVENT = new Event("Unpaused",
            Arrays.<TypeReference<?>>asList());
    ;

    public static final Event PAUSERADDED_EVENT = new Event("PauserAdded",
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>(true) {
            }));
    ;

    public static final Event PAUSERREMOVED_EVENT = new Event("PauserRemoved",
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>(true) {
            }));
    ;

    public static final Event MINTINGFINISHED_EVENT = new Event("MintingFinished",
            Arrays.<TypeReference<?>>asList());
    ;

    public static final Event MINTERADDED_EVENT = new Event("MinterAdded",
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>(true) {
            }));
    ;

    public static final Event MINTERREMOVED_EVENT = new Event("MinterRemoved",
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>(true) {
            }));
    ;

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

    protected HeroAsset(String contractAddress, Web3 web3, Credentials credentials,
                        BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3, getTransactionManager(web3, credentials), getContractGasProvider(gasPrice, gasLimit));
    }

    protected HeroAsset(String contractAddress, Web3 web3, TransactionManager transactionManager,
                        BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3, transactionManager, getContractGasProvider(gasPrice, gasLimit));
    }

    protected HeroAsset(String contractAddress, Web3 web3, TransactionManager transactionManager,
                        ContractGasProvider gasProvider) {
        super(BINARY, contractAddress, web3, transactionManager, gasProvider);
    }

    public RemoteCall<TransactionReceipt> addMinter(Address account) {
        final Function function = new Function(
                FUNC_ADDMINTER,
                Arrays.<Type>asList(account),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<TransactionReceipt> addPauser(Address account) {
        final Function function = new Function(
                FUNC_ADDPAUSER,
                Arrays.<Type>asList(account),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<TransactionReceipt> approve(Address to, Uint256 tokenId) {
        final Function function = new Function(
                FUNC_APPROVE,
                Arrays.<Type>asList(to, tokenId),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<TransactionReceipt> finishMinting() {
        final Function function = new Function(
                FUNC_FINISHMINTING,
                Arrays.<Type>asList(),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<TransactionReceipt> mint(Address to, Uint256 tokenId) {
        final Function function = new Function(
                FUNC_MINT,
                Arrays.<Type>asList(to, tokenId),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<TransactionReceipt> mintHeroAsset(Address _owner, Uint256 _tokenId) {
        final Function function = new Function(
                FUNC_MINTHEROASSET,
                Arrays.<Type>asList(_owner, _tokenId),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<TransactionReceipt> mintWithTokenURI(Address to, Uint256 tokenId,
                                                           Utf8String tokenURI) {
        final Function function = new Function(
                FUNC_MINTWITHTOKENURI,
                Arrays.<Type>asList(to, tokenId, tokenURI),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<TransactionReceipt> pause() {
        final Function function = new Function(
                FUNC_PAUSE,
                Arrays.<Type>asList(),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<TransactionReceipt> renounceMinter() {
        final Function function = new Function(
                FUNC_RENOUNCEMINTER,
                Arrays.<Type>asList(),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<TransactionReceipt> renouncePauser() {
        final Function function = new Function(
                FUNC_RENOUNCEPAUSER,
                Arrays.<Type>asList(),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<TransactionReceipt> safeTransferFrom(Address from, Address to,
                                                           Uint256 tokenId) {
        final Function function = new Function(
                FUNC_SAFETRANSFERFROM,
                Arrays.<Type>asList(from, to, tokenId),
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

    public RemoteCall<TransactionReceipt> setApprovalForAll(Address to, Bool approved) {
        final Function function = new Function(
                FUNC_SETAPPROVALFORALL,
                Arrays.<Type>asList(to, approved),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<TransactionReceipt> setSupplyLimit(Uint16 _heroType, Uint16 _supplyLimit) {
        final Function function = new Function(
                FUNC_SETSUPPLYLIMIT,
                Arrays.<Type>asList(_heroType, _supplyLimit),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<TransactionReceipt> setTokenURIPrefix(Utf8String _tokenURIPrefix) {
        final Function function = new Function(
                FUNC_SETTOKENURIPREFIX,
                Arrays.<Type>asList(_tokenURIPrefix),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<TransactionReceipt> transferFrom(Address from, Address to, Uint256 tokenId) {
        final Function function = new Function(
                FUNC_TRANSFERFROM,
                Arrays.<Type>asList(from, to, tokenId),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<TransactionReceipt> unpause() {
        final Function function = new Function(
                FUNC_UNPAUSE,
                Arrays.<Type>asList(),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public static RemoteCall<HeroAsset> deploy(Web3 web3, Credentials credentials,
                                               BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(HeroAsset.class, web3, credentials, gasPrice, gasLimit, BINARY, "");
    }

    public static RemoteCall<HeroAsset> deploy(Web3 web3, TransactionManager transactionManager,
                                               BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(HeroAsset.class, web3, transactionManager, gasPrice, gasLimit, BINARY, "");
    }

    public List<PausedEventResponse> getPausedEvents(TransactionReceipt transactionReceipt) {
        List<EventValuesWithLog> valueList = extractEventParametersWithLog(PAUSED_EVENT, transactionReceipt);
        ArrayList<PausedEventResponse> responses = new ArrayList<PausedEventResponse>(valueList.size());
        for (EventValuesWithLog eventValues : valueList) {
            PausedEventResponse typedResponse = new PausedEventResponse();
            typedResponse.log = eventValues.getLog();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Observable<PausedEventResponse> pausedEventObservable(HpbFilter filter) {
        return web3.hpbLogObservable(filter).map(new Func1<Log, PausedEventResponse>() {
            @Override
            public PausedEventResponse call(Log log) {
                EventValuesWithLog eventValues = extractEventParametersWithLog(PAUSED_EVENT, log);
                PausedEventResponse typedResponse = new PausedEventResponse();
                typedResponse.log = log;
                return typedResponse;
            }
        });
    }

    public Observable<PausedEventResponse> pausedEventObservable(DefaultBlockParameter startBlock,
                                                                 DefaultBlockParameter endBlock) {
        HpbFilter filter = new HpbFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(PAUSED_EVENT));
        return pausedEventObservable(filter);
    }

    public List<UnpausedEventResponse> getUnpausedEvents(TransactionReceipt transactionReceipt) {
        List<EventValuesWithLog> valueList = extractEventParametersWithLog(UNPAUSED_EVENT, transactionReceipt);
        ArrayList<UnpausedEventResponse> responses = new ArrayList<UnpausedEventResponse>(valueList.size());
        for (EventValuesWithLog eventValues : valueList) {
            UnpausedEventResponse typedResponse = new UnpausedEventResponse();
            typedResponse.log = eventValues.getLog();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Observable<UnpausedEventResponse> unpausedEventObservable(HpbFilter filter) {
        return web3.hpbLogObservable(filter).map(new Func1<Log, UnpausedEventResponse>() {
            @Override
            public UnpausedEventResponse call(Log log) {
                EventValuesWithLog eventValues = extractEventParametersWithLog(UNPAUSED_EVENT, log);
                UnpausedEventResponse typedResponse = new UnpausedEventResponse();
                typedResponse.log = log;
                return typedResponse;
            }
        });
    }

    public Observable<UnpausedEventResponse> unpausedEventObservable(DefaultBlockParameter startBlock,
                                                                     DefaultBlockParameter endBlock) {
        HpbFilter filter = new HpbFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(UNPAUSED_EVENT));
        return unpausedEventObservable(filter);
    }

    public List<PauserAddedEventResponse> getPauserAddedEvents(TransactionReceipt transactionReceipt) {
        List<EventValuesWithLog> valueList = extractEventParametersWithLog(PAUSERADDED_EVENT, transactionReceipt);
        ArrayList<PauserAddedEventResponse> responses = new ArrayList<PauserAddedEventResponse>(valueList.size());
        for (EventValuesWithLog eventValues : valueList) {
            PauserAddedEventResponse typedResponse = new PauserAddedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.account = (Address) eventValues.getIndexedValues().get(0);
            responses.add(typedResponse);
        }
        return responses;
    }

    public Observable<PauserAddedEventResponse> pauserAddedEventObservable(HpbFilter filter) {
        return web3.hpbLogObservable(filter).map(new Func1<Log, PauserAddedEventResponse>() {
            @Override
            public PauserAddedEventResponse call(Log log) {
                EventValuesWithLog eventValues = extractEventParametersWithLog(PAUSERADDED_EVENT, log);
                PauserAddedEventResponse typedResponse = new PauserAddedEventResponse();
                typedResponse.log = log;
                typedResponse.account = (Address) eventValues.getIndexedValues().get(0);
                return typedResponse;
            }
        });
    }

    public Observable<PauserAddedEventResponse> pauserAddedEventObservable(DefaultBlockParameter startBlock,
                                                                           DefaultBlockParameter endBlock) {
        HpbFilter filter = new HpbFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(PAUSERADDED_EVENT));
        return pauserAddedEventObservable(filter);
    }

    public List<PauserRemovedEventResponse> getPauserRemovedEvents(TransactionReceipt transactionReceipt) {
        List<EventValuesWithLog> valueList = extractEventParametersWithLog(PAUSERREMOVED_EVENT, transactionReceipt);
        ArrayList<PauserRemovedEventResponse> responses = new ArrayList<PauserRemovedEventResponse>(valueList.size());
        for (EventValuesWithLog eventValues : valueList) {
            PauserRemovedEventResponse typedResponse = new PauserRemovedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.account = (Address) eventValues.getIndexedValues().get(0);
            responses.add(typedResponse);
        }
        return responses;
    }

    public Observable<PauserRemovedEventResponse> pauserRemovedEventObservable(HpbFilter filter) {
        return web3.hpbLogObservable(filter).map(new Func1<Log, PauserRemovedEventResponse>() {
            @Override
            public PauserRemovedEventResponse call(Log log) {
                EventValuesWithLog eventValues = extractEventParametersWithLog(PAUSERREMOVED_EVENT, log);
                PauserRemovedEventResponse typedResponse = new PauserRemovedEventResponse();
                typedResponse.log = log;
                typedResponse.account = (Address) eventValues.getIndexedValues().get(0);
                return typedResponse;
            }
        });
    }

    public Observable<PauserRemovedEventResponse> pauserRemovedEventObservable(DefaultBlockParameter startBlock,
                                                                               DefaultBlockParameter endBlock) {
        HpbFilter filter = new HpbFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(PAUSERREMOVED_EVENT));
        return pauserRemovedEventObservable(filter);
    }

    public List<MintingFinishedEventResponse> getMintingFinishedEvents(TransactionReceipt transactionReceipt) {
        List<EventValuesWithLog> valueList = extractEventParametersWithLog(MINTINGFINISHED_EVENT, transactionReceipt);
        ArrayList<MintingFinishedEventResponse> responses = new ArrayList<MintingFinishedEventResponse>(valueList.size());
        for (EventValuesWithLog eventValues : valueList) {
            MintingFinishedEventResponse typedResponse = new MintingFinishedEventResponse();
            typedResponse.log = eventValues.getLog();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Observable<MintingFinishedEventResponse> mintingFinishedEventObservable(HpbFilter filter) {
        return web3.hpbLogObservable(filter).map(new Func1<Log, MintingFinishedEventResponse>() {
            @Override
            public MintingFinishedEventResponse call(Log log) {
                EventValuesWithLog eventValues = extractEventParametersWithLog(MINTINGFINISHED_EVENT, log);
                MintingFinishedEventResponse typedResponse = new MintingFinishedEventResponse();
                typedResponse.log = log;
                return typedResponse;
            }
        });
    }

    public Observable<MintingFinishedEventResponse> mintingFinishedEventObservable(DefaultBlockParameter startBlock,
                                                                                   DefaultBlockParameter endBlock) {
        HpbFilter filter = new HpbFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(MINTINGFINISHED_EVENT));
        return mintingFinishedEventObservable(filter);
    }

    public List<MinterAddedEventResponse> getMinterAddedEvents(TransactionReceipt transactionReceipt) {
        List<EventValuesWithLog> valueList = extractEventParametersWithLog(MINTERADDED_EVENT, transactionReceipt);
        ArrayList<MinterAddedEventResponse> responses = new ArrayList<MinterAddedEventResponse>(valueList.size());
        for (EventValuesWithLog eventValues : valueList) {
            MinterAddedEventResponse typedResponse = new MinterAddedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.account = (Address) eventValues.getIndexedValues().get(0);
            responses.add(typedResponse);
        }
        return responses;
    }

    public Observable<MinterAddedEventResponse> minterAddedEventObservable(HpbFilter filter) {
        return web3.hpbLogObservable(filter).map(new Func1<Log, MinterAddedEventResponse>() {
            @Override
            public MinterAddedEventResponse call(Log log) {
                EventValuesWithLog eventValues = extractEventParametersWithLog(MINTERADDED_EVENT, log);
                MinterAddedEventResponse typedResponse = new MinterAddedEventResponse();
                typedResponse.log = log;
                typedResponse.account = (Address) eventValues.getIndexedValues().get(0);
                return typedResponse;
            }
        });
    }

    public Observable<MinterAddedEventResponse> minterAddedEventObservable(DefaultBlockParameter startBlock,
                                                                           DefaultBlockParameter endBlock) {
        HpbFilter filter = new HpbFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(MINTERADDED_EVENT));
        return minterAddedEventObservable(filter);
    }

    public List<MinterRemovedEventResponse> getMinterRemovedEvents(TransactionReceipt transactionReceipt) {
        List<EventValuesWithLog> valueList = extractEventParametersWithLog(MINTERREMOVED_EVENT, transactionReceipt);
        ArrayList<MinterRemovedEventResponse> responses = new ArrayList<MinterRemovedEventResponse>(valueList.size());
        for (EventValuesWithLog eventValues : valueList) {
            MinterRemovedEventResponse typedResponse = new MinterRemovedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.account = (Address) eventValues.getIndexedValues().get(0);
            responses.add(typedResponse);
        }
        return responses;
    }

    public Observable<MinterRemovedEventResponse> minterRemovedEventObservable(HpbFilter filter) {
        return web3.hpbLogObservable(filter).map(new Func1<Log, MinterRemovedEventResponse>() {
            @Override
            public MinterRemovedEventResponse call(Log log) {
                EventValuesWithLog eventValues = extractEventParametersWithLog(MINTERREMOVED_EVENT, log);
                MinterRemovedEventResponse typedResponse = new MinterRemovedEventResponse();
                typedResponse.log = log;
                typedResponse.account = (Address) eventValues.getIndexedValues().get(0);
                return typedResponse;
            }
        });
    }

    public Observable<MinterRemovedEventResponse> minterRemovedEventObservable(DefaultBlockParameter startBlock,
                                                                               DefaultBlockParameter endBlock) {
        HpbFilter filter = new HpbFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(MINTERREMOVED_EVENT));
        return minterRemovedEventObservable(filter);
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

    public RemoteCall<Uint256> balanceOf(Address owner) {
        final Function function = new Function(FUNC_BALANCEOF,
                Arrays.<Type>asList(owner),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {
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

    public RemoteCall<Uint16> getSupplyLimit(Uint16 _heroType) {
        final Function function = new Function(FUNC_GETSUPPLYLIMIT,
                Arrays.<Type>asList(_heroType),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint16>() {
                }));
        return executeRemoteCallSingleValueReturn(function);
    }

    public RemoteCall<Uint16> HERO_TYPE_OFFSET() {
        final Function function = new Function(FUNC_HERO_TYPE_OFFSET,
                Arrays.<Type>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint16>() {
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

    public RemoteCall<Bool> isMinter(Address account) {
        final Function function = new Function(FUNC_ISMINTER,
                Arrays.<Type>asList(account),
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {
                }));
        return executeRemoteCallSingleValueReturn(function);
    }

    public RemoteCall<Bool> isPauser(Address account) {
        final Function function = new Function(FUNC_ISPAUSER,
                Arrays.<Type>asList(account),
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {
                }));
        return executeRemoteCallSingleValueReturn(function);
    }

    public RemoteCall<Bool> mintingFinished() {
        final Function function = new Function(FUNC_MINTINGFINISHED,
                Arrays.<Type>asList(),
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

    public RemoteCall<Address> ownerOf(Uint256 tokenId) {
        final Function function = new Function(FUNC_OWNEROF,
                Arrays.<Type>asList(tokenId),
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {
                }));
        return executeRemoteCallSingleValueReturn(function);
    }

    public RemoteCall<Bool> paused() {
        final Function function = new Function(FUNC_PAUSED,
                Arrays.<Type>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {
                }));
        return executeRemoteCallSingleValueReturn(function);
    }

    public RemoteCall<Bool> supportsInterface(Bytes4 interfaceId) {
        final Function function = new Function(FUNC_SUPPORTSINTERFACE,
                Arrays.<Type>asList(interfaceId),
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {
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

    public RemoteCall<Uint256> tokenByIndex(Uint256 index) {
        final Function function = new Function(FUNC_TOKENBYINDEX,
                Arrays.<Type>asList(index),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {
                }));
        return executeRemoteCallSingleValueReturn(function);
    }

    public RemoteCall<Uint256> tokenOfOwnerByIndex(Address owner, Uint256 index) {
        final Function function = new Function(FUNC_TOKENOFOWNERBYINDEX,
                Arrays.<Type>asList(owner, index),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {
                }));
        return executeRemoteCallSingleValueReturn(function);
    }

    public RemoteCall<Utf8String> tokenURI(Uint256 tokenId) {
        final Function function = new Function(FUNC_TOKENURI,
                Arrays.<Type>asList(tokenId),
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {
                }));
        return executeRemoteCallSingleValueReturn(function);
    }

    public RemoteCall<Utf8String> tokenURIPrefix() {
        final Function function = new Function(FUNC_TOKENURIPREFIX,
                Arrays.<Type>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {
                }));
        return executeRemoteCallSingleValueReturn(function);
    }

    public RemoteCall<Uint256> totalSupply() {
        final Function function = new Function(FUNC_TOTALSUPPLY,
                Arrays.<Type>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {
                }));
        return executeRemoteCallSingleValueReturn(function);
    }

    public static HeroAsset load(String contractAddress, Web3 web3, Credentials credentials,
                                 BigInteger gasPrice, BigInteger gasLimit) {
        return new HeroAsset(contractAddress, web3, credentials, gasPrice, gasLimit);
    }

    public static HeroAsset load(String contractAddress, Web3 web3,
                                 TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new HeroAsset(contractAddress, web3, transactionManager, gasPrice, gasLimit);
    }

    public static class PausedEventResponse {
        public Log log;
    }

    public static class UnpausedEventResponse {
        public Log log;
    }

    public static class PauserAddedEventResponse {
        public Log log;

        public Address account;
    }

    public static class PauserRemovedEventResponse {
        public Log log;

        public Address account;
    }

    public static class MintingFinishedEventResponse {
        public Log log;
    }

    public static class MinterAddedEventResponse {
        public Log log;

        public Address account;
    }

    public static class MinterRemovedEventResponse {
        public Log log;

        public Address account;
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
