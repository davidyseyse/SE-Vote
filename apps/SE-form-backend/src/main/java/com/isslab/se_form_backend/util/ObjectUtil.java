package com.isslab.se_form_backend.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public class ObjectUtil {
    public static String objectToJsonStr(Object content) {
        try {
            return (new ObjectMapper().disable(SerializationFeature.FAIL_ON_EMPTY_BEANS)).writeValueAsString(content);
        } catch (JsonProcessingException e) {
            return content.getClass().getName() + '@' + Integer.toHexString(content.hashCode());
        }
    }

    public static <T> T jsonStringToObject(String jsonString, Class<T> ObjectType) throws JsonProcessingException {
        return new ObjectMapper()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .readerFor(ObjectType)
                .readValue(jsonString);
    }
}
