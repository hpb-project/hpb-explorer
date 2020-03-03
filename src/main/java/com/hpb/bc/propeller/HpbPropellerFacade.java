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

import com.hpb.bc.configure.Web3Properties;
import com.hpb.bc.propeller.model.HpbStatediffbyblockandTx;
import io.hpb.web3.protocol.Web3Service;
import io.hpb.web3.protocol.http.HttpService;
import io.hpb.web3.protocol.ipc.UnixIpcService;
import io.hpb.web3.protocol.ipc.WindowsIpcService;
import okhttp3.OkHttpClient;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Component
public class HpbPropellerFacade {

    @Autowired
    Web3Properties web3Properties;

    public HpbPropeller hpbPropeller() {
        String hpbNodeIp = web3Properties.getErcClientAddress();
        if (StringUtils.isBlank(web3Properties.getErcClientAddress())) {
            hpbNodeIp = "http://106.15.89.82:33004";

        }
        Web3Service web3Service = buildService(hpbNodeIp);
        HpbPropeller p = new HpbPropeller(web3Service);
        return p;
    }

    private Web3Service buildService(String clientAddress) {
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

    private OkHttpClient createOkHttpClient() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        configureTimeouts(builder);
        return builder.build();
    }

    private void configureTimeouts(OkHttpClient.Builder builder) {
        builder.connectTimeout(5, TimeUnit.SECONDS);
        builder.readTimeout(5, TimeUnit.SECONDS);
        builder.writeTimeout(5, TimeUnit.SECONDS);
    }


    public HpbStatediffbyblockandTx getHpbStatediffbyblockandTxByBlockNumberOrTxHash(BigInteger blockNumber, String txHash) {
        try {
            HpbStatediffbyblockandTx hpbStatediffbyblockandTx = hpbPropeller().hpbGetStatediffbyblockandTxByBlockNumberOrTxHash(blockNumber, txHash).sendAsync().get(2, TimeUnit.MINUTES);
            return hpbStatediffbyblockandTx;
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
        return null;
    }


    public HpbStatediffbyblockandTx getHpbStatediffbyblockandTxByBlockHashOrTxHash(String blockHash, String txHash) {
        try {
            HpbStatediffbyblockandTx hpbStatediffbyblockandTx = hpbPropeller().hpbGetStatediffbyblockandTxByBlockHashOrTxHash(blockHash, txHash).sendAsync().get(2, TimeUnit.MINUTES);
            return hpbStatediffbyblockandTx;
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
        return null;
    }

}
