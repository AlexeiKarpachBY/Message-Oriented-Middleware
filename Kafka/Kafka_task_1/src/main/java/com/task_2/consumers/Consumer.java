package com.task_2.consumers;

import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Getter
@Component
public class Consumer {

    private static final Logger LOGGER = LoggerFactory.getLogger(Consumer.class);
    private static final String TOPIC = "test-topic";
    private static final String GROUP_ID = "group";

    private final List<String> messageStore = new ArrayList<>();

    @KafkaListener(topics = TOPIC, groupId = GROUP_ID)
    public void receiveMessage(String message) {
        LOGGER.info("Message is: {}", message);
        messageStore.add(message);
    }

}
