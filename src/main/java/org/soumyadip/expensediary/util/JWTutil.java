package org.soumyadip.expensediary.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.soumyadip.expensediary.entity.User;
import org.soumyadip.expensediary.repository.UserRepository;
import org.soumyadip.expensediary.service.UlidGenerator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.Date;

@Slf4j
@Component
@RequiredArgsConstructor
public class JWTutil {

    private final UserRepository userRepository;
    private final UlidGenerator ulidGenerator;

    @Value("${jwt.secret}")
    private String secret;
    protected final long EXPIRATION_TIME = 60 * 60 * 1000;

    private SecretKey getSecretKey() {
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    public long getDefaultExpirationTime() {
        return EXPIRATION_TIME;
    }

    public String generateToken(String username) {

        log.info("Generating token for user {}", username);
        return Jwts.builder()
                .id(ulidGenerator.generate())
                .subject(username)
                .issuedAt(new Date())
                .issuer("ExpenseDiary")
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(getSecretKey())
                .compact();
    }

    public String getClaimID(String token) {

        Claims claims = extractClaims(token);

        log.info("Extracting claim ID");

        return claims.getId();
    }

    public String extractUsername(String token) {

        log.debug("Extracting username from token");
        return extractClaims(token).getSubject();
    }

    private Claims extractClaims(String token) {

        log.debug("Sending extracted claims.");
        return Jwts.parser()
                .verifyWith(getSecretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public boolean validateToken(String username, UserDetails userDetails, String token){

        log.debug("Validating Token");
        if(username.equals(userDetails.getUsername()) && !isExpired(token, username)) {

            log.debug("Token is valid.");
            return true;
        }

        log.debug("Token is not valid");
        return false;
    }

    public Instant getExpirationTime(String token) {
        return extractClaims(token).getExpiration().toInstant();
    }

    public boolean isExpired(String token, String username) {

        log.debug("Checking if token is expired");

        Claims claims = extractClaims(token);
        Instant passwordChangedAt = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"))
                .getPasswordChangedAt();
        if (claims.getExpiration().before(new Date())) {
            return true;
        }
        return passwordChangedAt != null && claims.getIssuedAt() != null
                && passwordChangedAt.isAfter(claims.getIssuedAt().toInstant());
    }
}
