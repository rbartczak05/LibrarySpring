package pl.lodz.p.user.adapters.rest.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterRequest(
        @NotBlank @Size(min = 3, max = 20) String login,
        @NotBlank @Size(min = 5) String password,
        @NotBlank @Email @Size(min = 3, max = 100) String email,
        @NotBlank String firstName,
        @NotBlank String lastName,
        @Min(1) int age,
        @NotBlank String accessLevel
) {
}