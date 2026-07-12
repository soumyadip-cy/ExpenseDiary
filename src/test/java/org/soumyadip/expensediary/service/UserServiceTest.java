package org.soumyadip.expensediary.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.soumyadip.expensediary.dto.UserInfo;
import org.soumyadip.expensediary.entity.Role;
import org.soumyadip.expensediary.entity.User;
import org.soumyadip.expensediary.exception.PasswordMismatchException;
import org.soumyadip.expensediary.exception.UsernameAlreadyExists;
import org.soumyadip.expensediary.repository.RoleRepository;
import org.soumyadip.expensediary.repository.UserRepository;
import org.soumyadip.expensediary.repository.UserRoleRepository;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock UserRepository userRepository;
    @Mock RoleRepository roleRepository;
    @Mock UserRoleRepository userRoleRepository;
    @Mock PasswordEncoder passwordEncoder;
    @Mock UlidGenerator ulid;
    @Mock AccessTimeService accessTimeService;
    @InjectMocks UserService userService;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User("id-1", "sam", "encoded", true, null);
    }

    @Test
    void createUserSavesEncodedUserAndRoleAssignment() {
        Role role = new Role("role-id", "USER", true);
        when(userRepository.findByUsername("sam")).thenReturn(Optional.empty());
        when(ulid.generate()).thenReturn("user-id", "user-role-id");
        when(passwordEncoder.encode("Password1!")).thenReturn("encoded");
        when(roleRepository.findByNameIgnoreCase("USER")).thenReturn(Optional.of(role));

        userService.createUser("sam", "Password1!", Set.of(role));

        verify(userRepository).save(argThat(saved -> saved.getUsername().equals("sam") && saved.getPassword().equals("encoded")));
        verify(userRoleRepository).save(any());
    }

    @Test
    void createUserRejectsDuplicateUsername() {
        when(userRepository.findByUsername("sam")).thenReturn(Optional.of(user));
        assertThrows(UsernameAlreadyExists.class, () -> userService.createUser("sam", "Password1!", Set.of()));
        verify(userRepository, never()).save(any());
    }

    @Test
    void getUserReturnsRoles() {
        Role role = new Role("role-id", "USER", true);
        when(userRepository.findByUsername("sam")).thenReturn(Optional.of(user));
        when(userRoleRepository.findAllRolesByUser(user)).thenReturn(new HashSet<>(Set.of(role)));

        UserInfo result = userService.getUser("sam");

        assertEquals("sam", result.username());
        assertEquals(Set.of("USER"), result.roles());
    }

    @Test
    void changePasswordUpdatesPasswordAndTimestamp() {
        when(userRepository.findByUsername("sam")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("old", "encoded")).thenReturn(true);
        when(passwordEncoder.matches("new", "encoded")).thenReturn(false);
        when(passwordEncoder.encode("new")).thenReturn("new-encoded");

        userService.changePassword("sam", "old", "new");

        assertEquals("new-encoded", user.getPassword());
        assertNotNull(user.getPasswordChangedAt());
        verify(userRepository).save(user);
    }

    @Test
    void changePasswordRejectsWrongOldPassword() {
        when(userRepository.findByUsername("sam")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("wrong", "encoded")).thenReturn(false);
        assertThrows(PasswordMismatchException.class, () -> userService.changePassword("sam", "wrong", "new"));
    }

    @Test
    void administratorDeleteUsesUsernamePathParameter() {
        User target = new User("id-2", "target", "password", true, null);
        when(userRepository.findByUsername("target")).thenReturn(Optional.of(target));

        userService.deleteUser("admin", "target");

        assertFalse(target.isUserIsActive());
        verify(userRepository).save(target);
    }
}
