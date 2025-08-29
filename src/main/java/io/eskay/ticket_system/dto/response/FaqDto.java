package io.eskay.ticket_system.dto.response;

public record FaqDto(
        Long id,
        String question,
        String answer,
        String category
) {
}
