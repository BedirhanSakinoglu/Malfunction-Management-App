package com.bootcamp.malfunctionmanagement.graphql.datafetcher;

import com.bootcamp.malfunctionmanagement.model.Malfunction;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class MalfunctionDataFetcher implements DataFetcher<Malfunction> {
    @Autowired
    private RestTemplate restTemplate;

    @Override
    public Malfunction get(DataFetchingEnvironment dataFetchingEnvironment) {
        String id = dataFetchingEnvironment.getArgument("id");
        String url = "http://localhost:8081/malfunctions/" + id;
        Malfunction malfunction = restTemplate.getForObject(url, Malfunction.class);
        return malfunction;
    }
}
