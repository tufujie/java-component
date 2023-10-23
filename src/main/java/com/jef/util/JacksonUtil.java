package com.jef.util;

//import com.alibaba.fastjson.JSON;
//import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by kim on 16/9/02.
 */
public class JacksonUtil {

    private static final Logger logger = LoggerFactory.getLogger(JacksonUtil.class);

    /**
     * 精简输出Mapper
     */
    private static final ObjectMapper objectMapper = new ObjectMapper();

    static {
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
        objectMapper.configure(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS, true);
        objectMapper.configure(SerializationFeature.WRITE_EMPTY_JSON_ARRAYS, false);
    }


    /**
     * 异常必须抛给上层处理，否则可能会出现很多坑爹问题
     */
    public static String serialize(Object serialized) throws Exception {
        if (serialized == null) {
            return null;
        }
        return objectMapper.writeValueAsString(serialized);
    }

    /**
     * 使用JSON格式化数据
     */
    public static String serializeWithPretty(Object serialized) throws Exception {
        if (serialized == null) {
            return null;
        }
        return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(serialized);
    }

    public static String serializeIgnoreException(Object serialized) {
        try {
            return serialize(serialized);
        } catch (Exception e) {
            logger.error(" serializeIgnoreException execute error ", e);
        }
        return null;
    }

}
