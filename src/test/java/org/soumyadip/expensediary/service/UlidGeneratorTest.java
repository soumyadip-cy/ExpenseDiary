package org.soumyadip.expensediary.service;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UlidGeneratorTest {
    @Test
    void generateReturnsUlidValue() {
        String value = new UlidGenerator().generate();

        assertNotNull(value);
        assertEquals(26, value.length());
    }
}
