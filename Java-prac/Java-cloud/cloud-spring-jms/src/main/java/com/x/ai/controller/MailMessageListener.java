package com.x.ai.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.x.ai.entity.MailMessage;
import com.x.ai.service.MailService;
import jakarta.jms.Message;
import jakarta.jms.TextMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
public class MailMessageListener {
    final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    MailService mailService;

    @JmsListener(destination = "jms/queue/mail", concurrency = "10")
    public void onMailMessageReceived(Message message) throws Exception {
        logger.info("received message: " + message);
        if (message instanceof TextMessage) {
            String text = ((TextMessage) message).getText();
            MailMessage mm = objectMapper.readValue(text, MailMessage.class);
            if(mm.type == MailMessage.Type.REGISTRATION){
                mailService.sendRegistrationMail(mm);}
            else if (mm.type == MailMessage.Type.SIGNIN) {
                mailService.sendSignInMail(mm);
            }
        } else {
            logger.error("unable to process non-text message!");
        }
    }
}
