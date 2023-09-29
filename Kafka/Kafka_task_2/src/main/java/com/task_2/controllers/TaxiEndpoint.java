package com.task_2.controllers;

import com.task_2.models.Signal;
import com.task_2.services.TaxiService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TaxiEndpoint {

    private static final Logger LOGGER = LoggerFactory.getLogger(TaxiEndpoint.class);

    @Autowired
    private TaxiService taxiService;

    @PostMapping("/signal")
    public void signal(@RequestBody Signal signal) {
        LOGGER.info("IN CONTROLLER Signal is: {}", signal);
        taxiService.processVehicleSignal(signal);
    }

}
