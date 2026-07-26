package org.soumyadip.expensediary.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UpdateBeneficiaryRequest(
        @NotBlank(message = "Name is required") String name,
        String description,
        String address,
        String phone,
        @Email String email
) {}
