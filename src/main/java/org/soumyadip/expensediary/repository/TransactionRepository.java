package org.soumyadip.expensediary.repository;

import org.soumyadip.expensediary.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.HashSet;
import java.util.Optional;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, String> {

    Optional<Transaction> findByTitle(String transactionName);
    Optional<Transaction> findById(String id);
}
