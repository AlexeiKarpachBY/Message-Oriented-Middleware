package com.task_2.producers;

import com.task_2.models.Signal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class SignalProducer {

    private static final Logger LOGGER = LoggerFactory.getLogger(SignalProducer.class);
    private static final String TOPIC = "cosmic-input-topic";

    @Autowired
    private KafkaTemplate<String, Signal> template;

    public void sendMessage(Signal message) {
        LOGGER.info("SignalProducer Sent message is: {}", message);
        template.send(TOPIC, message);
    }

}
