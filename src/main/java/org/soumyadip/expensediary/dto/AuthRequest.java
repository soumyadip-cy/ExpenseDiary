package org.soumyadip.expensediary.dto;

public record AuthRequest(
    String username,
    String password
) {}