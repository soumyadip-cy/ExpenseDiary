package org.soumyadip.expensediary.dto;

import jakarta.validation.constraints.NotBlank;

public record ApiMessage(
        @NotBlank
        String message
) {}
