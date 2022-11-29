package com.bootcamp.malfunctionmanagement.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@RedisHash(value = "${spring.redis.hashName.outage}")
@Data
public class Malfunction {
    @Id
    String malfunctionId;
    long startTimestamp;
    long endTimestamp;
    List<String> stage;
    Detail details;
}