package io.eskay.ticket_system.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateTeamRequest(
        @NotBlank(message = "name field cannot be empty")
        @Size(min = 4, max = 255, message = "name must have a length between 4 and 255 characters")
        String name,

        @Min(value = 1, message = "team_lead field value cannot be less than 1")
        Long teamLead
) {
}
