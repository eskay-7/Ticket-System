package io.eskay.ticket_system.service;

import io.eskay.ticket_system.dto.request.AgentUpdateTicketRequest;
import io.eskay.ticket_system.dto.request.CreateCommentRequest;
import io.eskay.ticket_system.dto.request.CreateTeamRequest;
import io.eskay.ticket_system.dto.response.CommentDto;
import io.eskay.ticket_system.dto.response.TicketDto;
import io.eskay.ticket_system.entity.Team;
import jakarta.validation.Valid;

import java.util.List;

public interface AgentService {
    List<TicketDto> getAllAssignedTickets();

    TicketDto getAssignedTicket(Long id);

    List<CommentDto> getTicketComments(Long id);

    CommentDto addComment(Long id, CreateCommentRequest request);

    TicketDto updateTicket(Long id, AgentUpdateTicketRequest request);

    List<TicketDto> getLatestPendingTeamTickets();

    void assignTicketToAgent(Long ticketId);

    Team createTeam(CreateTeamRequest request);

    TicketDto getUnAssignedTicket(Long ticketId);
}
