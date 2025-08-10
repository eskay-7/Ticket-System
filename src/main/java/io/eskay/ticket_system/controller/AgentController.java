package io.eskay.ticket_system.controller;

import io.eskay.ticket_system.dto.request.AgentUpdateTicketRequest;
import io.eskay.ticket_system.dto.request.CreateCommentRequest;
import io.eskay.ticket_system.dto.response.CommentDto;
import io.eskay.ticket_system.dto.response.TicketDto;
import io.eskay.ticket_system.service.AgentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/agents")
public class AgentController {

        /*find all tickets assigned to current agent
    * get a particular ticket assigned to current agent
    * update a particular ticket assigned to current agent(change_status, change_priority)
    * view comments for a particular assigned ticket
    * add comments to a particular assigned ticket*/

    /*findByAssignedTo
    * findByIdAndAssignedTo
    * changeTicketStatus
    * changeTicketPriority
    * findAllByTicket
    * saveTicket*/

    private final AgentService agentService;

    public AgentController(AgentService agentService) {
        this.agentService = agentService;
    }

    @GetMapping("/assigned_tickets")
    public List<TicketDto> getAllAssignedTickets() {
        return agentService.getAllAssignedTickets();
    }

    @GetMapping("/assigned_tickets/{ticket_id}")
    public ResponseEntity<TicketDto> getAssignedTicket(@PathVariable("ticket_id") Long ticketId) {
        var ticket = agentService.getAssignedTicket(ticketId);
        return ResponseEntity.ok(ticket);
    }

    @PutMapping("/assigned_tickets/{ticket_id}")
    public ResponseEntity<TicketDto> updateTicket(
            @PathVariable("ticket_id") Long ticketId,
            @RequestBody @Valid AgentUpdateTicketRequest request
    ) {
        var updatedTicket = agentService.updateTicket(ticketId, request);
        return ResponseEntity.ok(updatedTicket);
    }

    @GetMapping("/assigned_tickets/{ticket_id}/comments")
    public List<CommentDto> getTicketComments(@PathVariable("ticket_id") Long ticketId) {
        return agentService.getTicketComments(ticketId);
    }

    @PostMapping("/assigned_tickets/{ticket_id}/comments")
    public ResponseEntity<CommentDto> addCommentToTicket(
            @PathVariable("ticket_id") Long ticketId,
            @RequestBody @Valid CreateCommentRequest request
    ) {
        var createdComment = agentService.addComment(ticketId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdComment);
    }
}
