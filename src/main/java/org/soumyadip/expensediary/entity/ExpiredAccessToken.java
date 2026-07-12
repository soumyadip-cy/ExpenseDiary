package org.soumyadip.expensediary.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "expired_access_tokens")
public class ExpiredAccessToken {

    @Id
    @Column(unique = true, nullable = false, name = "jti_claim_id")
    private String jtiClaimId;

    @Column(nullable = false, name = "expiry_time")
    private Instant expiryTime;
}
