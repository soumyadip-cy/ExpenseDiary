package org.soumyadip.expensediary.controller;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.soumyadip.expensediary.dto.AuthRequest;
import org.soumyadip.expensediary.dto.AuthResponse;
import org.soumyadip.expensediary.entity.ImplementedUserDetails;
import org.soumyadip.expensediary.entity.RefreshToken;
import org.soumyadip.expensediary.entity.User;
import org.soumyadip.expensediary.exception.InvalidRefreshTokenException;
import org.soumyadip.expensediary.exception.RefreshTokenExpiredException;
import org.soumyadip.expensediary.repository.UserRepository;
import org.soumyadip.expensediary.service.RefreshTokenService;
import org.soumyadip.expensediary.util.JWTutil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    @Value("${secure-cookie-pref}")
    private boolean secureCookiePref;
    private final AuthenticationManager authenticationManager;
    private final JWTutil jwtutil;
    private final RefreshTokenService refreshTokenService;
    private final UserRepository userRepository;

    @PostMapping("/authenticate")
    public String generateToken(@RequestBody AuthRequest authRequest) {

        try {
            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            authRequest.username(),
                            authRequest.password()
                    )
            );
            log.info("Authentication Successful: {}", auth.getPrincipal());
            return jwtutil.generateToken(auth.getName());
        } catch (AuthenticationException e) {
            log.error("Authentication Failed: {}", e.getMessage());
            return e.getMessage();
        }
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(
            @RequestBody @Valid AuthRequest authRequest,
            HttpServletResponse response
    ) {
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                authRequest.username(),
                authRequest.password()
            )
        );

        ImplementedUserDetails principal = (ImplementedUserDetails) authentication.getPrincipal();

        User user = userRepository.findByUsername(principal.getUsername()).orElseThrow(() -> new UsernameNotFoundException("Username not found: "+authentication.getName()));

        String accessToken = jwtutil.generateToken(principal.getUsername());

        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user);

        ResponseCookie cookie = ResponseCookie.from(
                "refreshToken",
                refreshToken.getRefreshToken())
                .httpOnly(true)
                        .secure(secureCookiePref)
                                .sameSite("Strict")
                                        .path("/")
                                                .maxAge(Duration.ofDays(7))
                                                        .build();

        response.addHeader("Set-Cookie", cookie.toString());

        log.debug("User Logged in: {}", user);

        return ResponseEntity.ok(new AuthResponse(accessToken));
    }

    @PostMapping
    public ResponseEntity<AuthResponse> refreshToken(
            @CookieValue("refreshToken") String refreshToken
    ) throws InvalidRefreshTokenException, RefreshTokenExpiredException {

        RefreshToken newRefreshToken = refreshTokenService.rotateRefreshToken(refreshToken);

        String accessToken = jwtutil.generateToken(newRefreshToken.getUser().getUsername());

        return ResponseEntity.ok(new AuthResponse(accessToken));
    }
}
