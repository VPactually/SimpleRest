package com.vpactually.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public class MyObjectMapper {

    private static final ObjectMapper INSTANCE = new ObjectMapper();

    static {
        INSTANCE.registerModule(new JavaTimeModule());
    }

    private MyObjectMapper() {
    }

    public static ObjectMapper getInstance() {
        return INSTANCE;
    }

}