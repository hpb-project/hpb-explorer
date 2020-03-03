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
