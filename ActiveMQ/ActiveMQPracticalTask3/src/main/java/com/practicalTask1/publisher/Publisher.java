package com.practicalTask1.publisher;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.JmsException;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

@Service
public class Publisher {
    private final static Logger logger = LoggerFactory.getLogger(Publisher.class);

    @Autowired
    private JmsTemplate jmsTemplate;

    public void sendMessage(final String message) throws JmsException {
        jmsTemplate.convertAndSend(message);
        logger.info("Publisher sent message: {} ", message);
    }

}

