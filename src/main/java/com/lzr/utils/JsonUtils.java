package com.lzr.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;

import java.io.IOException;

@Log4j2
public class JsonUtils {

    public static ObjectMapper objectMapper = new ObjectMapper();

    public static String Object2json(Object object) throws JsonProcessingException {
        return objectMapper.writeValueAsString(object);
    }

    public static <T> T json2Object(String json, Class<T> valueType) throws IOException {
        return objectMapper.readValue(json, valueType);
    }

    /**
     * 不抛出异常方法
     * @param object
     * @return
     */
    public static String ObjectToJson(Object object){
        try {
            return objectMapper.writeValueAsString(object);
        }catch (JsonProcessingException jsonException){
            jsonException.printStackTrace();
            return null;
        }
    }

    /**
     * 不抛出异常方法
     * @param json
     * @param valueType
     * @return
     */
    public static <T> T jsonToObject(String json, Class<T> valueType){
        try {
            return objectMapper.readValue(json, valueType);
        }catch (IOException e){
            e.printStackTrace();
            return null;
        }
    }


}