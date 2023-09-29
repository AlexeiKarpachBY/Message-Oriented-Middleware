package com.practicalTask2.subscriber;

import com.practicalTask2.publisher.Publisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.TextMessage;

@Component
public class Subscriber {

    private final static String RESPONSE_MESSAGE_TEMPLATE = "Everyone says %s, but you buy an elephant :)";
    private final static Logger logger = LoggerFactory.getLogger(Subscriber.class);
    @Autowired
    private JmsTemplate jmsTemplate;

    @JmsListener(destination = Publisher.QUEUENAME)
    public void receiveMessage(TextMessage message) throws JMSException {
        logger.info("Request is: {}", message.getText());
        String responseMessage = String.format(RESPONSE_MESSAGE_TEMPLATE, message.getText());
        jmsTemplate.send(message.getJMSReplyTo(), session -> {
            Message responseMsg = session.createTextMessage(responseMessage);
            Destination destination = session.createTemporaryQueue();
            responseMsg.setJMSCorrelationID(message.getJMSCorrelationID());
            responseMsg.setJMSDestination(destination);
            return responseMsg;
        });
    }
}
