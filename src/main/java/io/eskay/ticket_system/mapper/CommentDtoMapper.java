package io.eskay.ticket_system.mapper;

import io.eskay.ticket_system.dto.response.CommentDto;
import io.eskay.ticket_system.entity.TicketComment;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.function.Function;

@Component
public class CommentDtoMapper implements Function<TicketComment, CommentDto> {
    @Override
    public CommentDto apply(TicketComment comment) {
        return new CommentDto(
                comment.getId(),
                comment.getMessage(),
                Timestamp.valueOf(comment.getCreatedAt())
        );
    }
}
