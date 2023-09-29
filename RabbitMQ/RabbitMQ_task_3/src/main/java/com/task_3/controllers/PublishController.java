package com.task_3.controllers;

import com.task_3.models.MyMessage;
import com.task_3.producers.Producer;
import com.task_3.repository.Repository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class PublishController {

    private static final Logger LOGGER = LoggerFactory.getLogger(Producer.class);

    @Autowired
    private Producer producer;

    @Autowired
    private Repository repository;

    public PublishController(Producer producer, Repository repository) {
        this.producer = producer;
        this.repository = repository;
    }

    @PostMapping("publish")
    ResponseEntity<Void> publishMessage(@RequestParam("message") String messageText) {
        MyMessage myMessage = new MyMessage();
        myMessage.setMessage(messageText);
        producer.sendMessage(myMessage);
        return ResponseEntity.ok().build();
    }

    @GetMapping("handleMessage")
    ResponseEntity<Void> publishMessage() {
        List<MyMessage> failedMyMessageList = repository.findAll();
        failedMyMessageList.forEach(myMessage -> {
            LOGGER.info("\u001B[34m" + String.format("Handle message: %s", myMessage.getMessage()) + "\u001B[0m");
            producer.sendMessage(getValidMessage(myMessage));
            repository.delete(myMessage);
        });
        return ResponseEntity.ok().build();
    }

    private MyMessage getValidMessage(MyMessage myMessage) {
        String validMessage = "VALID: ".concat(myMessage.getMessage());
        myMessage.setMessage(validMessage);
        return myMessage;
    }
}
