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

package com.hpb.bc.solidity.abi;


/**
 * Created by davidroon on 28.03.17.
 * This code is released under Apache 2 license
 */
public class AbiParam {
    private final Boolean indexed;
    private final String name;
    private final String type;

    public AbiParam() {
        this(null, null, null);
    }

    public AbiParam(Boolean indexed, String name, String type) {
        this.indexed = indexed;
        this.name = name;
        this.type = type;
    }

    public boolean isIndexed() {
        return Boolean.TRUE.equals(indexed);
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public boolean isArray() {
        return type.contains("[");
    }

    public boolean isDynamic() {
        return type.contains("[]") || type.equals("string") || type.equals("bytes");
    }

    @Override
    public String toString() {
        return "AbiParam{" +
                "indexed=" + indexed +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                '}';
    }

    public Integer getArraySize() {
        if (!isArray() || isDynamic()) {
            return 0;
        }
        int startIndex = type.indexOf("[");
        int endIndex = type.indexOf("]");
        return Integer.parseInt(type.substring(startIndex + 1, endIndex));
    }
}
