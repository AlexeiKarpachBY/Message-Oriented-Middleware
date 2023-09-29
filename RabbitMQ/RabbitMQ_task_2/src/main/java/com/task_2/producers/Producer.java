package com.task_2.producers;

import com.task_2.models.MyMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class Producer {
    private static final Logger LOGGER = LoggerFactory.getLogger(Producer.class);

    @Value("${topic.name}")
    private String topic;

    @Value("${routingKey.name}")
    private String routingKey;

    private final AmqpTemplate rabbitTemplate;

    public Producer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendMessage(MyMessage message) {
        LOGGER.info("\u001B[34m" + String.format("Message for send is: %s", message.getMessage()) + "\u001B[0m");
        rabbitTemplate.convertAndSend(topic, routingKey, message);
    }
}
