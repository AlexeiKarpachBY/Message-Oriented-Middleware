package com.practicalTask1.controller;

import com.practicalTask1.publisher.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PublishController {
    @Autowired
    private Publisher publisher;

    @PostMapping("/publishMessage/{message}")
    public ResponseEntity<String> publishMessage(@PathVariable String message) {
        publisher.sendMessage(message);
        return new ResponseEntity<>("sent", HttpStatus.OK);
    }
}
