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


import com.hpb.bc.exception.ApiException;
import com.hpb.bc.solidity.values.HvmVersion;
import com.hpb.bc.solidity.values.SoliditySourceFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;

import org.zeroturnaround.exec.ProcessExecutor;
import org.zeroturnaround.exec.ProcessResult;


import static com.hpb.bc.solidity.HpbSolidityCompilerOptions.AST;
import static com.hpb.bc.solidity.HpbSolidityCompilerOptions.INTERFACE;
import static com.hpb.bc.solidity.HpbSolidityCompilerOptions.METADATA;
import static com.hpb.bc.solidity.HpbSolidityCompilerOptions.ABI;
import static com.hpb.bc.solidity.HpbSolidityCompilerOptions.BIN;


/**
 * Created by davidroon on 27.03.17.
 * This code is released under Apache 2 license
 */
public class HpbSolidityCompiler {

    private static HpbSolidityCompiler compiler;

    public static HpbSolidityCompiler getInstance() {
        if (compiler == null) {
            compiler = new HpbSolidityCompiler();
        }
        return compiler;
    }

    public CompilationResult compileSrc(SoliditySourceFile source, Optional<HvmVersion> evmVersion) {
        List<String> commandParts = prepareCommandOptions(evmVersion, BIN, ABI, AST, INTERFACE, METADATA);
        commandParts.add(source.getSource().getAbsolutePath());

        try {
            return CompilationResult.parse(runProcess(commandParts));
        } catch (IOException e) {
            throw new ApiException("error while waiting for the process to finish", e);
        }
    }

    public CompilationResult compileSrc(SoliditySourceFile source) {
        return compileSrc(source, Optional.empty());
    }

    public SolidityVersion getVersion() {
        List<String> commandParts = new ArrayList<>(Arrays.asList("solc", "--version"));
        return new SolidityVersion(runProcess(commandParts));
    }

    private String runProcess(final List<String> commandParts) {
        try {
            ProcessResult result = new ProcessExecutor()
                    .command(commandParts)
                    .readOutput(true)
                    .execute();
            if (result.getExitValue() != 0) {
                throw new ApiException(result.outputString());
            }
            return result.outputString();
        } catch (InterruptedException | IOException | TimeoutException e) {
            throw new ApiException("error while running process", e);
        }
    }

    private List<String> prepareCommandOptions(Optional<HvmVersion> evmVersion, HpbSolidityCompilerOptions... options) {
        List<String> commandParts = new ArrayList<>();
        commandParts.add("solc");
        if (evmVersion.isPresent()) {
            commandParts.add("--evm-version=" + evmVersion.get().version);
        }
        commandParts.add("--optimize");
        commandParts.add("--combined-json");
        commandParts.add(Arrays.stream(options)
                .map(HpbSolidityCompilerOptions::getName)
                .collect(Collectors.joining(",")));

        return commandParts;
    }

    private List<String> prepareCommandOptions(HpbSolidityCompilerOptions... options) {
        return prepareCommandOptions(Optional.empty(), options);
    }
}
