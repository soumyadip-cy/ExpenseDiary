package org.soumyadip.expensediary.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.soumyadip.expensediary.entity.Role;
import org.soumyadip.expensediary.entity.User;
import org.soumyadip.expensediary.entity.UserRole;
import org.soumyadip.expensediary.repository.RoleRepository;
import org.soumyadip.expensediary.repository.UserRepository;
import org.soumyadip.expensediary.repository.UserRoleRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserInitializerService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserRoleRepository userRoleRepository;
    private final UlidGenerator ulid;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void CreateInitialAdmin() {
        User user;
        Role role;
        UserRole userRole;

        if(userRepository.findByUsername("admin").isEmpty()) {
            user = new User(
                    ulid.generate(),
                    "admin",
                    passwordEncoder.encode("@dmin$99")
            );
            userRepository.save(user);
            log.info("Default user created");
        } else
            user = userRepository.findByUsername("admin").orElse(null);

        if(roleRepository.findByName("ADMIN").isEmpty()) {

            role = new Role(
                    ulid.generate(),
                    "ADMIN"
            );
            roleRepository.save(role);
            log.info("ADMIN role created");
        } else
            role = roleRepository.findByName("ADMIN").orElse(null);

        if(userRoleRepository.findByUserAndRole(user, role).isEmpty()) {

            userRole = new UserRole(
                    ulid.generate(),
                    user,
                    role
            );
            userRoleRepository.save(userRole);
            log.info("Role assigned to default user");
        } else
            userRole = userRoleRepository.findByUserAndRole(user, role).orElse(null);
    }
}
