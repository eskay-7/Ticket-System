package io.eskay.ticket_system.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateCommentRequest(
        @NotBlank(message = "message field cannot be empty")
        @Size(min = 4, message = "message must have a length of at least 4 characters")
        String message
) {
}
