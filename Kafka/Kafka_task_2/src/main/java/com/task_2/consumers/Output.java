package com.task_2.consumers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class Output {

    private static final Logger LOGGER = LoggerFactory.getLogger(Output.class);
    private static final String TOPIC = "cosmic-output-topic";
    private static final String GROUP_ID = "group";

    @KafkaListener(topics = TOPIC, groupId = GROUP_ID)
    public void receive(String distance) {
        LOGGER.info("Output Distance: {}", distance);
    }

}
