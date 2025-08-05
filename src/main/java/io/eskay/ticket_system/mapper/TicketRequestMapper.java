package io.eskay.ticket_system.mapper;

import io.eskay.ticket_system.dto.request.CreateTicketRequest;
import io.eskay.ticket_system.entity.Ticket;
import io.eskay.ticket_system.entity.TicketStatus;
import io.eskay.ticket_system.util.AuthenticatedUser;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class TicketRequestMapper implements Function<CreateTicketRequest, Ticket> {
    @Override
    public Ticket apply(CreateTicketRequest request) {
         return Ticket.builder()
                .title(request.title())
                .description(request.description())
                .status(TicketStatus.PENDING)
                .raisedBy(AuthenticatedUser.get())
                .build();
    }
}
