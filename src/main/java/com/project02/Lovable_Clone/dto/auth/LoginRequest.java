package com.project02.Lovable_Clone.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record LoginRequest(
        @NotBlank @Email String username,
        @Size(min = 4, max = 50) String password
) {
}
