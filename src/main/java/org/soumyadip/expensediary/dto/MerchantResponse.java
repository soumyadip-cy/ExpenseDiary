package org.soumyadip.expensediary.dto;

import java.time.Instant;

public record MerchantResponse(
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
