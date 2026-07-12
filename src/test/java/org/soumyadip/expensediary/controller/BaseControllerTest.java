package org.soumyadip.expensediary.controller;

import org.junit.jupiter.api.BeforeEach;
import org.soumyadip.expensediary.entity.ImplementedUserDetails;
import org.soumyadip.expensediary.filter.JwtAuthFilter;
import org.soumyadip.expensediary.service.UlidGenerator;
import org.soumyadip.expensediary.util.JWTutil;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.request.RequestPostProcessor;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;

abstract class BaseControllerTest {

    @MockitoBean
    protected UlidGenerator ulidGenerator;

    @MockitoBean
    private JwtAuthFilter jwtAuthFilter;

    @MockitoBean
    private JWTutil jwtutil;

    protected Authentication authentication;
    protected ImplementedUserDetails implementedUserDetails;

    @BeforeEach
    void setup() {

        authentication = mock(Authentication.class);
        implementedUserDetails = mock(ImplementedUserDetails.class);

        when(authentication.getPrincipal()).thenReturn(implementedUserDetails);

        when(implementedUserDetails.getUsername()).thenReturn("soumyadip");
    }

    protected RequestPostProcessor authenticatedUser() {
        return user("soumyadip").roles("ADMIN");
    }
}
