package org.soumyadip.expensediary.repository;

import org.soumyadip.expensediary.entity.Role;
import org.soumyadip.expensediary.entity.User;
import org.soumyadip.expensediary.entity.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, String> {

    Optional<UserRole> findById(String id);
    @Query("""
    SELECT ur.role
    FROM UserRole ur
    WHERE ur.user = :user
    """)
    HashSet<Role> findAllRolesByUser(@Param("user") User user);
    Optional<UserRole> findByUserAndRole(User user, Role role);
}
