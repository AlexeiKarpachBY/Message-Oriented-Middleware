package com.task_3.consumer;

import com.task_3.models.MyMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class Consumer {
    private static final Logger LOGGER = LoggerFactory.getLogger(Consumer.class);
    private static final String QUEUE = "my.cosmic.queue";

    @RabbitListener(queues = QUEUE, containerFactory = "customConnectionFactory")
    public void receiveMessage(MyMessage message) {
        if (!message.getMessage().startsWith("VALID")) {
            logInvalidMessage(message);
            throw new RuntimeException("Throwing exception...");
        } else logValidMessage(message);

    }

    private void logInvalidMessage(MyMessage message) {
        LOGGER.info("\u001B[31m" + String.format("Received INVALID message is: %s", message.getMessage()) + "\u001B[0m");
    }

    private void logValidMessage(MyMessage message) {
        LOGGER.info("\u001B[32m" + String.format("Received VALID message is: %s", message.getMessage()) + "\u001B[0m");
    }

}
