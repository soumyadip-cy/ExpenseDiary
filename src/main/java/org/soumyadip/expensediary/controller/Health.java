package org.soumyadip.expensediary.controller;

import org.soumyadip.expensediary.dto.ApiMessage;
import org.soumyadip.expensediary.dto.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;

@RestController
@RequestMapping("/api/v1/health")
public class Health {

    @GetMapping("/health-check")
    public ResponseEntity<ApiResponse<ApiMessage>> health() {
        return ResponseEntity.status(HttpStatus.OK).body(
                new ApiResponse<>(
                        true,
                        new ApiMessage("OK!"),
                        HttpStatus.OK.value(),
                        Instant.now()
                )
        );
    }
}
