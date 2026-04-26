package org.soumyadip.expensediary.repository;

import org.soumyadip.expensediary.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, String> {

    Optional<Role> findById(String id);
    Optional<Role> findByName(String name);
}
