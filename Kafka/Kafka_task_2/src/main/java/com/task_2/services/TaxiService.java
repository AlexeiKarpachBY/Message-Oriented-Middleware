package com.task_2.services;

import com.task_2.models.Signal;
import com.task_2.producers.SignalProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TaxiService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TaxiService.class);

    @Autowired
    private SignalProducer producer;

    public void processVehicleSignal(Signal signal) {
        if (signal.isValid()) {
            producer.sendMessage(signal);
        } else {
            LOGGER.error("Signal is invalid: {}", signal);
        }
    }
}
