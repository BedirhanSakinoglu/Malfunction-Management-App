package com.bootcamp.malfunctionmanagement.graphql.datafetcher;

import com.bootcamp.malfunctionmanagement.model.Detail;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class DetailDataFetcher implements DataFetcher<Detail> {
    @Autowired
    private RestTemplate restTemplate;

    @Override
    public Detail get(DataFetchingEnvironment dataFetchingEnvironment) {
        String id = dataFetchingEnvironment.getArgument("id");
        String url = "http://localhost:8087/malfunctions/details/" + id;
        Detail response = restTemplate.getForObject(url, Detail.class);
        return response;
    }
}
