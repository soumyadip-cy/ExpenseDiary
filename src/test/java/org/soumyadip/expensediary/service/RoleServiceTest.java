package org.soumyadip.expensediary.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.soumyadip.expensediary.entity.Role;
import org.soumyadip.expensediary.repository.RoleRepository;
import org.soumyadip.expensediary.repository.UserRepository;
import org.soumyadip.expensediary.repository.UserRoleRepository;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RoleServiceTest {
    @Mock UserRepository userRepository;
    @Mock RoleRepository roleRepository;
    @Mock UserRoleRepository userRoleRepository;
    @Mock PasswordEncoder passwordEncoder;
    @Mock UlidGenerator ulid;
    @InjectMocks RoleService service;

    @Test
    void createRoleReturnsExistingRoleWithoutSaving() {
        Role role = new Role("id", "USER", true);
        when(roleRepository.findByNameIgnoreCase("USER")).thenReturn(Optional.of(role));

        assertSame(role, service.createRole("USER"));
        verify(roleRepository, never()).save(any());
    }

    @Test
    void createRoleGeneratesAndSavesMissingRole() {
        when(roleRepository.findByNameIgnoreCase("USER")).thenReturn(Optional.empty());
        when(ulid.generate()).thenReturn("role-id");

        Role result = service.createRole("USER");

        assertEquals("USER", result.getName());
        verify(roleRepository).save(result);
    }
}
