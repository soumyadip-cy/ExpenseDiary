package org.soumyadip.expensediary.service;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.soumyadip.expensediary.entity.ImplementedUserDetails;
import org.soumyadip.expensediary.entity.Role;
import org.soumyadip.expensediary.entity.User;
import org.soumyadip.expensediary.entity.UserRole;
import org.soumyadip.expensediary.repository.RoleRepository;
import org.soumyadip.expensediary.repository.UserRepository;
import org.soumyadip.expensediary.repository.UserRoleRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;

@RequiredArgsConstructor
@Service
public class ImplementedUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;
    private final RoleRepository roleRepository;

    @Transactional
    @Override
    public UserDetails loadUserByUsername(@NonNull String username) throws UsernameNotFoundException {

        User plainUser = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(username+" not found."));
        HashSet<GrantedAuthority> grantedAuthorities = new HashSet<>();

        for(UserRole userRole : userRoleRepository.findAllByUser(plainUser)) {

            Role role = roleRepository
                    .findById(userRole.getRole().getId())
                    .orElseThrow(() -> new RuntimeException(username+" has no role."));

            grantedAuthorities.add(new SimpleGrantedAuthority(
                        role.getName()
                    ));
        }

        return new ImplementedUserDetails(username, plainUser.getPassword(), grantedAuthorities);
    }
}
