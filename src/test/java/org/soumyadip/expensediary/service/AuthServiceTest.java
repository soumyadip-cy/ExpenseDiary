package org.soumyadip.expensediary.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.soumyadip.expensediary.dto.AuthRequest;
import org.soumyadip.expensediary.entity.ImplementedUserDetails;
import org.soumyadip.expensediary.entity.RefreshToken;
import org.soumyadip.expensediary.entity.User;
import org.soumyadip.expensediary.enums.AccessTimeType;
import org.soumyadip.expensediary.repository.UserRepository;
import org.soumyadip.expensediary.util.JWTutil;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {
    @Mock UserRepository userRepository;
    @Mock JWTutil jwtutil;
    @Mock RefreshTokenService refreshTokenService;
    @Mock AuthenticationManager authenticationManager;
    @Mock ExpiredAccessTokenService expiredAccessTokenService;
    @Mock AccessTimeService accessTimeService;
    @InjectMocks AuthService service;

    @AfterEach void clearContext() { SecurityContextHolder.clearContext(); }

    @Test
    void loginAuthenticatesCreatesTokensAndStoresLoginTime() {
        User user = new User("id", "sam", "encoded", true, null);
        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(new ImplementedUserDetails("sam", "encoded", List.of()));
        when(authenticationManager.authenticate(any())).thenReturn(authentication);
        when(userRepository.findByUsername("sam")).thenReturn(Optional.of(user));
        when(jwtutil.generateToken("sam")).thenReturn("access-token");
        when(refreshTokenService.createRefreshToken(user)).thenReturn(new RefreshToken("id", "refresh-token", ZonedDateTime.now().plusDays(7), user));
        MockHttpServletResponse response = new MockHttpServletResponse();

        var result = service.login(new AuthRequest("sam", "password"), response);

        assertEquals("access-token", result.accessToken());
        assertTrue(response.getHeader("Set-Cookie").contains("refreshToken=refresh-token"));
        verify(accessTimeService).storeAccessTime(user, AccessTimeType.LOGIN);
    }
}
