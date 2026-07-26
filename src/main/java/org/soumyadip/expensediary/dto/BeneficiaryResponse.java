package org.soumyadip.expensediary.dto;

import java.time.Instant;

public record BeneficiaryResponse(
        String id,
        String name,
        String description,
        String address,
        String phone,
        String email,
        boolean isActive,
        Instant activationTime,
        Instant deactivationTime
) {}
