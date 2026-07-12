package org.soumyadip.expensediary;

import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class ExpenseDiaryApplicationTests {

    @Test
    void applicationIsConfiguredAsSpringBootApplication() {
        assertNotNull(ExpenseDiaryApplication.class.getAnnotation(SpringBootApplication.class));
    }

}
