package org.soumyadip.expensediary.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.soumyadip.expensediary.dto.UserInfo;
import org.soumyadip.expensediary.entity.ImplementedUserDetails;
import org.soumyadip.expensediary.service.EasyUserCreationService;
import org.soumyadip.expensediary.service.UserService;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {
    @Mock UserService userService;
    @Mock EasyUserCreationService easyUserCreationService;
    @InjectMocks UserController controller;

    @Test
    void profileUsesAuthenticatedUsername() {
        ImplementedUserDetails principal = new ImplementedUserDetails("sam", "password", List.of());
        UserInfo userInfo = new UserInfo("sam", true, Set.of("USER"));
        when(userService.getUser("sam")).thenReturn(userInfo);

        var response = controller.getSelfProfile(principal);

        assertEquals(200, response.getStatusCode().value());
        assertTrue(response.getBody().success());
        assertEquals(userInfo, response.getBody().data());
        verify(userService).getUser("sam");
    }
}
