package com.practicalTask1.subscriber;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.TextMessage;

@Component
public class SubscriberTwo {
    private final static Logger logger = LoggerFactory.getLogger(SubscriberOne.class);

    private final static String DESTINATION = "Consumer.myConsumer2.VirtualTopic.MY-VIRTUAL-TOPIC";
    @Autowired
    private JmsTemplate jmsTemplate;

    @JmsListener(destination = DESTINATION)
    public void receiveMessage(TextMessage message) throws JMSException {
        logger.info("myConsumerTwo received message: {}", message.getText());
    }
}
