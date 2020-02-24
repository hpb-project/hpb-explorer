package com.hpb.bc.propeller.solidity;
import com.hpb.bc.exception.ApiException;
import com.hpb.bc.propeller.model.EvmVersion;
import com.hpb.bc.solidity.CompilationResult;
import com.hpb.bc.solidity.SolidityVersion;
import com.hpb.bc.solidity.values.SoliditySourceFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zeroturnaround.exec.ProcessExecutor;
import org.zeroturnaround.exec.ProcessResult;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;

import static com.hpb.bc.propeller.solidity.SolidityCompilerOptions.*;


/**
 * Created by davidroon on 27.03.17.
 * This code is released under Apache 2 license
 */
public class HpbPropellerSolidityCompiler {

    public Logger log = LoggerFactory.getLogger("contractCompileAppenderLogger");

    private static HpbPropellerSolidityCompiler compiler;

    public static HpbPropellerSolidityCompiler getInstance() {
        if (compiler == null) {
            compiler = new HpbPropellerSolidityCompiler();
        }
        return compiler;
    }

    public CompilationResult compileSrc(SoliditySourceFile source, Optional<EvmVersion> evmVersion) {
       // List<String> commandParts = prepareCommandOptions(evmVersion, BIN, ABI, AST, INTERFACE, METADATA);
        List<String> commandParts = prepareCommandOptions(evmVersion, BIN, ABI);
        commandParts.add(source.getSource().getAbsolutePath());
        log.info("compileSrc  commandParts.size ==="+commandParts.size());
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
            log.info("runProcess  start ....");
            ProcessResult result = new ProcessExecutor()
                    .command(commandParts)
                    .readOutput(true)
                    .execute();
            log.info("runProcess result ==="+result);
            if(result != null){
                log.info("runProcess result getOutput ==="+result.getOutput());
                log.info("runProcess result getExitValue ==="+result.getExitValue());
            }
            if (result.getExitValue() != 0) {
                throw new ApiException(result.outputString());
            }
            return result.outputString();
        } catch (InterruptedException | IOException | TimeoutException e) {
            log.info("runProcess  error e  ======"+e);
            throw new ApiException("error while running process", e);
        }
    }

    private List<String> prepareCommandOptions(Optional<EvmVersion> evmVersion, SolidityCompilerOptions... options) {
        List<String> commandParts = new ArrayList<>();
        commandParts.add("solc");
        if (evmVersion.isPresent()) {
            commandParts.add("--evm-version=" + evmVersion.get().version);
        }
        commandParts.add("--optimize");
        commandParts.add("--combined-json");
        commandParts.add(Arrays.stream(options)
                .map(SolidityCompilerOptions::getName)
                .collect(Collectors.joining(",")));

        return commandParts;
    }

    private List<String> prepareCommandOptions(SolidityCompilerOptions... options) {
        return prepareCommandOptions(Optional.empty(), options);
    }
}
