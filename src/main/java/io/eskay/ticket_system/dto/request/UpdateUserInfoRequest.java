package io.eskay.ticket_system.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UpdateUserInfoRequest(
        @JsonProperty("first_name")
        @NotBlank(message = "first_name field cannot be empty")
        @Size(min = 2, max = 255, message = "first_name must have a length between 2 and 255 characters")
        String firstName,

        @NotBlank(message = "last_name field cannot be empty")
        @Size(min = 2, max = 255, message = "last_name must have a length between 2 and 255 characters")
        @JsonProperty("last_name")
        String lastName
) {
}
