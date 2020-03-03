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

package com.hpb.bc.common;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;

public class SpringBootContext {
    private static ApplicationContext aplicationContext;

    public static void setAplicationContext(ApplicationContext aplicationContext) {
        SpringBootContext.aplicationContext = aplicationContext;
    }

    public static ApplicationContext getAplicationContext() {
        return aplicationContext;
    }

    public static Object getBean(String name) throws BeansException {
        return aplicationContext.getBean(name);
    }

    public static <T> T getBean(String name, Class<T> requiredType) throws BeansException {
        return aplicationContext.getBean(name, requiredType);
    }

    public static <T> T getBean(Class<T> requiredType) throws BeansException {
        return aplicationContext.getBean(requiredType);
    }

    public static Object getBean(String name, Object... args) throws BeansException {
        return aplicationContext.getBean(name, args);
    }

    public static <T> T getBean(Class<T> requiredType, Object... args) throws BeansException {
        return aplicationContext.getBean(requiredType, args);
    }

    ;

    public static Class<?> getType(String name) throws NoSuchBeanDefinitionException {
        return aplicationContext.getType(name);
    }

    public static String[] getAliases(String name) {
        return aplicationContext.getAliases(name);
    }
}
