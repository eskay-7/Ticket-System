package io.eskay.ticket_system.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UpdateFaqRequest(
        @NotBlank(message = "question field cannot be empty")
        @Size(min = 4, max = 255, message = "question must have a length between 4 and 255 characters")
        String question,

        @NotBlank(message = "answer field cannot be empty")
        @Size(min = 4, max = 255, message = "answer must have a length between 4 and 255 characters")
        String answer
) {
}
