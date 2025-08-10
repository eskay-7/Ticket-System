package io.eskay.ticket_system.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record AgentUpdateTicketRequest(
        @NotBlank(message = "title field cannot be empty")
        @Size(min = 4, max = 255, message = "title must have a length between 4 and 255 characters")
        String title,

        @NotBlank(message = "description field cannot be empty")
        @Size(min = 4, message = "description must have a minimum length of 4 characters")
        String description,

        @Min(value = 1, message = "category value cannot be less than 1")
        int category,

        @NotBlank(message = "priority field cannot be empty")
        @Size(min = 3, max = 50, message = "priority must have a length between 3 and 50 characters")
        String priority,

        @NotBlank(message = "status field cannot be empty")
        @Size(min = 3, max = 50, message = "status must have a length between 3 and 50 characters")
        String status
) {
}
