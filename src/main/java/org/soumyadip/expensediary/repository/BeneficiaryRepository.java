package org.soumyadip.expensediary.repository;

import org.soumyadip.expensediary.entity.Beneficiary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BeneficiaryRepository extends JpaRepository<Beneficiary, String> {

    Optional<Beneficiary> findById(String id);
    Optional<Beneficiary> findByName(String name);
}
