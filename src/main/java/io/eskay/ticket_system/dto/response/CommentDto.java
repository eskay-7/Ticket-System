package io.eskay.ticket_system.dto.response;


import java.sql.Timestamp;

public record CommentDto(
        Long id,
        String message,
        Timestamp createdAt
) {
}
