package com.bootcamp.malfunctionmanagement.producer;

import com.bootcamp.malfunctionmanagement.model.Event;
import com.bootcamp.malfunctionmanagement.graphql.mutationmodel.UpdateInput;
import com.bootcamp.malfunctionmanagement.model.Detail;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Log4j2
public class UpdatedEventProducer {
    @Autowired
    private RestTemplate restTemplate;

    final KafkaTemplate<String, Event> kafkaTemplate;

    @Value(value = "${spring.kafka.producer.topic}")
    String topicName;

    public boolean send(UpdateInput updateInput) throws IOException, InterruptedException {

        String type;
        if (updateInput.getStage().equals("OPEN")) {
            type = "MalfunctionCreated";
        } else if (updateInput.getStage().equals("CREW_ASSIGNED")) {
            type = "MalfunctionCrewAssigned";
        } else if (updateInput.getStage().equals("CANCELLED")) {
            type = "MalfunctionCancelled";
        } else if (updateInput.getStage().equals("CLOSED")) {
            type = "MalfunctionClosed";
        } else {
            type = "Undefined";
        }

        Detail detail = new Detail(
                updateInput.getMalfunctionId(),
                updateInput.getCrew_id(),
                updateInput.getDescription(),
                updateInput.getMalfunction_cause());

        String url = "http://localhost:8081/malfunctions/" + updateInput.getMalfunctionId();
        //Malfunction response = restTemplate.getForObject(url, Malfunction.class);
        long timeStamp = System.currentTimeMillis();

        Event event = new Event(
                detail,
                updateInput.getMalfunctionId(),
                updateInput.getStage(),
                timeStamp,
                type);

        kafkaTemplate.send(topicName, event);
        log.info("Produced: " + event.getMalfunction_id());

        return true;
    }
}
