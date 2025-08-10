package io.eskay.ticket_system.service;

import io.eskay.ticket_system.dto.request.AgentUpdateTicketRequest;
import io.eskay.ticket_system.dto.request.CreateCommentRequest;
import io.eskay.ticket_system.dto.response.CommentDto;
import io.eskay.ticket_system.dto.response.TicketDto;

import java.util.List;

public interface AgentService {
    List<TicketDto> getAllAssignedTickets();

    TicketDto getAssignedTicket(Long id);

    List<CommentDto> getTicketComments(Long id);

    CommentDto addComment(Long id, CreateCommentRequest request);

    TicketDto updateTicket(Long id, AgentUpdateTicketRequest request);
}
