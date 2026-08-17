package org.soumyadip.expensediary.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.soumyadip.expensediary.dto.ApiMessage;
import org.soumyadip.expensediary.dto.ApiResponse;
import org.soumyadip.expensediary.dto.AuthRequest;
import org.soumyadip.expensediary.dto.AuthResponse;
import org.soumyadip.expensediary.exception.InvalidRefreshTokenException;
import org.soumyadip.expensediary.exception.RefreshTokenExpiredException;
import org.soumyadip.expensediary.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(
            @RequestBody @Valid AuthRequest authRequest,
            HttpServletResponse response
    ) {
        AuthResponse authResponse = authService.login(authRequest, response);
        return ResponseEntity.status(HttpStatus.OK).body(
                new ApiResponse<>(
                        true,
                        authResponse,
                        HttpStatus.OK.value(),
                        Instant.now()
                )
        );
    }

    @GetMapping("/refresh")
    public ResponseEntity<ApiResponse<AuthResponse>> refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws InvalidRefreshTokenException, RefreshTokenExpiredException {

        AuthResponse authResponse = authService.refreshAuthentication(request, response);
        return ResponseEntity.status(HttpStatus.OK).body(
                new ApiResponse<>(
                        true,
                        authResponse,
                        HttpStatus.OK.value(),
                        Instant.now()
                )
        );
    }

    @GetMapping("/auth-check")
    public ResponseEntity<ApiResponse<ApiMessage>> authCheck() {
        return ResponseEntity.status(HttpStatus.OK).body(
                new ApiResponse<>(
                        true,
                        new ApiMessage("Authentication check successful!"),
                        HttpStatus.OK.value(),
                        Instant.now()
                )
        );
    }

    @GetMapping("/logout")
    public ResponseEntity<ApiResponse<ApiMessage>> logout(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws InvalidRefreshTokenException, RefreshTokenExpiredException {

        authService.logout(request, response);
        return ResponseEntity.status(HttpStatus.OK).body(
                new ApiResponse<>(
                        true,
                        new ApiMessage("Logged out successfully!"),
                        HttpStatus.OK.value(),
                        Instant.now()
                )
        );
    }
}
