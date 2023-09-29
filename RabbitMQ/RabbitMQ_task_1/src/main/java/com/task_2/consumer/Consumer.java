package com.task_2.consumer;

import com.task_2.models.MyMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class Consumer {
    private static final Logger LOGGER = LoggerFactory.getLogger(Consumer.class);
    private static final String QUEUE = "my.cosmic.queue";

    @Value("${topic.name}")
    private String topic;

    @Value("${topic.name.failed}")
    private String failedTopic;

    @Value("${routingKey.name}")
    private String routingKey;

    @Value("${routingKey.name.failed}")
    private String failedRoutingKey;

    int count = 1;
    int limit = 3;

    private final AmqpTemplate rabbitTemplate;

    public Consumer(AmqpTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @RabbitListener(queues = QUEUE)
    public void receiveMessage(MyMessage myMessage) {
        if (myMessage.getMessage().startsWith("VALID")) {
            LOGGER.info("\u001B[32m" + String.format("Received message is: %s", myMessage.getMessage()) + "\u001B[0m");
        } else if (count <= limit){
            count++;
            LOGGER.info("\u001B[31m" + String.format("Received message '%s' is INVALID -> Try to republish!", myMessage.getMessage()) + "\u001B[0m");
            rabbitTemplate.convertAndSend(topic, routingKey, myMessage);
        } else {
            count = 1;
            LOGGER.info("\u001B[31m" + String.format("Received message '%s' still INVALID after 3 retry -> Publish to failed topic!", myMessage.getMessage()) + "\u001B[0m");
            rabbitTemplate.convertAndSend(failedTopic, failedRoutingKey, myMessage);
        }
    }
}
