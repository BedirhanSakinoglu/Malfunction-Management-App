package com.bootcamp.malfunctionmanagement.controller;

import com.bootcamp.malfunctionmanagement.producer.EventProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/event")
public class EventController {
    final EventProducer eventProducer;

    public EventController(@Autowired EventProducer eventProducer) {
        this.eventProducer = eventProducer;
    }

    @PostMapping(value = "/publish")
    public String publishEvent() throws IOException, InterruptedException {
        eventProducer.send();
        return "PUBLISHED";
    }

}
