package com.redcare.git.demo.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.redcare.git.demo.dto.GitRepositoriesResponse;
import com.redcare.git.demo.exception.GitDataParseException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class UtilityTests {

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testConvertJsonNodeToMap_CorrectlyConversion() throws IOException {
        var resource = new ClassPathResource("response/git-repositories-response.json");
        var gitResponse = objectMapper.readValue(resource.getInputStream(), GitRepositoriesResponse.class);

        gitResponse.items().forEach(item -> {
            var mapNode = Utility.convertJsonNodeToMap(item);
            assertNotNull(mapNode);
        });
    }

    @Test
    void testConvertJsonNodeToMap_GitDataParseException() throws IOException {
        var jsonNode = objectMapper.readTree("\"NOT_A_JSON_OBJECT\"");
        assertThrows(GitDataParseException.class, () -> Utility.convertJsonNodeToMap(jsonNode));
    }
}