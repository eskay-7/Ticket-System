package io.eskay.ticket_system.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record UserDto(
        Long id,

        @JsonProperty("first_name")
        String firstName,

        @JsonProperty("last_name")
        String lastName,

        String email) {
}