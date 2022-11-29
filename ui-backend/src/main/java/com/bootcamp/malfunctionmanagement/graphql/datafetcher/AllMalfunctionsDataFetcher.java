package com.bootcamp.malfunctionmanagement.graphql.datafetcher;

import com.bootcamp.malfunctionmanagement.model.Malfunction;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Component
public class AllMalfunctionsDataFetcher implements DataFetcher<List<Malfunction>> {
    @Autowired
    private RestTemplate restTemplate;

    @Override
    public List<Malfunction> get(DataFetchingEnvironment dataFetchingEnvironment) {
        String url = "http://localhost:8081/malfunctions";
        ResponseEntity<List<Malfunction>> response = restTemplate.exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<List<Malfunction>>() {});
        List<Malfunction> malfunctions = response.getBody();
        return malfunctions;
    }
}
