package com.streams.configuration;

import com.streams.models.Employee;
import com.streams.services.Deserializer;
import com.streams.services.Serializer;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.JoinWindows;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.StreamJoined;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.EnableKafkaStreams;

import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;

@EnableKafka
@EnableKafkaStreams
@org.springframework.context.annotation.Configuration
public class KSConfiguration {

    private static final Logger LOGGER = LoggerFactory.getLogger(KSConfiguration.class);

    @Bean
    public KStream<String, String> cosmicOneStream(StreamsBuilder builder) {
        KStream<String, String> cosmicStream = builder.stream("task1-1");
        cosmicStream.peek((key, value) -> LOGGER.info("Message is: {}, Key is: {}", value, key))
                .to("task1-2");
        return cosmicStream;
    }

    @Bean
    public KStream<Integer, String> cosmicTwoStream(StreamsBuilder builder) {
        KStream<String, String> inputStream = builder.stream("task2");
        KStream<Integer, String> wordStream = inputStream
                .flatMap((key, value) -> {
                    if (value == null) {
                        return Collections.emptyList();
                    }
                    return splitSentence(value);
                });
        wordStream.peek((key, value) -> LOGGER.info("Splitted : Value is: {}, Key is: {}", value, key));
        return wordStream;
    }

    @Bean
    public Map<String, KStream<Integer, String>> wordsStream(@Qualifier("cosmicTwoStream") KStream<Integer, String> wordStream) {
        KStream<Integer, String>[] branches = wordStream.branch(
                (key, value) -> key < 5,
                (key, value) -> key >= 5
        );
        KStream<Integer, String> shortWords = branches[0];
        KStream<Integer, String> longWords = branches[1];
        Map<String, KStream<Integer, String>> result = new HashMap<>();
        result.put("short", shortWords);
        result.put("long", longWords);
        return result;
    }

    @Bean
    public Map<String, KStream<Integer, String>> filterWords(@Qualifier("wordsStream") Map<String, KStream<Integer, String>> wordStreams) {
        KStream<Integer, String> shortWords = wordStreams.get("short");
        KStream<Integer, String> longWords = wordStreams.get("long");
        KStream<Integer, String> filteredShortWords = shortWords.filter((key, value) -> value.contains("a"));
        KStream<Integer, String> filteredLongWords = longWords.filter((key, value) -> value.contains("a"));
        Map<String, KStream<Integer, String>> result = new HashMap<>();
        result.put("short", filteredShortWords);
        result.put("long", filteredLongWords);
        return result;
    }

    @Bean
    public KStream<Integer, String> mergedWords(@Qualifier("filterWords") Map<String, KStream<Integer, String>> filteredWordStreams) {
        KStream<Integer, String> filteredShortWords = filteredWordStreams.get("short");
        KStream<Integer, String> filteredLongWords = filteredWordStreams.get("long");
        KStream<Integer, String> mergedStream = filteredShortWords.merge(filteredLongWords);
        mergedStream.peek((key, value) -> LOGGER.info("Message is: {}, Key is: {}", key, value));
        return mergedStream;
    }

    private List<KeyValue<Integer, String>> splitSentence(String value) {
        return Arrays.stream(value.split("\\s+"))
                .map(word -> new KeyValue<>(word.length(), word))
                .collect(Collectors.toList());
    }

    @Bean
    public KStream<Long, String> cosmicThreeStream(StreamsBuilder builder) {
        KStream<String, String> task3_1Stream = builder.stream("task3-1", Consumed.with(Serdes.String(), Serdes.String()));
        KStream<String, String> task3_2Stream = builder.stream("task3-2", Consumed.with(Serdes.String(), Serdes.String()));
        task3_1Stream = task3_1Stream.filter((key, value) -> value != null && value.contains(":"));
        task3_2Stream = task3_2Stream.filter((key, value) -> value != null && value.contains(":"));
        KStream<Long, String> task3_1StreamWithNewKey = task3_1Stream.map((key, value) -> {
            Long newKey = Long.valueOf(value.split(":")[0]);
            return KeyValue.pair(newKey, value);
        });
        KStream<Long, String> task3_2StreamWithNewKey = task3_2Stream.map((key, value) -> {
            Long newKey = Long.valueOf(value.split(":")[0]);
            return KeyValue.pair(newKey, value);
        });
        task3_1StreamWithNewKey.foreach((key, value) -> LOGGER.info("task3-1: key=" + key + ", value=" + value));
        task3_2StreamWithNewKey.foreach((key, value) -> LOGGER.info("task3-2: key=" + key + ", value=" + value));
        KStream<Long, String> joinedStream = task3_1StreamWithNewKey.join(
                task3_2StreamWithNewKey,
                (leftValue, rightValue) -> leftValue + " ::: " + rightValue,
                JoinWindows.of(Duration.ofMinutes(1)).grace(Duration.ofSeconds(30)),
                StreamJoined.with(Serdes.Long(), Serdes.String(), Serdes.String())
        );
        joinedStream.foreach((key, value) -> LOGGER.info("Joined: key=" + key + ", value=" + value));
        return joinedStream;
    }

    @Bean
    public KStream<String, Employee> cosmicFourStream(StreamsBuilder builder) {
        KStream<String, Employee> employeeStream = builder.stream("task4",
                Consumed.with(Serdes.String(),
                        Serdes.serdeFrom(new Serializer(),
                                new Deserializer())));
        employeeStream.filter((k, v) -> v != null)
                .foreach((k, v) -> LOGGER.info("Key: " + k + ", Value: " + v));
        return employeeStream;
    }


}
