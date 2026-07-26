package org.soumyadip.expensediary.repository;

import org.soumyadip.expensediary.entity.TransactionType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.HashSet;
import java.util.Optional;

@Repository
public interface TransactionTypeRepository extends JpaRepository<TransactionType, String> {

    Optional<TransactionType> findById(String id);
    Optional<TransactionType> findByName(String name);
    Page<TransactionType> findAll(Pageable pageable);
}
