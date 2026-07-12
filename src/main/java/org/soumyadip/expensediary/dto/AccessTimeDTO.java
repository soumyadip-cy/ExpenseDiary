package org.soumyadip.expensediary.dto;

import jakarta.validation.constraints.NotNull;
import org.soumyadip.expensediary.enums.AccessTimeType;

import java.time.Instant;

public record AccessTimeDTO(
        @NotNull
        AccessTimeType type,
        @NotNull
        Instant timestamp
) {
}
