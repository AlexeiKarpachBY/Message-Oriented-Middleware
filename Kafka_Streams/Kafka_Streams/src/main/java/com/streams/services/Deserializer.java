package com.streams.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.streams.models.Employee;

import java.util.Map;

public class Deserializer implements org.apache.kafka.common.serialization.Deserializer<Employee> {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
    }

    @Override
    public Employee deserialize(String topic, byte[] data) {
        try {
            return objectMapper.readValue(data, Employee.class);
        } catch (Exception e) {
            throw new RuntimeException("Can`t deserializing Employee", e);
        }
    }


}
