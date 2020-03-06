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

package com.hpb.bc.rpc;

import com.hpb.bc.event.HpbEventHandler;
import com.hpb.bc.solidity.values.ChainId;
import com.hpb.bc.solidity.values.config.InfuraKey;
import io.hpb.web3.protocol.Web3;
import io.hpb.web3.protocol.Web3Service;
import okhttp3.Credentials;
import okhttp3.OkHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.hpb.web3.protocol.http.HttpService;
import io.hpb.web3.protocol.websocket.WebSocketService;

import java.net.ConnectException;



public final class RpcHpbFacadeProvider {

    public static final ChainId MAIN_CHAIN_ID = ChainId.id(269);

    private RpcHpbFacadeProvider() {
    }

    public static HpbFacade forRemoteNode(final String url, final ChainId chainId) {
        return forRemoteNode(url, chainId, HpbRpcConfig.builder().build());
    }

    public static HpbFacade forRemoteNode(final String url, final ChainId chainId, HpbRpcConfig config) {
        return forRemoteNode(createHttpService(url, config), chainId, config);
    }

    private static HttpService createHttpService(final String url, HpbRpcConfig config) {
        switch (config.getAuthType()) {
            case BasicAuth:
                return createHttpServiceForBasicAuth(url, config);
            default:
                return new HttpService(url);
        }
    }

    private static HttpService createHttpServiceForBasicAuth(final String url, HpbRpcConfig config) {
        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();
        clientBuilder.authenticator((route, response) -> {
            String credential = Credentials.basic(config.getUserName(), config.getPassword());
            return response.request().newBuilder().header("Authorization", credential).build();
        });

        return new HttpService(url, clientBuilder.build(), false);
    }

    private static HpbFacade forRemoteNode(Web3Service web3jService, final ChainId chainId, HpbRpcConfig config) {
        Web3 w3j = Web3.build(web3jService);
        Web3JFacade web3jFacade = new Web3JFacade(w3j);
        HpbRpc ethRpc = new HpbRpc(web3jFacade, chainId, config);
        HpbEventHandler eventHandler = new HpbEventHandler();
        eventHandler.onReady();
        return CoreHpbFacadeProvider.create(ethRpc, eventHandler, config);
    }
}
