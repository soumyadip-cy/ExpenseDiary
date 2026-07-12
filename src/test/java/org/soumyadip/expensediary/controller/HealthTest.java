package org.soumyadip.expensediary.controller;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class HealthTest {
    @Test
    void healthReturnsOk() {
        assertEquals("OK", new Health().health());
    }
}
