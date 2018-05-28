package cn.ishangchi.mydelay.util;

import cn.ishangchi.mydelay.exception.SerializationException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.nio.charset.Charset;

/**
 * jackson 序列化
 * @author shirman
 */
public class Jackson2JsonRedisSerializer {

    public static final Charset DEFAULT_CHARSET = Charset.forName("UTF-8");

    private static ObjectMapper objectMapper = new ObjectMapper();

    static {
        objectMapper.enableDefaultTyping();
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    }

    public static <T> T deserialize(byte[] bytes, Class<T> clazz) throws SerializationException {

        if ((bytes == null || bytes.length == 0)) {
            return null;
        }
        try {
            return objectMapper.readValue(bytes, 0, bytes.length, clazz);
        } catch (Exception ex) {
            throw new SerializationException("Could not read JSON: " + ex.getMessage(), ex);
        }
    }

    public static byte[] serialize(Object t) throws SerializationException {

        if (t == null) {
            return null;
        }
        try {
            return objectMapper.writeValueAsBytes(t);
        } catch (Exception ex) {
            throw new SerializationException("Could not write JSON: " + ex.getMessage(), ex);
        }
    }
}