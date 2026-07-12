package org.soumyadip.expensediary.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.soumyadip.expensediary.dto.AuthRequest;
import org.soumyadip.expensediary.dto.AuthResponse;
import org.soumyadip.expensediary.exception.InvalidRefreshTokenException;
import org.soumyadip.expensediary.exception.RefreshTokenExpiredException;
import org.soumyadip.expensediary.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(
            @RequestBody @Valid AuthRequest authRequest,
            HttpServletResponse response
    ) {
        AuthResponse authResponse = authService.login(authRequest, response);
        return ResponseEntity.ok(authResponse);
    }

    @GetMapping("/refresh")
    public ResponseEntity<AuthResponse> refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws InvalidRefreshTokenException, RefreshTokenExpiredException {

        AuthResponse authResponse = authService.refreshAuthentication(request, response);
        return ResponseEntity.ok(authResponse);
    }

    @GetMapping("/auth-check")
    public ResponseEntity<String> authCheck() {
        return ResponseEntity.ok("Authenticated !");
    }

    @GetMapping("/logout")
    public ResponseEntity<String> logout(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws InvalidRefreshTokenException, RefreshTokenExpiredException {

        authService.logout(request, response);
        return ResponseEntity.ok().body("Login required!");
    }
}
