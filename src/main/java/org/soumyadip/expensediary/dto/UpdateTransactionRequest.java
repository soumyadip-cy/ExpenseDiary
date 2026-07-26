package org.soumyadip.expensediary.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.Instant;

public record UpdateTransactionRequest(
        @NotBlank Instant transactionTime,
        @NotNull String transactionTypeId,
        @NotNull String beneficiaryId,
        @NotNull String merchantId,
        @NotBlank String title,
        String description,
        @NotNull BigDecimal amount
) {}
