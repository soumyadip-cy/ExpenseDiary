package org.soumyadip.expensediary.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.soumyadip.expensediary.entity.ExpiredAccessToken;
import org.soumyadip.expensediary.exception.JwtExpireTokenCreationException;
import org.soumyadip.expensediary.repository.ExpiredAccessTokenRepository;
import org.soumyadip.expensediary.util.JWTutil;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;

@Slf4j
@RequiredArgsConstructor
@Service
public class ExpiredAccessTokenService {

    private final JWTutil jwtutil;
    private final ExpiredAccessTokenRepository expiredAccessTokenRepository;

    public boolean isExpired(String accessToken) {

        return expiredAccessTokenRepository.findExpiredToken(jwtutil.getClaimID(accessToken)).isPresent();
    }

    public void expireToken(String accessToken) {

        ExpiredAccessToken expiredAccessToken;

        log.debug("claimID: {}", jwtutil.getClaimID(accessToken));
        String claimID = jwtutil.getClaimID(accessToken);
        try {
            expiredAccessToken = ExpiredAccessToken.builder()
                .jtiClaimId(claimID)
                .expiryTime(jwtutil.getExpirationTime(accessToken))
                .build();
        } catch (DataAccessException e) {
            throw new JwtExpireTokenCreationException(e.getMessage());
        }

        log.debug("expiredAccessToken.id: {} | claimID: {}", expiredAccessToken.getJtiClaimId(), jwtutil.getClaimID(accessToken));

        expiredAccessTokenRepository.save(expiredAccessToken);
    }

}
