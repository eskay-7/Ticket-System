package io.eskay.ticket_system.service;

import io.eskay.ticket_system.dto.response.TicketDto;
import io.eskay.ticket_system.dto.response.UserDto;
import io.eskay.ticket_system.entity.Category;
import io.eskay.ticket_system.entity.User;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface LeadAgentService {
    List<TicketDto> getTeamTickets();

    TicketDto getTicket(Long id);

    List<UserDto> getTeamMembers();

    TicketDto assignTicketToAgent(Long ticketId, Long agentId);

    List<Category> getTeamCategories();

    Category addCategory(String name);

    List<UserDto> getAllUsersWithoutTeam();

    void addNewAgentToTeam(Long userId);

    void removeAgentFromTeam(Long agentId);
}
