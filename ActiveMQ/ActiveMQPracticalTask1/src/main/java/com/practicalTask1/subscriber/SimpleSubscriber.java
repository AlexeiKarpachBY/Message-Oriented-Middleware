package com.practicalTask1.subscriber;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

@Component
public class SimpleSubscriber implements MessageListener {

    private final static Logger logger = LoggerFactory.getLogger(SimpleSubscriber.class);

    @Override
    public void onMessage(Message message) {
        if (message instanceof TextMessage) {
            try {
                logger.info("SimpleSubscriber say: {} ", ((TextMessage) message).getText());
            } catch (JMSException ex) {
                throw new RuntimeException(ex);
            }
        } else {
            throw new IllegalArgumentException("Wrong message type");
        }
    }

}
