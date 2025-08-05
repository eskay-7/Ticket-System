package io.eskay.ticket_system.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UpdateTicketRequest(
        @NotBlank(message = "title field cannot be empty")
        @Size(min = 4, max = 255, message = "title must have a length between 4 and 255 characters")
        String title,

        @NotBlank(message = "description field cannot be empty")
        @Size(min = 4, max = 255, message = "description must have a length between 4 and 255 characters")
        String description
) {
}
