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
 * @author will
 */
public interface ApiConstant {

    String MODULE = "module";
    String ACTION = "action";
    String MISSINGPARAMETER = "missing parameter";

    String MODULE_ACCOUNT = "account";
    String MODULE_STATS = "stats";

    String ACTION_BALANCE = "balance";
    String ACTION_TXLIST = "txlist";
    String ACTION_TXLIST_INTERNAL = "txlistinternal";
    String ACTION_TOKENTX = "tokentx";
    String ACTION_BALANCEMULTI = "balancemulti";
    String ACTION_GETMINEDBLOCK = "getminedblocks";
    String ACTION_HPBPRICE = "hpbprice";

    String ACCOUNT = "account";
    String BALANCE = "balance";

    String PARAM_START_BLOCK = "startblock";
    String PARAM_END_BLOCK = "endblock";
    String PARAM_ADDRESS = "address";
    String PARAM_TXHASH = "txhash";
    String PARAM_CONTRACTADDRESS = "contractaddress";
    String PARAM_PAGE = "page";
    String PARAM_PAGESIZE = "offset";

}
