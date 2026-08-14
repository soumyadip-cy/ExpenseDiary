package org.soumyadip.expensediary.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UpdateMerchantRequest(
        String name,
        String description,
        String address,
        String phone,
        @Email String email
) {}
