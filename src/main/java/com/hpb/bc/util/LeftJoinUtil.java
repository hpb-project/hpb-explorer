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

import com.alibaba.fastjson.JSON;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author lij
 */
public class LeftJoinUtil {

    private static final Logger logger = LoggerFactory.getLogger(LeftJoinUtil.class);

    /**
     * @param leftList        左表数据
     * @param rightList       右表数据
     * @param leftIdGetter    左表获取id Getter
     * @param rightIdGetter   右表获取id Getter
     * @param rightNameGetter 右表name  Getter
     * @param leftNameSetter  左表name  Setter
     * @param <A>             左表实体类类型
     * @param <B>             右表实体类类型
     * @param <C>             左表关联id类型
     * @param <E>             右表name类型
     */
    public static <A, B, C, E> void lj(List<A> leftList, List<B> rightList, Function<A, C> leftIdGetter, Function<B, C> rightIdGetter, Function<B, E> rightNameGetter, BiConsumer<A, E> leftNameSetter) {
        ljByConverters(leftList, rightList, leftIdGetter, rightIdGetter, rightNameGetter, leftNameSetter, Function.identity(), Function.identity());
    }

    /**
     * @param leftList        左表数据
     * @param rightList       右表数据
     * @param leftIdGetter    左表获取id Getter
     * @param rightIdGetter   右表获取id Getter
     * @param rightNameGetter 右表name  Getter
     * @param leftNameSetter  左表name  Setter
     * @param idConverter     id对比转换器
     * @param <A>             左表实体类类型
     * @param <B>             右表实体类类型
     * @param <C>             左表关联id类型
     * @param <D>             右表关联id类型
     * @param <E>             右表name类型
     */
    public static <A, B, C, D, E> void ljByIdConverter(List<A> leftList, List<B> rightList, Function<A, C> leftIdGetter, Function<B, D> rightIdGetter, Function<B, E> rightNameGetter, BiConsumer<A, E> leftNameSetter, Function<C, D> idConverter) {
        ljByConverters(leftList, rightList, leftIdGetter, rightIdGetter, rightNameGetter, leftNameSetter, idConverter, Function.identity());
    }

    /**
     * @param leftList        左表数据
     * @param rightList       右表数据
     * @param leftIdGetter    左表获取id Getter
     * @param rightIdGetter   右表获取id Getter
     * @param rightNameGetter 右表name  Getter
     * @param leftNameSetter  左表name  Setter
     * @param nameConverter   名称设置转换器
     * @param <A>             左表实体类类型
     * @param <B>             右表实体类类型
     * @param <C>             左表关联id类型
     * @param <E>             右表name类型
     * @param <F>             左表name类型
     */
    public static <A, B, C, E, F> void ljByNameConverter(List<A> leftList, List<B> rightList, Function<A, C> leftIdGetter, Function<B, C> rightIdGetter, Function<B, E> rightNameGetter, BiConsumer<A, F> leftNameSetter, Function<E, F> nameConverter) {
        ljByConverters(leftList, rightList, leftIdGetter, rightIdGetter, rightNameGetter, leftNameSetter, Function.identity(), nameConverter);
    }

    /**
     * @param leftList        左表数据
     * @param rightList       右表数据
     * @param leftIdGetter    左表获取id Getter
     * @param rightIdGetter   右表获取id Getter
     * @param rightNameGetter 右表name  Getter
     * @param leftNameSetter  左表name  Setter
     * @param idConverter     id对比转换器
     * @param nameConverter   名称设置转换器
     * @param <A>             左表实体类类型
     * @param <B>             右表实体类类型
     * @param <C>             左表关联id类型
     * @param <D>             右表关联id类型
     * @param <E>             右表name类型
     * @param <F>             左表name类型
     */
    public static <A, B, C, D, E, F> void ljByConverters(List<A> leftList, List<B> rightList, Function<A, C> leftIdGetter, Function<B, D> rightIdGetter, Function<B, E> rightNameGetter, BiConsumer<A, F> leftNameSetter, Function<C, D> idConverter, Function<E, F> nameConverter) {
        List<C> leftIdList = leftList.parallelStream().map(leftIdGetter).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(leftIdList) && CollectionUtils.isNotEmpty(rightList)) {
            Map<D, E> rightMap = rightList.parallelStream().filter(Objects::nonNull).collect(Collectors.toMap(rightIdGetter, rightNameGetter, (l, r) -> {
                logger.error("数据里面有重复key数据,重复的数据left:{},right:{}", JSON.toJSONString(l), JSON.toJSONString(r));
                return r;
            }));
            leftList.parallelStream().forEach(e -> {
                C leftId = leftIdGetter.apply(e);
                //leftId 的对象类型转换为D
                D dTypeLeftId = idConverter.apply(leftId);

                E rightName = rightMap.get(dTypeLeftId);

                if (rightName != null) {
                    F fTypeRightName = nameConverter.apply(rightName);
                    leftNameSetter.accept(e, fTypeRightName);
                }
            });
        }

    }

}