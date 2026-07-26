package org.soumyadip.expensediary.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.Instant;

public record CreateTransactionRequest(
        @NotNull Instant transactionTime,
        @NotBlank String transactionTypeId,
        @NotBlank String beneficiaryId,
        @NotBlank String merchantId,
        @NotBlank String title,
        String description,
        @NotNull BigDecimal amount
) {}
