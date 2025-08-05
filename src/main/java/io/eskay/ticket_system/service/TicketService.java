package io.eskay.ticket_system.service;

import io.eskay.ticket_system.dto.request.CreateTicketRequest;
import io.eskay.ticket_system.dto.request.UpdateTicketRequest;
import io.eskay.ticket_system.dto.response.TicketDto;

import java.util.List;

public interface TicketService {

    List<TicketDto> getUserTickets();
    TicketDto getTicket(Long id);
    TicketDto createTicket(CreateTicketRequest request);
    TicketDto updateTicket(Long id, UpdateTicketRequest request);
    void deleteTicket(Long id);
}
