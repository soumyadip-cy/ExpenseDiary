package org.soumyadip.expensediary.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.soumyadip.expensediary.entity.Role;
import org.soumyadip.expensediary.repository.RoleRepository;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EasyUserCreationServiceTest {
    @Mock RoleRepository roleRepository;
    @Mock UserService userService;
    @Mock RoleService roleService;
    @InjectMocks EasyUserCreationService service;

    @Test
    void regularUserUsesExistingUserRole() {
        Role userRole = new Role("role-id", "USER", true);
        when(roleRepository.findByNameIgnoreCase("USER")).thenReturn(Optional.of(userRole));

        service.createRegularUser("sam", "password");

        verify(userService).createUser(eq("sam"), eq("password"), eq(java.util.Set.of(userRole)));
        // Optional.orElse evaluates its argument eagerly, so this collaborator is invoked
        // even when the repository already supplies the role.
        verify(roleService).createRole("USER");
    }

    @Test
    void adminUserCreatesMissingAdminRole() {
        Role adminRole = new Role("role-id", "ADMIN", true);
        when(roleRepository.findByNameIgnoreCase("ADMIN")).thenReturn(Optional.empty());
        when(roleService.createRole("ADMIN")).thenReturn(adminRole);

        service.createAdminUser("admin", "password");

        verify(userService).createUser(eq("admin"), eq("password"), eq(java.util.Set.of(adminRole)));
    }
}
