package io.eskay.ticket_system.mapper;

import io.eskay.ticket_system.dto.response.TicketDto;
import io.eskay.ticket_system.entity.Ticket;
import io.eskay.ticket_system.entity.User;
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
                getUserFullName(ticket.getRaisedBy()),
                getUserFullName(ticket.getAssignedTo())
        );
    }

    private String checkPriority(Ticket ticket) {
        if (ticket.getPriority() == null)
            return "Not assigned";
        return ticket.getPriority().name();
    }

    private String getUserFullName(User user) {
        if (user == null)
            return "Not assigned";
        return "%s %s".formatted(user.getFirstName(), user.getLastName());
    }
}
