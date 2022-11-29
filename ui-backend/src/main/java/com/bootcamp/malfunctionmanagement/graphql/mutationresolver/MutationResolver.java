package com.bootcamp.malfunctionmanagement.graphql.mutationresolver;

import com.bootcamp.malfunctionmanagement.model.Malfunction;
import com.bootcamp.malfunctionmanagement.producer.UpdatedEventProducer;
import com.bootcamp.malfunctionmanagement.graphql.mutationmodel.UpdateInput;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@AllArgsConstructor
public class MutationResolver implements DataFetcher<Malfunction> {
    @Autowired
    private RestTemplate restTemplate;
    final UpdatedEventProducer updatedEventProducer;

    @Override
    public Malfunction get(DataFetchingEnvironment environment) throws Exception {
        String url = "http://localhost:8081/malfunctions/" + environment.getArgument("malfunctionId");
        Malfunction response = restTemplate.getForObject(url, Malfunction.class);
        UpdateInput updateInput = new UpdateInput(
                environment.getArgument("malfunctionId"),
                environment.getArgument("stage"),
                environment.getArgument("crew_id"),
                environment.getArgument("description"),
                environment.getArgument("malfunction_cause"));

        updatedEventProducer.send(updateInput);
        return response;
    }
}
