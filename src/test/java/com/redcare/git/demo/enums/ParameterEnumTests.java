package com.redcare.git.demo.enums;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class ParameterEnumTests {

    @Test
    void testFromValue() {
        var response = ParameterEnum.fromValue("language");

        assertNotNull(response);
        assertEquals(ParameterEnum.LANGUAGE, response);
    }

    @Test
    void testFromValue_UnknownValue() {
        assertThrows(IllegalArgumentException.class, () -> ParameterEnum.fromValue("unknown"));
    }
}