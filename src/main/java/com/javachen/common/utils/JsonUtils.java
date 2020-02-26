package com.javachen.common.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;
import java.io.StringWriter;
import java.text.SimpleDateFormat;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.ANY;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.PropertyAccessor.ALL;
import static com.fasterxml.jackson.annotation.PropertyAccessor.FIELD;
import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES;
import static com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS;
import static java.time.ZoneId.of;
import static java.util.TimeZone.getTimeZone;

public class JsonUtils {
    private static final String JSON_DATE_FROMATE = "yyyy-MM-dd HH:mm:ss";

    /**
     * 获取json中指定的字段值
     *
     * @param jsonAsString
     * @param field
     * @return
     * @throws IOException
     */
    public static String fromJson(String jsonAsString, String field) throws IOException {
        JsonNode jsonNode = getObjectMapper().readTree(jsonAsString);
        return jsonNode.get(field).toString();
    }

    public static <T> T fromJson(String jsonAsString, Class<T> pojoClass) {
        return (T) getObjectMapper().readValue(jsonAsString, pojoClass);
    }

    public static <T> T fromJson(String jsonAsString, TypeReference<T> type) {
        return (T) getObjectMapper().readValue(jsonAsString, type);
    }

    public static String toJson(Object pojo, boolean prettyPrint) {
        StringWriter sw = new StringWriter();
        JsonFactory jsonFactory = new JsonFactory();
        try {
            JsonGenerator jg = jsonFactory.createGenerator(sw);
            if (prettyPrint) {
                jg.useDefaultPrettyPrinter();
            }
            getObjectMapper().writeValue(jg, pojo);
        } catch (IOException e) {
            throw new RuntimeException("解析json出错", e);
        }
        return sw.toString();
    }

    public static String toJson(Object pojo) {
        return toJson(pojo, false);
    }

    private static DefaultObjectMapper getObjectMapper() {
        DefaultObjectMapper objectMapper = new DefaultObjectMapper();
        objectMapper.findAndRegisterModules()
                .setSerializationInclusion(JsonInclude.Include.NON_NULL)
                .setDateFormat(new SimpleDateFormat(JSON_DATE_FROMATE))
                .setTimeZone(getTimeZone(of("Asia/Shanghai")))
                .setVisibility(ALL, NONE)
                .setVisibility(FIELD, ANY)
                .configure(WRITE_DATES_AS_TIMESTAMPS, false)
                .configure(FAIL_ON_UNKNOWN_PROPERTIES, false)
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
        return objectMapper;
    }
}
