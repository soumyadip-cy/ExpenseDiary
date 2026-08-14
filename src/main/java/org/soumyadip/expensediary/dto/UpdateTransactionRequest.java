package org.soumyadip.expensediary.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.Instant;

public record UpdateTransactionRequest(
        Instant transactionTime,
        String transactionTypeId,
        String beneficiaryId,
        String merchantId,
        String title,
        String description,
        BigDecimal amount
) {}
