package io.eskay.ticket_system.controller;

import io.eskay.ticket_system.dto.response.TicketDto;
import io.eskay.ticket_system.dto.response.UserDto;
import io.eskay.ticket_system.entity.Category;
import io.eskay.ticket_system.service.LeadAgentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/agents")
@PreAuthorize("hasRole('LEAD')")
public class LeadAgentController {

    private final LeadAgentService leadAgentService;

    public LeadAgentController(LeadAgentService leadAgentService) {
        this.leadAgentService = leadAgentService;
    }

    @GetMapping("/tickets")
    public List<TicketDto> getAllTicketsForTeam() {
        return leadAgentService.getTeamTickets();
    }

    @GetMapping("/tickets/{ticket_id}")
    public ResponseEntity<TicketDto> getTicket(@PathVariable("ticket_id") Long ticketId) {
        var ticket = leadAgentService.getTicket(ticketId);
        return ResponseEntity.ok(ticket);
    }

    @PutMapping("/tickets/{ticket_id}/assign_to")
    public ResponseEntity<TicketDto> assignTicketToAgent(
            @PathVariable("ticket_id") Long ticketId,
            @RequestParam(name = "agent_id") Long agentId
    ) {
        var ticket = leadAgentService.assignTicketToAgent(ticketId, agentId);
        return ResponseEntity.ok(ticket);
    }

    @GetMapping("/team_members")
    public List<UserDto> getAllTeamMembers() {
        return leadAgentService.getTeamMembers();
    }

    @GetMapping("/users")
    public List<UserDto> getAllUsersWithoutTeam() {
        return leadAgentService.getAllUsersWithoutTeam();
    }

    @PutMapping("/team_members")
    public ResponseEntity<?> addNewAgentToTeam(@RequestParam("user_id") Long userId) {
        leadAgentService.addNewAgentToTeam(userId);
        return ResponseEntity.ok("Success");
    }

    @DeleteMapping("/team_members")
    public ResponseEntity<?> removeAgentFromTeam(@RequestParam("agent_id") Long agentId) {
        leadAgentService.removeAgentFromTeam(agentId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PostMapping("/categories")
    public ResponseEntity<Category> addCategory(@RequestParam String name) {
        var createdCategory = leadAgentService.addCategory(name);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdCategory);
    }

    @GetMapping("/categories")
    public List<Category> getTeamCategories() {
        return leadAgentService.getTeamCategories();
    }
}