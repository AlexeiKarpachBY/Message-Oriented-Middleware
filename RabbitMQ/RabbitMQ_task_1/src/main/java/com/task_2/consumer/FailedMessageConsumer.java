package com.task_2.consumer;

import com.task_2.models.MyMessage;
import com.task_2.repository.Repository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FailedMessageConsumer {

    private static final Logger LOGGER = LoggerFactory.getLogger(FailedMessageConsumer.class);

    @Autowired
    private final Repository repository;

    private static final String FAILED_MESSAGE_QUEUE = "my.cosmic.queue.failed";

    public FailedMessageConsumer(Repository repository) {
        this.repository = repository;
    }

    @RabbitListener(queues = FAILED_MESSAGE_QUEUE)
    public void receiveMessage(MyMessage myMessage) {
        LOGGER.info("\u001B[32m" + String.format("Saved failed message is: %s", myMessage.getMessage()) + "\u001B[0m");
        repository.save(myMessage);
    }
}
