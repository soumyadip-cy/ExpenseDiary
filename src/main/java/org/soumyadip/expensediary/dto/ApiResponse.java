package org.soumyadip.expensediary.dto;

import java.time.Instant;

public record ApiResponse<T>(
        boolean success,
        T data,
        int status,
        Instant timestamp
) {}
