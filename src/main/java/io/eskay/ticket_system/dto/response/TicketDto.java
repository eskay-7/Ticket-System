package io.eskay.ticket_system.dto.response;

import java.sql.Timestamp;

public record TicketDto(
        Long id,
        String title,
        String description,
        Timestamp createdAt,
        Timestamp updatedAT,
        String category,
        String priority,
        String status,
        String raisedBy,
        String assignedTo
) {
}
