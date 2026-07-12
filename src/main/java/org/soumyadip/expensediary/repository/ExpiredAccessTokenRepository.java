package org.soumyadip.expensediary.repository;

import org.soumyadip.expensediary.entity.ExpiredAccessToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ExpiredAccessTokenRepository extends JpaRepository<ExpiredAccessToken, String> {

    @Query("""
    SELECT t
    FROM ExpiredAccessToken t
    WHERE t.jtiClaimId = :token_id
    """)
    Optional<ExpiredAccessToken> findExpiredToken(@Param("token_id") String tokenId);
}
