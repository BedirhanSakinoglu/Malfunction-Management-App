package com.bootcamp.malfunctionmanagement.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@Getter
@Setter
@RedisHash(value = "${spring.redis.hashName.detail}")
public class Detail {
    @Id
    private String detailId;
    @JsonProperty("crew_id")
    private String crew_id;
    @JsonProperty("description")
    private String description;
    @JsonProperty("malfunction_cause")
    private String malfunction_cause;
}
