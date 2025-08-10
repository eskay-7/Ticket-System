package io.eskay.ticket_system.controller;

import io.eskay.ticket_system.dto.request.CreateCommentRequest;
import io.eskay.ticket_system.dto.request.CreateTicketRequest;
import io.eskay.ticket_system.dto.request.UpdateTicketRequest;
import io.eskay.ticket_system.dto.response.CommentDto;
import io.eskay.ticket_system.dto.response.TicketDto;
import io.eskay.ticket_system.service.TicketService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tickets")
public class TicketController {

    private final TicketService ticketService;

    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @GetMapping
    public List<TicketDto> getUserTickets() {
        return ticketService.getUserTickets();
    }

    @GetMapping("{id}")
    public ResponseEntity<TicketDto> getTicket(@PathVariable Long id) {
        var ticket = ticketService.getTicket(id);
        return ResponseEntity.ok().body(ticket);
    }

    @PostMapping
    public ResponseEntity<TicketDto> createTicket(@RequestBody @Valid CreateTicketRequest request) {
        var createdTicket = ticketService.createTicket(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTicket);
    }

    @PutMapping("{id}")
    public ResponseEntity<TicketDto> updateTicket(
            @RequestBody @Valid UpdateTicketRequest request,
            @PathVariable Long id
    ) {
        var updatedTicket = ticketService.updateTicket(id, request);
        return ResponseEntity.ok().body(updatedTicket);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> deleteTicket(@PathVariable Long id) {
        ticketService.deleteTicket(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("{ticket_id}/comments")
    public List<CommentDto> getTicketComments(@PathVariable("ticket_id") Long ticketId) {
        return ticketService.getTicketComments(ticketId);
    }

    @PostMapping("{ticket_id}/comments")
    public ResponseEntity<CommentDto> addComment(
            @PathVariable("ticket_id") Long ticketId,
            @RequestBody @Valid CreateCommentRequest request
    ) {
        var createdTicket = ticketService.addCommentToTicket(ticketId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTicket);
    }
}
