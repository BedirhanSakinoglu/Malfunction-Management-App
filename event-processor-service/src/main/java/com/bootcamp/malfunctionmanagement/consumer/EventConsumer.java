package com.bootcamp.malfunctionmanagement.consumer;

import com.bootcamp.malfunctionmanagement.service.EventProcessorService;
import com.bootcamp.malfunctionmanagement.model.Event;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@AllArgsConstructor
public class EventConsumer {
    private final EventProcessorService eventProcessorService;

    @KafkaListener(topics = "${spring.kafka.producer.topic}", groupId = "service")
    public void consumeEventMessage(Event message){
        log.info("Message consumed: " + message.getMalfunction_id());
        eventProcessorService.constructMalfunction(message);
    }
}
