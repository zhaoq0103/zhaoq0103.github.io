package com.x.ai.service;


import com.x.ai.entity.MailMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class MailService {

    final Logger logger = LoggerFactory.getLogger(getClass());

    public void sendRegistrationMail(MailMessage mm) {
        logger.info("[send mail] sending registration mail to {}...", mm.email);
        // TODO: simulate a long-time task:
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
        }
        logger.info("[send mail] registration mail was sent to {}.", mm.email);
    }

    public void sendSignInMail(MailMessage mm) {
        logger.info("[send mail] sending signin mail to {}...", mm.email);
        // TODO: simulate a long-time task:
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
        }
        logger.info("[send mail] signin mail was sent to {}.", mm.email);
    }

}
