package org.soumyadip.expensediary.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class AddInitialUser implements CommandLineRunner {

    private final UserInitializerService userInitializerService;

    @Override
    public void run(String... args) throws Exception {
        userInitializerService.CreateInitialAdmin();
        log.info("User initialized");
    }
}
