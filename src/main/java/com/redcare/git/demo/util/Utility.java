package com.redcare.git.demo.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.redcare.git.demo.exception.GitDataParseException;
import lombok.experimental.UtilityClass;
import lombok.extern.log4j.Log4j2;

import java.util.Map;

@UtilityClass
@Log4j2
public class Utility {
    public static Map<String, Object> convertJsonNodeToMap(JsonNode jsonNode) {
        try {
            return new ObjectMapper().convertValue(jsonNode, new TypeReference<Map<String, Object>>() {});
        } catch (IllegalArgumentException e) {
            log.error(e.getMessage());
            throw new GitDataParseException(e.getMessage());
        }
    }
}
