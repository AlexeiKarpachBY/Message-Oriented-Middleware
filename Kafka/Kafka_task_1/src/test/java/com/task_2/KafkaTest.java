package com.task_2;


import com.task_2.consumers.Consumer;
import com.task_2.producers.Producer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@DirtiesContext
@EmbeddedKafka(partitions = 1, brokerProperties = {"listeners=PLAINTEXT://localhost:9092", "port=9092"})
public class KafkaTest {
    private static final String TOPIC = "test-topic";

    private static final String TEST_MESSAGE = "cosmic-test-message";

    @Autowired
    private Producer producer;

    @Autowired
    private Consumer consumer;

    @Test
    public void testGetsMessages() throws Exception {
        producer.sendMessage(TOPIC, TEST_MESSAGE);

        Thread.sleep(5000);

        assertTrue(consumer.getMessageStore().contains(TEST_MESSAGE));
    }

}
