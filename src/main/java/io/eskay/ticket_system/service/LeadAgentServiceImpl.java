package io.eskay.ticket_system.service;

import io.eskay.ticket_system.dto.response.TicketDto;
import io.eskay.ticket_system.dto.response.UserDto;
import io.eskay.ticket_system.entity.Category;
import io.eskay.ticket_system.entity.Role;
import io.eskay.ticket_system.entity.Team;
import io.eskay.ticket_system.exception.ForbiddenOperationException;
import io.eskay.ticket_system.exception.ResourceNotFoundException;
import io.eskay.ticket_system.mapper.TicketDtoMapper;
import io.eskay.ticket_system.mapper.UserDtoMapper;
import io.eskay.ticket_system.repository.CategoryRepository;
import io.eskay.ticket_system.repository.TeamRepository;
import io.eskay.ticket_system.repository.TicketRepository;
import io.eskay.ticket_system.repository.UserRepository;
import io.eskay.ticket_system.util.AuthenticatedUser;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class LeadAgentServiceImpl implements LeadAgentService {

    private final TicketRepository ticketRepository;
    private final TicketDtoMapper ticketDtoMapper;
    private final CategoryRepository categoryRepository;
    private final TeamRepository teamRepository;
    private final UserDtoMapper userDtoMapper;
    private final UserRepository userRepository;

    public LeadAgentServiceImpl(
            TicketRepository ticketRepository,
            TicketDtoMapper ticketDtoMapper,
            CategoryRepository categoryRepository,
            TeamRepository teamRepository,
            UserDtoMapper userDtoMapper,
            UserRepository userRepository
    ) {
        this.ticketRepository = ticketRepository;
        this.ticketDtoMapper = ticketDtoMapper;
        this.categoryRepository = categoryRepository;
        this.teamRepository = teamRepository;
        this.userDtoMapper = userDtoMapper;
        this.userRepository = userRepository;
    }

    @Override
    public List<TicketDto> getTeamTickets() {
        var team = getTeamByLead();

        return ticketRepository.findByTeam(team)
                .stream()
                .map(ticketDtoMapper)
                .toList();
    }

    @Override
    public TicketDto getTicket(Long id) {
        var team = getTeamByLead();

        var ticket = ticketRepository.findByIdAndTeam(id, team)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Error, ticket with id '%d' does not belong to this team, check and try again"
                                .formatted(id)));
        return ticketDtoMapper.apply(ticket);
    }

    @Override
    public List<UserDto> getTeamMembers() {
        var team = getTeamByLead();

        return userRepository.findByTeam(team)
                .stream()
                .map(userDtoMapper)
                .toList();
    }

    @Override
    @Transactional
    public TicketDto assignTicketToAgent(Long ticketId, Long agentId) {
        /*find the ticket by id and the team
         * find the supposed agent by the id and team then check if agent */
        var team = getTeamByLead();

        var ticket = ticketRepository.findByIdAndTeam(ticketId, team)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Error, ticket with id '%d' does not belong to this team, check and try again"
                                .formatted(ticketId)));

        var agent = userRepository.findByIdAndTeam(agentId, team)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Error, agent with id '%d' not found or does not belong to this team, check and try again"
                                .formatted(agentId)));

//        ticket.setAssignedTo(agent);
//        ticket.setStatus(TicketStatus.OPEN);
        ticket.assignedToAgent(agent);

        return ticketDtoMapper.apply(ticketRepository.save(ticket));
    }

    @Override
    public List<Category> getTeamCategories() {
        var team = getTeamByLead();

        return categoryRepository.findByDefaultTeam(team);
    }

    @Override
    public Category addCategory(String name) {
        var team = getTeamByLead();

        var category = new Category(name, team);
        return categoryRepository.save(category);
    }

    @Override
    public List<UserDto> getAllUsersWithoutTeam() {
        return userRepository.findAllByTeamIsNull()
                .stream()
                .map(userDtoMapper)
                .toList();
    }

    @Override
    @Transactional
    public void addNewAgentToTeam(Long userId) {
        var user = userRepository.findByIdWithTeam(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Error, user with id '%d' not found, check and try again"
                        .formatted(userId)));
        if (user.getTeam() != null)
            throw new ForbiddenOperationException("Forbidden, user with id '%d' is already an agent"
                    .formatted(userId));

        var team = getTeamByLead();
        user.setTeam(team);
        user.addRole(Role.AGENT);
        userRepository.save(user);
    }

    @Override
    public void removeAgentFromTeam(Long agentId) {
        var team = getTeamByLead();

        var agent = userRepository.findByIdAndTeam(agentId, team)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Error, agent with id '%d' not found or does not belong to this team, check and try again"
                                .formatted(agentId)));
        agent.setTeam(null);
        agent.removeRole(Role.AGENT);
        userRepository.save(agent);
    }

    private Team getTeamByLead() {
        return teamRepository.findByTeamLead(AuthenticatedUser.get())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Error, no team found where current authenticated user is a Team Lead"));
    }
}