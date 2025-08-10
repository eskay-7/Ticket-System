package io.eskay.ticket_system.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record AuthResponse(
        @JsonProperty(value = "access_token")
        String accessToken
) {
}
