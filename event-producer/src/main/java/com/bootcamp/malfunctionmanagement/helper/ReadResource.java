package com.bootcamp.malfunctionmanagement.helper;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.bootcamp.malfunctionmanagement.model.Event;
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class ReadResource {
    private static final ObjectMapper mapper = new ObjectMapper();

    public static <T> T  getResource(String fileResourcePath, Class<T> clazz ) throws IOException, JsonParseException {
        return (T) mapper.readValue(new ClassPathResource(fileResourcePath).getFile(), clazz);
    }

    public static <T> List<T>  getResourceList(String fileResourcePath, Class<T> clazz ) throws IOException, JsonParseException {
        return (List<T>) mapper.readValue(new File(String.valueOf(new ClassPathResource(fileResourcePath).getFile())), new TypeReference<List<Event>>(){});
    }
}