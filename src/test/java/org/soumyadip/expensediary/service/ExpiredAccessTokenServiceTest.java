package org.soumyadip.expensediary.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.soumyadip.expensediary.entity.ExpiredAccessToken;
import org.soumyadip.expensediary.repository.ExpiredAccessTokenRepository;
import org.soumyadip.expensediary.util.JWTutil;

import java.time.Instant;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ExpiredAccessTokenServiceTest {
    @Mock JWTutil jwtutil;
    @Mock ExpiredAccessTokenRepository repository;
    @InjectMocks ExpiredAccessTokenService service;

    @Test
    void isExpiredLooksUpJwtClaimId() {
        when(jwtutil.getClaimID("token")).thenReturn("jti");
        when(repository.findExpiredToken("jti")).thenReturn(Optional.of(new ExpiredAccessToken()));

        assertTrue(service.isExpired("token"));
    }

    @Test
    void expireTokenPersistsJwtMetadata() {
        Instant expiry = Instant.parse("2026-01-01T00:00:00Z");
        when(jwtutil.getClaimID("token")).thenReturn("jti");
        when(jwtutil.getExpirationTime("token")).thenReturn(expiry);

        service.expireToken("token");

        ArgumentCaptor<ExpiredAccessToken> captor = ArgumentCaptor.forClass(ExpiredAccessToken.class);
        verify(repository).save(captor.capture());
        assertEquals("jti", captor.getValue().getJtiClaimId());
        assertEquals(expiry, captor.getValue().getExpiryTime());
    }
}
