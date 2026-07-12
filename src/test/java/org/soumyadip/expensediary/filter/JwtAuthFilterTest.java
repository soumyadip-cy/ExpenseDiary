package org.soumyadip.expensediary.filter;

import jakarta.servlet.FilterChain;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.soumyadip.expensediary.entity.ImplementedUserDetails;
import org.soumyadip.expensediary.service.ExpiredAccessTokenService;
import org.soumyadip.expensediary.service.ImplementedUserDetailsService;
import org.soumyadip.expensediary.util.JWTutil;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JwtAuthFilterTest {
    @Mock JWTutil jwtutil;
    @Mock ImplementedUserDetailsService userDetailsService;
    @Mock ExpiredAccessTokenService expiredAccessTokenService;
    @Mock FilterChain chain;
    @InjectMocks JwtAuthFilter filter;

    @AfterEach void clearSecurityContext() { SecurityContextHolder.clearContext(); }

    @Test
    void revokedTokenDoesNotAuthenticateRequest() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer token");
        when(jwtutil.extractUsername("token")).thenReturn("sam");
        ImplementedUserDetails details = new ImplementedUserDetails("sam", "password", List.of());
        when(userDetailsService.loadUserByUsername("sam")).thenReturn(details);
        when(jwtutil.validateToken("sam", details, "token")).thenReturn(true);
        when(expiredAccessTokenService.isExpired("token")).thenReturn(true);

        filter.doFilter(request, new MockHttpServletResponse(), chain);

        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verify(chain).doFilter(any(), any());
    }
}
