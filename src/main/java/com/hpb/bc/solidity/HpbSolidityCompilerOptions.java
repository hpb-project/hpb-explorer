package com.hpb.bc.solidity;

/**
 * Created by davidroon on 27.03.17.
 * This code is released under Apache 2 license
 */
public enum HpbSolidityCompilerOptions {
    AST("ast"),
    BIN("bin"),
    INTERFACE("interface"),
    ABI("abi"),
    METADATA("metadata");

    private final String name;

    HpbSolidityCompilerOptions(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
