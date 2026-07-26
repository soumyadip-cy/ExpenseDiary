package org.soumyadip.expensediary.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.soumyadip.expensediary.dto.AccessTimeDTO;
import org.soumyadip.expensediary.dto.AccessTimes;
import org.soumyadip.expensediary.dto.UserInfo;
import org.soumyadip.expensediary.entity.*;
import org.soumyadip.expensediary.enums.AccessTimeType;
import org.soumyadip.expensediary.exception.*;
import org.soumyadip.expensediary.repository.RoleRepository;
import org.soumyadip.expensediary.repository.UserRepository;
import org.soumyadip.expensediary.repository.UserRoleRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.management.relation.RoleInfoNotFoundException;
import java.util.*;
import java.time.Instant;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserRoleRepository userRoleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UlidGenerator ulid;
    private final AccessTimeService accessTimeService;
    private final RoleService roleService;

    @Transactional
    public void createUser(String username, String password, Set<Role> roles) {

        if(userRepository.findByUsername(username).isPresent()){
            throw new UsernameAlreadyExists("Username already exists !");
        }

        User user = new User(
                ulid.generate(),
                username,
                passwordEncoder.encode(password),
                true,
                null
        );

        try {
            userRepository.save(user);
            log.info("User created: {}", user);
        } catch (DataIntegrityViolationException e) {
            throw new UsernameAlreadyExists("Username already exists ! Ex:" + e);
        } catch (Exception e) {
            throw new UserCreationException("User creation failed! Ex:" + e);
        }

        try {
            for(Role role : roles) {
                Optional<Role> roleEntity = roleRepository.findByNameIgnoreCase(role.getName());
                if(roleEntity.isPresent()){
                    userRoleRepository.save(new UserRole(
                            ulid.generate(),
                            user,
                            roleEntity.get()
                    ));
                } else
                    throw new RoleInfoNotFoundException("Role not found!");
            }
        } catch (DataIntegrityViolationException e) {
            throw new UsernameAlreadyExists("User already has the role ! Ex:" + e);
        } catch (Exception e) {
            throw new UserCreationException("Role assignment failed! Ex:" + e);
        }
    }

    @Transactional
    public boolean deleteUser(String username){

        if(userRepository.findByUsername(username).isPresent()){

            User user = userRepository.findByUsername(username).get();
            user.setUserIsActive(false);
            userRepository.save(user);
            log.info("User deleted: {}", username);
            return true;
        }
        return false;
    }

    public UserInfo getUser(String username){
        User user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found!"));
        Set<String> roleSet = userRoleRepository.findAllRolesByUser(user)
                .stream()
                .map(Role::getName)
                .collect(Collectors.toSet());

        UserInfo userInfo = new UserInfo(
                user.getUsername(),
                user.isUserIsActive(),
                roleSet
        );
        log.info("User info: {}", userInfo);
        return userInfo;
    }

    @Transactional
    public void changePassword(String username, String oldPassword, String newPassword){

        User user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found!"));

        if(!passwordEncoder.matches(oldPassword, user.getPassword())){
            log.warn("Old password mismatch for user: {}", username);
            throw new PasswordMismatchException("Old Password does not match!");
        }

        if(passwordEncoder.matches(newPassword, user.getPassword())){
            log.warn("New password matches existing password for user: {}",username);
            throw new IllegalArgumentException("New password cannot be the same as the old password!");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        user.setPasswordChangedAt(Instant.now());
        userRepository.save(user);
        log.info("Password changed successfully for user: {}", username);

    }

    @Transactional
    public void editUsername(String username, String newUsername) {
        if(userRepository.findByUsername(username).isPresent()){
            User user = userRepository.findByUsername(username).get();
            if(userRepository.findByUsername(newUsername).isPresent()){
                throw new UsernameAlreadyExists("Username already exists ! Choose a different username.");
            }
            user.setUsername(newUsername);
            userRepository.save(user);
            log.info("New username changed for user: {}", user.getUsername());
        }
    }

    @Transactional
    public void deleteUser(String currentUser, String userIdToBeDeleted) {

        User user = userRepository.findById(userIdToBeDeleted).orElseThrow(() -> new UserNotFoundException("User not found!"));
        User performingUser = userRepository.findByUsername(currentUser).orElseThrow(() -> new UserNotFoundException("User not found!"));

        if(user.getUsername().equals(currentUser)){
            throw new IllegalArgumentException("User cannot delete itself!");
        }

        if((roleService.getAllRoles(user)
                .contains(roleRepository.findByNameIgnoreCase("SUPERADMIN")) ||
                roleService.getAllRoles(user)
                        .contains(roleRepository.findByNameIgnoreCase("ADMIN"))) &&
                !roleService.getAllRoles(performingUser)
                        .contains(roleRepository.findByNameIgnoreCase("SUPERADMIN"))) {
            throw new UserDeleteException("SuperAdmin and Admin users can only be deleted by SuperAdmin!");
        }

        user.setUserIsActive(false);

        //Unnecessary due to hibernate's dirty checking
        //userRepository.save(user);

        log.info("User id: {} deactivated by user: {}", userIdToBeDeleted, userRepository.findByUsername(currentUser).get().getId());
    }

    @Transactional
    public AccessTimes getAccessTimes() {

        log.debug("getAccessTimes() accessed");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Optional<User> user = Optional.empty();
        User concreteUser;
        ArrayList<AccessTimeDTO> accessTimes = new ArrayList<>();

        if(authentication != null && authentication.isAuthenticated()) {

            user = userRepository.findByUsername(authentication.getName());
            if(user.isPresent()) {

                concreteUser = user.get();
                accessTimeService
                        .getAccessTimeList(concreteUser, 0)
                        .stream().forEach(accessTime -> accessTimes
                                .add(new AccessTimeDTO(accessTime.type(), accessTime.timestamp())));

                log.info("Access times fetched successfully !");
                return new AccessTimes(accessTimes);
            }
        }
        return new AccessTimes(accessTimes);
    }

}
