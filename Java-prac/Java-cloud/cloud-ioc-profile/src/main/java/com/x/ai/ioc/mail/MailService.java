package com.x.ai.ioc.mail;

public interface MailService {

    void sendMail(String address, String subject, String body);
}
