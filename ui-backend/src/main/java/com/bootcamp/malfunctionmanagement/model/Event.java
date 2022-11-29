package com.bootcamp.malfunctionmanagement.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Event {
    @JsonProperty("details")
    private Detail details;
    @JsonProperty("id")
    private String malfunction_id;
    @JsonProperty("stage")
    private String stage;
    @JsonProperty("timestamp")
    private long timestamp;
    @JsonProperty("type")
    private String type;
}