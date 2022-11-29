package com.bootcamp.malfunctionmanagement.consumer;

import com.bootcamp.malfunctionmanagement.model.Event;
import com.bootcamp.malfunctionmanagement.model.Malfunction;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
@AllArgsConstructor
public class EventConsumer {
    private SimpMessagingTemplate template;
    private RestTemplate restTemplate;

    @KafkaListener(topics = "${spring.kafka.producer.topic}", groupId = "ui")
    public void consumeEvent(Event message){
        log.info("Message consumed! " + message.getMalfunction_id());
        Malfunction malfunction = constructMalfunction(message);
        template.convertAndSend("/topic/malfunction", malfunction);
    }

    public Malfunction constructMalfunction(Event event){
        //Construction of malfunction
        event.getDetails().setDetailId(event.getMalfunction_id());

        String id = event.getMalfunction_id();
        String url = "http://localhost:8081/malfunctions/" + id;
        Malfunction malfunction = restTemplate.getForObject(url, Malfunction.class);

        if(malfunction == null){
            System.out.println("New malfunction");
            List<String> stage = new ArrayList<String>();
            stage.add(event.getStage());
            malfunction = new Malfunction(
                    event.getMalfunction_id(),
                    event.getTimestamp(),
                    event.getTimestamp(),
                    stage,
                    event.getDetails());
        }
        else{
            System.out.println("Update malfunction : " + event.getStage());
            if(!malfunction.getStage().get(0).equals(event.getStage())) {
                malfunction.getStage().add(0, event.getStage());
            }
            malfunction.setDetails(event.getDetails());

            //-----
            if(malfunction.getStartTimestamp() <= event.getTimestamp()){
                malfunction.setStartTimestamp(malfunction.getStartTimestamp());
            }
            else if(malfunction.getStartTimestamp() > event.getTimestamp()){
                malfunction.setStartTimestamp(event.getTimestamp());
            }

            if(malfunction.getEndTimestamp() >= event.getTimestamp()){
                malfunction.setEndTimestamp(malfunction.getEndTimestamp());
            }
            else if(malfunction.getEndTimestamp() < event.getTimestamp()){
                malfunction.setEndTimestamp(event.getTimestamp());
            }
            //-----
        }

        return malfunction;
    }


}
