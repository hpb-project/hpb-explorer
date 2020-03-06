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

package com.hpb.bc.propeller.solidity;

/**
 * This code is released under Apache 2 license
 */
public enum SolidityCompilerOptions {
    AST("ast"),
    BIN("bin"),
    INTERFACE("interface"),
    ABI("abi"),
    METADATA("metadata");

    private final String name;

    SolidityCompilerOptions(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
