package org.soumyadip.expensediary.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.soumyadip.expensediary.entity.Role;
import org.soumyadip.expensediary.entity.User;
import org.soumyadip.expensediary.repository.RoleRepository;
import org.soumyadip.expensediary.repository.UserRepository;
import org.soumyadip.expensediary.repository.UserRoleRepository;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserInitializerServiceTest {
    @Mock UserRepository userRepository;
    @Mock RoleRepository roleRepository;
    @Mock UserRoleRepository userRoleRepository;
    @Mock UlidGenerator ulid;
    @Mock PasswordEncoder passwordEncoder;
    @InjectMocks UserInitializerService service;

    @Test
    void createsInitialAdminAndAssignsAdminRoleWhenMissing() {
        when(userRepository.findByUsername("admin")).thenReturn(Optional.empty());
        when(roleRepository.findByName("ADMIN")).thenReturn(Optional.empty());
        when(userRoleRepository.findByUserAndRole(any(), any())).thenReturn(Optional.empty());
        when(ulid.generate()).thenReturn("user-id", "role-id", "assignment-id");
        when(passwordEncoder.encode(any())).thenReturn("encoded");

        service.CreateInitialAdmin();

        verify(userRepository).save(any(User.class));
        verify(roleRepository).save(any(Role.class));
        verify(userRoleRepository).save(any());
    }
}
