package com.practicalTask1.subscriber;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

@Component
public class DurableSubscriber implements MessageListener {

    private final static Logger logger = LoggerFactory.getLogger(DurableSubscriber.class);

    @Override
    public void onMessage(Message message) {
        if (message instanceof TextMessage) {
            try {
                ((TextMessage) message).getText();
                logger.info("DurableSubscriber say : {}", ((TextMessage) message).getText());
            } catch (JMSException ex) {
                throw new RuntimeException(ex);
            }
        } else {
            throw new IllegalArgumentException("Wrong message type");
        }
    }
}
