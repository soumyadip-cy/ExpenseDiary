package org.soumyadip.expensediary.dto;

import jakarta.validation.constraints.NotBlank;

public record CreateTransactionTypeRequest(
        @NotBlank String name,
        String description
) {}
