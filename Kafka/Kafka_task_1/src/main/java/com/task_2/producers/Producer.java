package com.task_2.producers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class Producer {

    private static final Logger LOGGER = LoggerFactory.getLogger(Producer.class);
    @Autowired
    private KafkaTemplate<String, String> template;

    public void sendMessage(String topic, String message) {
        LOGGER.info("Send message '{}' to topic='{}'", message, topic);
        template.send(topic, message);
    }

}
