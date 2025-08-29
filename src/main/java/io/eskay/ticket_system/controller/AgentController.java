package io.eskay.ticket_system.controller;

import io.eskay.ticket_system.dto.request.*;
import io.eskay.ticket_system.dto.response.CommentDto;
import io.eskay.ticket_system.dto.response.FaqDto;
import io.eskay.ticket_system.dto.response.TicketDto;
import io.eskay.ticket_system.entity.Team;
import io.eskay.ticket_system.service.AgentService;
import io.eskay.ticket_system.service.FaqService;
import io.eskay.ticket_system.service.FaqServiceImpl;
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
    private final FaqService faqService;

    public AgentController(
            AgentService agentService,
            FaqService faqService
    ) {
        this.agentService = agentService;
        this.faqService = faqService;
    }

    @GetMapping("/tickets")
    public List<TicketDto> getLatestPendingTicketsForTeam() {
        return agentService.getLatestPendingTeamTickets();
    }

//    @GetMapping("/tickets/{ticket_id}")
//    public ResponseEntity<TicketDto> getTicket(@PathVariable("ticket_id") Long ticketId) {
//        var ticket = agentService.getUnAssignedTicket(ticketId);
//        return ResponseEntity.ok(ticket);
//    }

    @PutMapping("/tickets/{ticket_id}/assign_to")
    public ResponseEntity<?> assignTicketToAgent(
            @PathVariable("ticket_id") Long ticketId
    ) {
        agentService.assignTicketToAgent(ticketId);
        return ResponseEntity.ok().build();
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

    @PostMapping("/teams")
    public ResponseEntity<Team> createTeam(@RequestBody @Valid CreateTeamRequest request) {
        var createdTeam = agentService.createTeam(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTeam);
    }

    /*
    * get faqs for team, create faq, update faq, delete faq*/

    @PostMapping("/faqs")
    public ResponseEntity<FaqDto> createFaq(@RequestBody @Valid CreateFaqRequest request) {
        var createdFaq = faqService.createFaq(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdFaq);
    }

    @GetMapping("/faqs")
    public List<FaqDto> getTeamFaqs() {
        return faqService.getTeamFaqs();
    }

    @DeleteMapping("/faqs/{faq_id}")
    public ResponseEntity<?> deleteFaq(@PathVariable("faq_id") Long faqId) {
        faqService.deleteFaq(faqId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PutMapping("/faqs/{faq_id}")
    public ResponseEntity<FaqDto> updateFaq(
            @PathVariable("faq_id") Long faqId,
            @RequestBody @Valid UpdateFaqRequest request
    ) {
        var updatedFaq = faqService.updateFaq(faqId, request);
        return ResponseEntity.ok(updatedFaq);
    }

}
