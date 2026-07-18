package org.soumyadip.expensediary.dto;

public record UpdateTransactionTypeRequest(
        String name,
        String description
) {
}
