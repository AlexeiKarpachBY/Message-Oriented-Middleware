package com.task_3.consumer;

import com.task_3.models.MyMessage;
import com.task_3.repository.Repository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DeadLetterConsumer {
    private static final Logger LOGGER = LoggerFactory.getLogger(DeadLetterConsumer.class);

    @Autowired
    private final Repository repository;

    private static final String DEAD_MESSAGE_QUEUE = "my.cosmic.queue.dead";

    public DeadLetterConsumer(Repository repository) {
        this.repository = repository;
    }

    @RabbitListener(queues = DEAD_MESSAGE_QUEUE)
    public void receiveMessage(MyMessage myMessage) {
        LOGGER.info("\u001B[35m" + String.format("Saved failed message in DeadLetterConsumer is: %s", myMessage.getMessage()) + "\u001B[0m");
        repository.save(myMessage);
    }
}
