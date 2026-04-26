package org.soumyadip.expensediary.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.soumyadip.expensediary.entity.RefreshToken;
import org.soumyadip.expensediary.entity.User;
import org.soumyadip.expensediary.exception.InvalidRefreshTokenException;
import org.soumyadip.expensediary.exception.RefreshTokenExpiredException;
import org.soumyadip.expensediary.repository.RefreshTokenRepository;
import org.soumyadip.expensediary.repository.UserRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.ZonedDateTime;
import java.util.Base64;

@Service
@RequiredArgsConstructor
@Slf4j
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;
    private final UlidGenerator ulid;

    private String generateToken() {
        byte[] bytes = new byte[32];
        SecureRandom random = new SecureRandom();
        random.nextBytes(bytes);

        return Base64.getUrlEncoder()
                .withoutPadding()
                .encodeToString(bytes);
    }

    @Transactional
    public RefreshToken createRefreshToken(User user) {

        refreshTokenRepository.deleteByUser(user);

        RefreshToken token = RefreshToken.builder()
                .id(ulid.generate())
                .refreshToken(generateToken())
                .expiryDate(ZonedDateTime.now().plusDays(7))
                .user(user)
                .build();

        log.debug("Created refresh token for user={}", user.getUsername());
        return refreshTokenRepository.save(token);
    }

    @Transactional
    public RefreshToken validateRefreshToken(String refreshToken) throws InvalidRefreshTokenException, RefreshTokenExpiredException {

        RefreshToken token = refreshTokenRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new InvalidRefreshTokenException("Invalid refresh token !"));

        if (token.getExpiryDate().isBefore(ZonedDateTime.now())) {
            refreshTokenRepository.delete(token);
            throw new RefreshTokenExpiredException("Expired refresh token !");
        }

        return token;
    }

    @Transactional
    public RefreshToken rotateRefreshToken(String refreshToken) throws InvalidRefreshTokenException, RefreshTokenExpiredException {

        RefreshToken oldToken = validateRefreshToken(refreshToken);

        refreshTokenRepository.delete(oldToken);

        log.debug("Rotated refresh token");

        return createRefreshToken(oldToken.getUser());
    }
}
