package io.eskay.ticket_system.service;

import io.eskay.ticket_system.dto.request.CreateCommentRequest;
import io.eskay.ticket_system.dto.request.CreateTicketRequest;
import io.eskay.ticket_system.dto.request.UpdateTicketRequest;
import io.eskay.ticket_system.dto.response.CommentDto;
import io.eskay.ticket_system.dto.response.TicketDto;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface TicketService {

    List<TicketDto> getUserTickets();
    TicketDto getTicket(Long id);
    TicketDto createTicket(CreateTicketRequest request);
    TicketDto updateTicket(Long id, UpdateTicketRequest request);
    void deleteTicket(Long id);
    List<CommentDto> getTicketComments(Long id);
    CommentDto addComment(Long ticketId, CreateCommentRequest request);
}
