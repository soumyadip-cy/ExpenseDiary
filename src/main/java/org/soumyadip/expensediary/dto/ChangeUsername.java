package org.soumyadip.expensediary.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

public record ChangeUsername(
        @NotBlank(message = "Email required")
        String username
) {}
