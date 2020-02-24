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
