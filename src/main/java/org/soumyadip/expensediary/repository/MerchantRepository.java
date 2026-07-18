package org.soumyadip.expensediary.repository;

import org.soumyadip.expensediary.entity.Merchant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MerchantRepository extends JpaRepository<Merchant, String> {

    Optional<Merchant> findById(String Id);
    Optional<Merchant> findByName(String name);
}
