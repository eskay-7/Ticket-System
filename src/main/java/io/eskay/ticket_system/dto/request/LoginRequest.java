package io.eskay.ticket_system.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record LoginRequest(
        @NotBlank(message = "email cannot be empty")
        @Email(message = "enter a  valid email format")
        String email,

        @NotBlank(message = "password cannot be empty")
        @Size(min = 8, max = 255, message = "password must be between 8 and 255 characters")
        String password
) {
}
