package com.hpb.bc.constant;

/**
 * @author ThinkPad
 * 这里全部声明常量
 */
public interface ContractConstant {
    String EVENT_HASH = "eventHash";
    String CONTRACT_NAME = "contractName";
    String CONTRACT_ADDR = "contractAddr";
    String CONTRACT_COMPILER_TYPE = "compilerType";
    String CONTRACT_COMPILER_VERSION = "compilerVersion";

    String METHOD_ID = "methodId";
    String TX_HASH = "txHash";
    String CONTRACT_SRC = "contractSrc";
    String CONTRACT_ABI = "contractAbi";
    String CONTRACT_BIN = "contractBin";
    String OPTIMIZE_FLAG = "optimizeFlag";
    String CONTRACT_LIBRARY_NAME_ADDRESS_LIST = "contractLibraryNameAddressList";

    String SOLC_CMD_DEFAULT = "docker rm -rf solc;docker run -it --privileged=true --net=host -v /home/hpbroot/ethereum_go/contract:/contract --name solc ethereum/solc:stable";
    String SOLC_STABLE = "stable";
}
