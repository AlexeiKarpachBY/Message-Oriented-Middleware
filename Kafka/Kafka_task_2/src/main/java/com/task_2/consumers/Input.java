package com.task_2.consumers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.task_2.models.Signal;
import com.task_2.services.DistanceService;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class Input {

    private static final Logger LOGGER = LoggerFactory.getLogger(Input.class);
    private static final String TOPIC = "cosmic-input-topic";
    private static final String GROUP_ID = "group";

    @Autowired
    private DistanceService distanceService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @KafkaListener(topics = TOPIC, groupId = GROUP_ID, containerFactory = "stringKafkaListenerContainerFactory")
    public void receive(ConsumerRecord<String, String> record) {
        try {
            Signal signal = objectMapper.readValue(record.value(), Signal.class);
            LOGGER.info("INPUT Signal is: {}", signal);
            distanceService.update(signal);
        } catch (Exception e) {
            LOGGER.error("Error...", e);
        }
    }

}
