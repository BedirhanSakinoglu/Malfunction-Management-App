package com.bootcamp.malfunctionmanagement.producer;

import com.bootcamp.malfunctionmanagement.helper.ReadResource;
import com.bootcamp.malfunctionmanagement.model.Event;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
@Log4j2
public class EventProducer {

    final KafkaTemplate<String, Event> kafkaTemplate;

    @Value(value = "${spring.kafka.producer.topic}")
    String topicName;
    @Value(value = "${spring.kafka.producer.time-interval}")
    int timeInterval;

    public boolean send() throws IOException, InterruptedException {
        List<Event> eventList = ReadResource.getResourceList("randomEvents.json", Event.class);
        for(Event event : eventList) {
            log.debug("Event ='{}'", event.getMalfunction_id());
            if (event.getType().equals("MalfunctionCreated")) {
                event.setStage("OPEN");
            } else if (event.getType().equals("MalfunctionUpdated")) {
                event.setStage("OPEN");
            } else if (event.getType().equals("MalfunctionCrewAssigned")) {
                event.setStage("CREW_ASSIGNED");
            } else if (event.getType().equals("MalfunctionCancelled")) {
                event.setStage("CANCELLED");
            } else if (event.getType().equals("MalfunctionClosed")) {
                event.setStage("CLOSED");
            }
            kafkaTemplate.send(topicName, event);
            log.info("Events Produced");
            Thread.sleep(timeInterval);
        }
        return true;
    }

}