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

package com.hpb.bc.util;

import com.hpb.bc.exception.ApiException;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

/**
 * Created by davidroon on 21.04.17.
 * This code is released under Apache 2 license
 */
public final class CastUtil<T> {
    private final Map<Class<?>, Supplier<T>> classes = new HashMap<>();
    private final Map<String, Supplier<T>> classNames = new HashMap<>();
    private String errorMessage = "casting error ...";

    private CastUtil() {
    }

    public static <T> CastUtil<T> matcher() {
        return new CastUtil<>();
    }


    public CastUtil<T> typeNameEquals(String name, Supplier<T> supplier) {
        classNames.put(name, supplier);
        return this;
    }

    public CastUtil<T> typeEquals(Class<?> cls, Supplier<T> supplier) {
        classes.put(cls, supplier);
        return this;
    }

    public CastUtil<T> orElseThrowWithErrorMessage(String msg) {
        this.errorMessage = msg;
        return this;
    }

    public T matches(Class<? extends T> resultCls) {
        return Optional.ofNullable(classes.get(resultCls))
                .orElseGet(() -> Optional.ofNullable(classNames.get(resultCls.getTypeName()))
                        .orElseThrow(() -> new ApiException(errorMessage))).get();
    }
}
