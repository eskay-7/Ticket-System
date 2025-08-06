package io.eskay.ticket_system.mapper;

import io.eskay.ticket_system.dto.request.CreateCommentRequest;
import io.eskay.ticket_system.entity.TicketComment;
import io.eskay.ticket_system.util.AuthenticatedUser;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class CommentRequestMapper implements Function<CreateCommentRequest, TicketComment> {
    @Override
    public TicketComment apply(CreateCommentRequest request) {
        return TicketComment.builder()
                .author(AuthenticatedUser.get())
                .message(request.message())
                .build();
    }
}
