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

package com.hpb.bc.configure;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
@EnableWebMvc
class ApiKeyMailConfig {
    @Autowired
    private ApiKeyMailProperties apiKeyMailProperties;

    @Bean
    public SimpleMailMessage simpleMailMessage() {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setText("Dear %s %s,%n%nThank you for registering for the HPB API." +
                "%n%n" +
                "These are your HPB API keys: %n===========================%n" +
                "API key: \t\t%s %nPrivate key: \t%s %n===========================%n" +
                "Best regards," +
                "%n" +
                "The HPB API Team");
        message.setFrom(apiKeyMailProperties.getSentFrom());
        return message;
    }
}