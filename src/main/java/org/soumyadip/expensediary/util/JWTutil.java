package org.soumyadip.expensediary.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Slf4j
@Component
@RequiredArgsConstructor
public class JWTutil {

    @Value("${jwt.secret}")
    private String secret;
    private final long EXPIRATION_TIME = 60 * 60 * 1000;

    private SecretKey getSecretKey() {
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    public String generateToken(String username) {

        log.info("Generating token for user {}", username);
        return Jwts.builder()
                .subject(username)
                .issuedAt(new Date())
                .issuer("ExpenseDiary")
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(getSecretKey())
                .compact();
    }

    public String extractUsername(String token) {

        log.debug("Extracting username from token");
        return extractClaims(token).getSubject();
    }

    public Claims extractClaims(String token) {

        log.debug("Sending extracted claims.");
        return Jwts.parser()
                .verifyWith(getSecretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public boolean validateToken(String username, UserDetails userDetails, String token) {

        log.debug("Validating Token");
        if(username.equals(userDetails.getUsername()) && !isExpired(token)) {

            log.debug("Token is valid.");
            return true;
        }

        log.debug("Token is not valid");
        return false;
    }

    public boolean isExpired(String token) {

        log.debug("Checking if token is expired");
        return extractClaims(token).getExpiration().before(new Date());
    }
}
