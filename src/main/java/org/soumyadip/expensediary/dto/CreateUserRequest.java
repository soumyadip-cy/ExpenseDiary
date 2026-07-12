package org.soumyadip.expensediary.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record CreateUserRequest (

        @NotBlank(message = "Email is required!")
        String username,

        @NotBlank(message = "Password is required!")
        @Size(min = 8, max = 64)
        @Pattern(
                regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=!]).{8,64}$",
                message = "Password must contain at least one uppercase, lowercase, number," +
                        "symbol character and be of at least 8 characters."
        )
        String password
) {}
