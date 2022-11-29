package com.bootcamp.malfunctionmanagement;

import com.bootcamp.malfunctionmanagement.helper.ReadResource;
import com.bootcamp.malfunctionmanagement.model.Event;
import com.bootcamp.malfunctionmanagement.producer.EventProducer;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.kafka.core.KafkaTemplate;
import org.testng.annotations.Listeners;
import org.mockito.testng.MockitoTestNGListener;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.List;

@Listeners(MockitoTestNGListener.class)
public class EventProducerTest {
    @Mock
    KafkaTemplate<String, Event> kafkaTemplate;
    @Mock
    List<Event> expected;
    @InjectMocks
    EventProducer eventProducer;

    @Test
    void eventList_sendCalled_sendToKafka() throws IOException{
        final List<Event> mockPowerflowResultMessage = ReadResource.getResourceList("randomEvents.json", Event.class);
    }
}
