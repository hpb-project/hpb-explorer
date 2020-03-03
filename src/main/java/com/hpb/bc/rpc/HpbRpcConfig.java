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

import com.hpb.bc.solidity.values.GasPrice;

import java.util.concurrent.TimeUnit;

/**
 * Created by davidroon on 25.04.17.
 * This code is released under Apache 2 license
 */
public final class HpbRpcConfig extends HpbConfig {
    private final boolean pollBlocks;
    //    private final long pollingFrequence;
    private final AuthenticationType authType;
    private final String userName;
    private final String password;

    private HpbRpcConfig(boolean pollBlocks, long pollingFrequence, long blockWait, GasPrice gasPrice,
                         AuthenticationType authType, String userName, String password) {
        super(blockWait, gasPrice);
        this.pollBlocks = pollBlocks;
//        this.pollingFrequence = pollingFrequence;
        this.authType = authType;
        this.userName = userName;
        this.password = password;
    }

    public static Builder builder() {
        return new Builder();
    }

//    public boolean isPollBlocks() {
//        return pollBlocks;
//    }

   /* public long getPollingFrequence() {
        return pollingFrequence;
    }*/

    public AuthenticationType getAuthType() {
        return authType;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public static class Builder extends HpbConfig.Builder {
        private boolean pollBlocks;
        private long pollingFrequence = 100;
        private AuthenticationType authType = AuthenticationType.NoAuth;
        private String password;
        private String userName;

        public Builder pollBlocks(boolean value) {
            this.pollBlocks = value;
            return this;
        }

        public Builder pollingFrequence(long frequence) {
            this.pollingFrequence = frequence;
            return this;
        }

        public Builder pollingFrequence(long amount, TimeUnit unit) {
            this.pollingFrequence = unit.toMillis(amount);
            return this;
        }

        public Builder basicAuth(String user, String password) {
            this.authType = AuthenticationType.BasicAuth;
            this.userName = user;
            this.password = password;
            return this;
        }

        @Override
        public HpbRpcConfig build() {
            return new HpbRpcConfig(pollBlocks, pollingFrequence, blockWaitLimit, gasPrice, authType, userName, password);
        }
    }
}

