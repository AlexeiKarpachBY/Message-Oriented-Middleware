package com.task_2.producers;

import com.task_2.models.Signal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class OutputProducer {

    private static final Logger LOGGER = LoggerFactory.getLogger(OutputProducer.class);
    private static final String TOPIC = "cosmic-output-topic";

    @Autowired
    private KafkaTemplate<String, String> template;

    public void send(String message) {
        LOGGER.info("OutputProducer Sent message is: {}", message);
        template.send(TOPIC, message);
    }

}
