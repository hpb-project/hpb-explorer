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

package com.hpb.bc.service.impl;

import com.hpb.bc.service.MailService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;


@Service
public class MailServiceImpl implements MailService {
    private static final Logger LOG = LogManager.getLogger(MailServiceImpl.class);

    @Autowired
    public JavaMailSender emailSender;

    @Override
    public void sendSimpleMessage(String from, String to, String subject, String messageBody) {
        LOG.debug("send email ...");
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(from);
            message.setTo(to);
            message.setSubject(subject);
            message.setText(messageBody);
            emailSender.send(message);
        } catch (MailException e) {
            LOG.error("Exception occurred sending email message {} ", e);
        }
    }

    @Override
    public void sendSimpleMessageUsingTemplate(String to,
                                               String subject,
                                               SimpleMailMessage template,
                                               String... templateArgs) {
        String messageBody = String.format(template.getText(), templateArgs);
        sendSimpleMessage(template.getFrom(), to, subject, messageBody);
    }

}
