package io.eskay.ticket_system.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record TeamsDto(
        Long id,
        String name,
        @JsonProperty("team_lead")
        String teamLead
) {
}
