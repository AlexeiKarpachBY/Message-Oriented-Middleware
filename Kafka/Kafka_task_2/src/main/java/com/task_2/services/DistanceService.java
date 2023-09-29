package com.task_2.services;

import com.task_2.models.Signal;
import com.task_2.producers.OutputProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DistanceService {
    @Autowired
    private OutputProducer producer;

    public void update(Signal signal) {
        producer.send(signal.getDistance().toString());
    }

}
