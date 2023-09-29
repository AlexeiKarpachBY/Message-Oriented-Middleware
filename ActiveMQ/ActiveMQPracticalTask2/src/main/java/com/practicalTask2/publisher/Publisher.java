package com.practicalTask2.publisher;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.JmsException;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import javax.jms.JMSException;
import javax.jms.TextMessage;

import static java.util.UUID.randomUUID;

@Service
public class Publisher {

    private final static Logger logger = LoggerFactory.getLogger(Publisher.class);

    public static final String QUEUENAME = "request-response";

    @Autowired
    private JmsTemplate jmsTemplate;

    public void sendMessage(final String text) throws JMSException {
        String message = ((TextMessage) (jmsTemplate.sendAndReceive(QUEUENAME, session -> {
            final TextMessage textMessage = session.createTextMessage(text);
            textMessage.setJMSCorrelationID(randomUUID().toString());
            return textMessage;
        }))).getText();
        logger.info("Response is: {}", message);
    }

}

