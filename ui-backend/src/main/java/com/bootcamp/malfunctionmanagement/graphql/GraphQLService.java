package com.bootcamp.malfunctionmanagement.graphql;

import com.bootcamp.malfunctionmanagement.graphql.datafetcher.MalfunctionDataFetcher;
import com.bootcamp.malfunctionmanagement.graphql.mutationresolver.MutationResolver;
import com.bootcamp.malfunctionmanagement.graphql.datafetcher.AllMalfunctionsDataFetcher;
import com.bootcamp.malfunctionmanagement.graphql.datafetcher.DetailDataFetcher;
import graphql.GraphQL;
import graphql.schema.GraphQLSchema;
import graphql.schema.idl.RuntimeWiring;
import graphql.schema.idl.SchemaGenerator;
import graphql.schema.idl.SchemaParser;
import graphql.schema.idl.TypeDefinitionRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;


@Service
public class GraphQLService {
    @Autowired
    AllMalfunctionsDataFetcher allMalfunctionsDataFetcher;
    @Autowired
    MalfunctionDataFetcher malfunctionDataFetcher;
    @Autowired
    DetailDataFetcher detailDataFetcher;
    @Autowired
    MutationResolver mutationResolver;

    @Value("graphql/schema.graphqls")
    private ClassPathResource classPathLoader;
    private GraphQL graphQL;

    @PostConstruct
    private void loadSchema() throws IOException {
        InputStream stream = classPathLoader.getInputStream();
        Reader targetReader = new InputStreamReader(stream);

        TypeDefinitionRegistry typeDefinitionRegistry = new SchemaParser().parse(targetReader);
        RuntimeWiring runtimeWiring = buildRuntimeWiring();
        GraphQLSchema graphQLSchema = new SchemaGenerator().makeExecutableSchema(typeDefinitionRegistry, runtimeWiring);
        graphQL = GraphQL.newGraphQL(graphQLSchema).build();
    }

    private RuntimeWiring buildRuntimeWiring(){
        return RuntimeWiring.newRuntimeWiring()
                .type("Query", typeWiring -> typeWiring
                                .dataFetcher("allMalfunctions", allMalfunctionsDataFetcher)
                                .dataFetcher("malfunction", malfunctionDataFetcher)
                                .dataFetcher("detail", detailDataFetcher))
                .type("Mutation", typeWiring -> typeWiring
                                .dataFetcher("updateMalfunction", mutationResolver))
                .build();
    }

    public GraphQL initiateGraphQL(){
        return graphQL;
    }

}
