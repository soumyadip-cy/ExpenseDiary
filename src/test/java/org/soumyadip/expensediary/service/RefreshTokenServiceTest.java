package org.soumyadip.expensediary.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.soumyadip.expensediary.entity.RefreshToken;
import org.soumyadip.expensediary.entity.User;
import org.soumyadip.expensediary.exception.RefreshTokenExpiredException;
import org.soumyadip.expensediary.repository.RefreshTokenRepository;
import org.soumyadip.expensediary.repository.UserRepository;

import java.time.ZonedDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RefreshTokenServiceTest {
    @Mock RefreshTokenRepository repository;
    @Mock UserRepository userRepository;
    @Mock UlidGenerator ulid;
    @InjectMocks RefreshTokenService service;

    @Test
    void createRefreshTokenDeletesPreviousTokenAndSavesNewOne() {
        User user = new User("id", "sam", "password", true, null);
        when(ulid.generate()).thenReturn("new-id");
        when(repository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        RefreshToken token = service.createRefreshToken(user);

        assertEquals("new-id", token.getId());
        assertEquals(user, token.getUser());
        assertNotNull(token.getRefreshToken());
        verify(repository).deleteByUser(user);
    }

    @Test
    void validateRefreshTokenDeletesExpiredToken() {
        RefreshToken token = RefreshToken.builder().expiryDate(ZonedDateTime.now().minusMinutes(1)).build();
        when(repository.findByRefreshToken("expired")).thenReturn(Optional.of(token));

        assertThrows(RefreshTokenExpiredException.class, () -> service.validateRefreshToken("expired"));
        verify(repository).delete(token);
    }
}
