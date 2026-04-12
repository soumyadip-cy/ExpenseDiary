package org.soumyadip.expensediary.repository;

import org.soumyadip.expensediary.entity.Role;
import org.soumyadip.expensediary.entity.User;
import org.soumyadip.expensediary.entity.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRoleRepository extends JpaRepository<UserRole, String> {

    Optional<UserRole> findById(String id);
    List<UserRole> findAllByUser(User user);
    Optional<UserRole> findByUserAndRole(User user, Role role);
}
