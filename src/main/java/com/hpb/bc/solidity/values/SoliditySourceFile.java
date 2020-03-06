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

package com.hpb.bc.solidity.values;

import java.io.File;

/**
 * Created by davidroon on 21.02.17.
 * This code is released under Apache 2 license
 */
public class SoliditySourceFile implements SoliditySource<File> {

    private final File source;

    public SoliditySourceFile(File source) {
        this.source = source;
    }

    public static SoliditySourceFile from(File file) {
        return new SoliditySourceFile(file);

    }

    @Override
    public File getSource() {
        return source;
    }

    @Override
    public String toString() {
        return "source:" + source;
    }
}
