package edu.tju.scs.serializer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

/**
 * @Author: liyuze
 * @Description:
 * @Date: Created in 12:39 18/5/14.
 */
public class JacksonUtil {

    private static ObjectMapper objectMapper = new ObjectMapper();

    public static <T> byte[] serialize(T obj){
        byte[] result = null;
        try {
            result = objectMapper.writeValueAsBytes(obj);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return result;
    }


    public static <T> T deserialize(byte[] bytes,Class<?> className){
        T obj = null;
        try {
            obj = (T) objectMapper.readValue(bytes, className);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return obj;
    }
}
