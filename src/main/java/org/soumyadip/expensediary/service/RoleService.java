package org.soumyadip.expensediary.service;

import lombok.RequiredArgsConstructor;
import org.soumyadip.expensediary.entity.Role;
import org.soumyadip.expensediary.entity.User;
import org.soumyadip.expensediary.repository.RoleRepository;
import org.soumyadip.expensediary.repository.UserRepository;
import org.soumyadip.expensediary.repository.UserRoleRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.management.relation.RoleInfoNotFoundException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class RoleService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserRoleRepository userRoleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UlidGenerator ulid;

    public Role createRole(String roleName){
        Optional<Role> role = roleRepository.findByNameIgnoreCase(roleName);
        if(role.isPresent())
            return role.get();
        Role newRole = new Role(
                ulid.generate(),
                roleName,
                true
        );
        roleRepository.save(newRole);
        return newRole;
    }

    public Role updateRole(String oldRoleName, String newRoleName) throws RoleInfoNotFoundException {
        Optional<Role> role = roleRepository.findByNameIgnoreCase(oldRoleName);
        if(role.isPresent()){
            role.get().setName(newRoleName);
            roleRepository.save(role.get());
            return role.get();
        } else
            return null;
    }

    public HashSet<Role> getAllRoles(User user){
        return userRoleRepository.findAllRolesByUser(user);
    }
}
