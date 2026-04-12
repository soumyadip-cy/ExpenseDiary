package org.soumyadip.expensediary.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/health")
public class Health {

    @GetMapping("/health-check")
    public String health() {
        return "OK";
    }
}
