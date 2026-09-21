package com.x.ai.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.x.ai.entity.MailMessage;
import jakarta.jms.JMSException;
import jakarta.jms.Message;
import jakarta.jms.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Component;


//TextMessage：文本消息；
//BytesMessage：二进制消息；
//MapMessage：包含多个Key-Value对的消息；
//ObjectMessage：直接序列化Java对象的消息；
//StreamMessage：一个包含基本类型序列的消息。
@Component
public class MessagingService {
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    JmsTemplate jmsTemplate;

    public void sendMailMessage(MailMessage msg) throws Exception {
        String text = objectMapper.writeValueAsString(msg);
        jmsTemplate.send("jms/queue/mail", new MessageCreator() {
            public Message createMessage(Session session) throws JMSException {
                return session.createTextMessage(text);
            }
        });
    }
}
