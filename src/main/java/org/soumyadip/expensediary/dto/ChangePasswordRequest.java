package org.soumyadip.expensediary.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record ChangePasswordRequest (

        @NotBlank(message = "Old password is required!")
        String oldPassword,

        @NotBlank(message = "New password is required!")
        @Pattern(
                regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=!]).{8,64}$",
                message = "Password must contain at least one uppercase, lowercase, number," +
                        "symbol character and be of at least 8 characters."
        )
        String newPassword
) {}
