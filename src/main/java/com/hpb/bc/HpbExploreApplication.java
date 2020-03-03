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

package com.hpb.bc;

import com.hpb.bc.common.SpringBootContext;
import com.hpb.bc.listener.HpbApplicationListener;
import org.springframework.boot.Banner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication// same as @Configuration @EnableAutoConfiguration @ComponentScan
@Configuration
@EnableScheduling
@EnableAutoConfiguration(exclude = {SecurityAutoConfiguration.class})
@ComponentScan(basePackages = "com.hpb.bc")
public class HpbExploreApplication {

    public static void main(String[] args) {
        System.setProperty("spring.devtools.restart.enabled", "false");
        SpringApplication springApplication = new SpringApplication(HpbExploreApplication.class);
        springApplication.setAddCommandLineProperties(false);
        springApplication.addListeners(new HpbApplicationListener());
        springApplication.setBannerMode(Banner.Mode.OFF);
        ApplicationContext aplicationContext = springApplication.run(args);
        SpringBootContext.setAplicationContext(aplicationContext);
    }

    @Bean
    public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
        return args -> {
            if (SpringBootContext.getAplicationContext() == null) {
                SpringBootContext.setAplicationContext(ctx);
            }
        };
    }
}
