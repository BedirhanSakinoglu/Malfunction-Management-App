package com.bootcamp.malfunctionmanagement.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@RedisHash(value = "${spring.redis.hashName.outage}")
public class Malfunction {
    @Id
    String malfunctionId;
    long startTimestamp;
    long endTimestamp;
    List<String> stage;
    Detail details;
}
