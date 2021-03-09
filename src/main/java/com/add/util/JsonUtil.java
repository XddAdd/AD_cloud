package com.add.util;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;

public class JsonUtil {
    private static final ObjectMapper MAPPER = new ObjectMapper();


    /**
     * 序列化，将java对象序列化成json字符串
     * @param obj java对象
     * @return  json字符串
     */
    public static String serialize(Object obj){
        try {
            return MAPPER.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new RuntimeException("json序列化失败" + obj);
        }
    }

    /**
     * 反序列化，将json对象反序列化成java对象
     * @param input 输入流
     * @param cls 指定反序列化的类型
     * @param <T>
     * @return 反序列化的对象
     */
    public static <T> T deSerialize(InputStream input, Class<T> cls){
        try {
            return MAPPER.readValue(input, cls);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("序列化失败", e.getCause());
        }
    }


    /**
     * 反序列化，将json对象反序列化成java对象
     * @param jsonData json字符串
     * @param cls 指定反序列化的类型
     * @param <T>
     * @return 反序列化的对象
     */
    public static <T> T deSerialize(String jsonData, Class<T> cls){
        try {
            return MAPPER.readValue(jsonData, cls);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("序列化失败", e.getCause());
        }
    }




}
