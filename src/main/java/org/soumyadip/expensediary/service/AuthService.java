package org.soumyadip.expensediary.service;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.soumyadip.expensediary.dto.AuthRequest;
import org.soumyadip.expensediary.dto.AuthResponse;
import org.soumyadip.expensediary.entity.ImplementedUserDetails;
import org.soumyadip.expensediary.entity.RefreshToken;
import org.soumyadip.expensediary.entity.User;
import org.soumyadip.expensediary.enums.AccessTimeType;
import org.soumyadip.expensediary.exception.InvalidRefreshTokenException;
import org.soumyadip.expensediary.exception.RefreshTokenExpiredException;
import org.soumyadip.expensediary.repository.UserRepository;
import org.soumyadip.expensediary.util.JWTutil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class AuthService {

    @Value("${secure-cookie-pref}")
    private boolean secureCookiePref;
    private final UserRepository userRepository;
    private final JWTutil  jwtutil;
    private final RefreshTokenService refreshTokenService;
    private final AuthenticationManager authenticationManager;
    private final ExpiredAccessTokenService expiredAccessTokenService;
    private final AccessTimeService accessTimeService;

    private boolean firstLogin(User user) {
        return accessTimeService.getAccessTimeList(user, 0).isEmpty();
    }

    public AuthResponse login(AuthRequest authRequest, HttpServletResponse response) {
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
                .path("/api/v1/auth")
                .maxAge(Duration.ofDays(7))
                .build();

        response.addHeader("Set-Cookie", cookie.toString());

        log.debug("User Logged in: {}", user.getId());

        accessTimeService.storeAccessTime(user, AccessTimeType.LOGIN);

        return new AuthResponse(accessToken);
    }

    public AuthResponse refreshAuthentication(HttpServletRequest request, HttpServletResponse response) throws InvalidRefreshTokenException, RefreshTokenExpiredException {

        Cookie[] requestCookies = request.getCookies();
        ArrayList<Cookie> cookies = new ArrayList<>(requestCookies == null ? Collections.emptyList() : Arrays.asList(requestCookies));

        String refreshToken = cookies
                .stream()
                .filter(cookie -> cookie.getName().equals("refreshToken"))
                .map(Cookie::getValue)
                .findFirst().orElse(null);

        RefreshToken newRefreshToken = refreshTokenService.rotateRefreshToken(refreshToken);

        String accessToken = jwtutil.generateToken(newRefreshToken.getUser().getUsername());

        ResponseCookie newResponseCookie = ResponseCookie.from(
                        "refreshToken",
                        newRefreshToken.getRefreshToken())
                .httpOnly(true)
                .secure(secureCookiePref)
                .sameSite("Strict")
                .path("/api/v1/auth")
                .maxAge(Duration.ofDays(7))
                .build();

        response.addHeader("Set-Cookie", newResponseCookie.toString());

        log.debug("User authentication refreshed for {}", newRefreshToken.getUser().getId());

        accessTimeService.storeAccessTime(newRefreshToken.getUser(), AccessTimeType.REFRESH);

        return new AuthResponse(accessToken);
    }

    public void logout(HttpServletRequest request, HttpServletResponse response) throws InvalidRefreshTokenException, RefreshTokenExpiredException {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Optional<User> user = userRepository.findByUsername(authentication.getName());
        User concreteUser;

        if(user.isEmpty()) {
            throw new UsernameNotFoundException("Username not found during logout: "+authentication.getName());
        } else {
            concreteUser = user.get();
        }

        ResponseCookie responseCookie;

        Cookie[] requestCookies = request.getCookies();
        ArrayList<Cookie> cookies = new ArrayList<>(requestCookies == null ? Collections.emptyList() : Arrays.asList(requestCookies));

        String refreshToken = cookies
                .stream()
                .filter(cookie -> cookie.getName().equals("refreshToken"))
                .map(Cookie::getValue)
                .findFirst().orElse(null);

        if (refreshToken != null) {
            refreshTokenService.deleteRefreshToken(refreshToken);
            responseCookie = ResponseCookie.from("refreshToken", "")
                    .httpOnly(true)
                    .secure(secureCookiePref)
                    .path("/")
                    .sameSite("Strict")
                    .maxAge(Duration.ZERO)
                    .build();
            response.addHeader("Set-Cookie", responseCookie.toString());
        }

        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if(authHeader != null && authHeader.startsWith("Bearer ")) {
            String rawToken = authHeader.substring(7);
            log.info("Authorization header found.");
            expiredAccessTokenService.expireToken(rawToken);
        }

        SecurityContextHolder.clearContext();
        log.info("User Logged out: "+concreteUser.getId());
        accessTimeService.storeAccessTime(concreteUser, AccessTimeType.LOGOUT);
    }
}
