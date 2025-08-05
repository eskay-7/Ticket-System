package io.eskay.ticket_system.mapper;

import io.eskay.ticket_system.dto.response.TicketDto;
import io.eskay.ticket_system.entity.Ticket;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.function.Function;

@Component
public class TicketDtoMapper implements Function<Ticket, TicketDto> {
    @Override
    public TicketDto apply(Ticket ticket) {
        return new TicketDto(
                ticket.getId(),
                ticket.getTitle(),
                ticket.getDescription(),
                Timestamp.valueOf(ticket.getCreatedAt()),
                Timestamp.valueOf(ticket.getUpdatedAt()),
                ticket.getCategory().getName(),
                checkPriority(ticket),
                ticket.getStatus().name(),
                ticket.getRaisedBy().getId().toString(),
                checkAssignedTo(ticket)
        );
    }

    private String checkAssignedTo(Ticket ticket) {
        if (ticket.getAssignedTo() == null)
            return "Not assigned";
        return ticket.getAssignedTo().getId().toString();
    }

    private String checkPriority(Ticket ticket) {
        if (ticket.getPriority() == null)
            return "Not assigned";
        return ticket.getPriority().name();
    }
}
