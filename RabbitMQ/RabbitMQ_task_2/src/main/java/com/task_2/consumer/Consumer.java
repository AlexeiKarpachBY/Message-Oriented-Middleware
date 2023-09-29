package com.task_2.consumer;


import com.task_2.exception.RetryException;
import com.task_2.models.MyMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class Consumer {
    private static final Logger LOGGER = LoggerFactory.getLogger(Consumer.class);
    private static final String QUEUE = "my.cosmic.queue";


    @RabbitListener(queues = "my.cosmic.queue", containerFactory = "customConnectionfactory")
    public void receiveMessage(MyMessage message) throws Exception {
        if (!message.getMessage().startsWith("VALID")) {
            LOGGER.info("\u001B[31m" + String.format("Received INVALID message is: %s", message.getMessage()) + "\u001B[0m");
            throw new RetryException("Throwing Custom Retry exception...");
        }else LOGGER.info("\u001B[32m" + String.format("Received VALID message is: %s", message.getMessage()) + "\u001B[0m");
    }


}
