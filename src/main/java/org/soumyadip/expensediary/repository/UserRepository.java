package org.soumyadip.expensediary.repository;

import org.soumyadip.expensediary.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

    Optional<User> findById(String id);
    Optional<User> findByUsername(String username);
}
