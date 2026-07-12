package org.soumyadip.expensediary.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.soumyadip.expensediary.dto.AuthRequest;
import org.soumyadip.expensediary.dto.AuthResponse;
import org.soumyadip.expensediary.service.AuthService;
import org.springframework.mock.web.MockHttpServletResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {
    @Mock AuthService authService;
    @InjectMocks AuthController controller;

    @Test
    void loginDelegatesToServiceAndReturnsItsToken() {
        AuthRequest request = new AuthRequest("sam", "password");
        AuthResponse authResponse = new AuthResponse("access-token");
        MockHttpServletResponse response = new MockHttpServletResponse();
        when(authService.login(request, response)).thenReturn(authResponse);

        var result = controller.login(request, response);

        assertEquals(200, result.getStatusCode().value());
        assertEquals(authResponse, result.getBody());
        verify(authService).login(request, response);
    }

    @Test
    void authCheckReturnsAuthenticatedMessage() {
        assertEquals("Authenticated !", controller.authCheck().getBody());
    }
}
