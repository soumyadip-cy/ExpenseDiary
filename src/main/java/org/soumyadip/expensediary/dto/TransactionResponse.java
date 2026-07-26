package org.soumyadip.expensediary.dto;

import java.math.BigDecimal;
import java.time.Instant;

public record TransactionResponse(
        String id,
        Instant transactionTime,
        Instant creationTime,
        String transactionTypeName,
        String transactionTypeId,
        String beneficiaryName,
        String beneficiaryId,
        String merchantName,
        String merchantId,
        String title,
        String description,
        BigDecimal amount
) {}
