package org.soumyadip.expensediary.service;

import org.springframework.stereotype.Component;
import com.github.f4b6a3.ulid.UlidCreator;

@Component
public class UlidGenerator {

    public String generate() {
        return UlidCreator.getMonotonicUlid().toString();
    }
}
