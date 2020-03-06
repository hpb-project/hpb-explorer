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

package com.hpb.bc.solidity;

/**
 * Container for the compile result.
 */
public class CompilerResult {
    public String errors;
    public String output;
    private boolean success = false;

    public CompilerResult(String errors, String output, boolean success) {
        this.errors = errors;
        // https://ethereum.stackexchange.com/questions/11912/unable-to-define-greetercontract-in-the-greeter-tutorial-breaking-change-in-sol
        this.output = output.replaceAll("<stdin>:", "");
        ;
        this.success = success;
    }

    public String getErrors() {
        return errors;
    }

    public void setErrors(String errors) {
        this.errors = errors;
    }

    public String getOutput() {
        return output;
    }

    public void setOutput(String output) {
        this.output = output;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public boolean isFailed() {
        return !success;
    }
}
