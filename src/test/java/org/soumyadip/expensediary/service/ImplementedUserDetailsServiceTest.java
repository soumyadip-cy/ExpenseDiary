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

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ImplementedUserDetailsServiceTest {
    @Mock UserRepository userRepository;
    @Mock UserRoleRepository userRoleRepository;
    @Mock RoleRepository roleRepository;
    @InjectMocks ImplementedUserDetailsService service;

    @Test
    void loadUserMapsRolesToAuthorities() {
        User user = new User("id", "sam", "encoded", true, null);
        when(userRepository.findByUsername("sam")).thenReturn(java.util.Optional.of(user));
        when(userRoleRepository.findAllRolesByUser(user)).thenReturn(new HashSet<>(Set.of(new Role("r", "ROLE_USER", true))));

        var details = service.loadUserByUsername("sam");

        assertEquals("sam", details.getUsername());
        assertTrue(details.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_USER")));
    }
}
