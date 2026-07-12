package org.soumyadip.expensediary.service;

import lombok.RequiredArgsConstructor;
import org.soumyadip.expensediary.entity.Role;
import org.soumyadip.expensediary.exception.UsernameAlreadyExists;
import org.soumyadip.expensediary.repository.RoleRepository;
import org.springframework.stereotype.Service;

import java.util.Set;

@RequiredArgsConstructor
@Service
public class EasyUserCreationService {

    private final RoleRepository roleRepository;
    private final UserService userService;
    private final RoleService roleService;

    public void createRegularUser(String username, String password) throws UsernameAlreadyExists {

        Set<Role> roles = Set.of(roleRepository.findByNameIgnoreCase("USER").orElse(
                roleService.createRole("USER")
        ));
        userService.createUser(username, password, roles);
    }

    public void createAdminUser(String username, String password) throws UsernameAlreadyExists {

        Set<Role> roles = Set.of(roleRepository.findByNameIgnoreCase("ADMIN").orElse(
                roleService.createRole("ADMIN")
        ));
        userService.createUser(username, password, roles);
    }
}
