package com.bootcamp.malfunctionmanagement.graphql;

import graphql.ExecutionInput;
import graphql.ExecutionResult;
import graphql.spring.web.servlet.components.GraphQLRequestBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@CrossOrigin(origins="http://localhost:4200")
@RestController
@RequestMapping
public class GraphQLController {
    @Autowired
    GraphQLService graphQLService;

    @PostMapping(value="/malfunctions")
    public ResponseEntity<Object> getAllMalfunctions(@RequestBody GraphQLRequestBody body){
        String replacedQuery = body.getQuery();
        ExecutionInput executionInput = ExecutionInput.newExecutionInput().query(body.getQuery()).variables(body.getVariables()).build();
        ExecutionResult execute = graphQLService.initiateGraphQL().execute(executionInput);
        return new ResponseEntity<>(execute, HttpStatus.OK);
    }
}
