package org.soumyadip.expensediary.dto;

public record TransactionTypeResponse(
        String id,
        String name,
        String description
) {
}
