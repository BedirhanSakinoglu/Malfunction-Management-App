package com.bootcamp.malfunctionmanagement.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class Detail {
    private Long detailId;
    @JsonProperty("crew_id")
    private String crew_id;
    @JsonProperty("description")
    private String description;
    @JsonProperty("malfunction_cause")
    private String malfunction_cause;
}
