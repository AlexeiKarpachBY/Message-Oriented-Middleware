package com.streams.services;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.streams.models.Employee;

import java.util.Map;

public class Serializer implements org.apache.kafka.common.serialization.Serializer<Employee> {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
    }

    @Override
    public byte[] serialize(String topic, Employee employee) {
        try {
            return objectMapper.writeValueAsBytes(employee);
        } catch (Exception e) {
            throw new RuntimeException("Can`t serializing Employee", e);
        }
    }

}
