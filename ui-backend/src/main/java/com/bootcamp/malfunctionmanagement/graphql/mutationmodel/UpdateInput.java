package com.bootcamp.malfunctionmanagement.graphql.mutationmodel;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UpdateInput {
    String malfunctionId;
    String stage;
    String crew_id;
    String description;
    String malfunction_cause;
}
