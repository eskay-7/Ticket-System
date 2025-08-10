package io.eskay.ticket_system.service;

import io.eskay.ticket_system.dto.request.AgentUpdateTicketRequest;
import io.eskay.ticket_system.dto.request.CreateCommentRequest;
import io.eskay.ticket_system.dto.response.CommentDto;
import io.eskay.ticket_system.dto.response.TicketDto;
import io.eskay.ticket_system.entity.Ticket;
import io.eskay.ticket_system.entity.TicketPriority;
import io.eskay.ticket_system.entity.TicketStatus;
import io.eskay.ticket_system.exception.ResourceNotFoundException;
import io.eskay.ticket_system.mapper.CommentDtoMapper;
import io.eskay.ticket_system.mapper.CommentRequestMapper;
import io.eskay.ticket_system.mapper.TicketDtoMapper;
import io.eskay.ticket_system.repository.CategoryRepository;
import io.eskay.ticket_system.repository.TicketRepository;
import io.eskay.ticket_system.util.AuthenticatedUser;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AgentServiceImpl implements AgentService {

    private final TicketRepository ticketRepository;
    private final TicketDtoMapper ticketDtoMapper;
    private final CategoryRepository categoryRepository;
    private final CommentDtoMapper commentDtoMapper;
    private final CommentRequestMapper commentRequestMapper;

    public AgentServiceImpl(
            TicketRepository ticketRepository,
            TicketDtoMapper ticketDtoMapper,
            CategoryRepository categoryRepository,
            CommentDtoMapper commentDtoMapper,
            CommentRequestMapper commentRequestMapper
    ) {
        this.ticketRepository = ticketRepository;
        this.ticketDtoMapper = ticketDtoMapper;
        this.categoryRepository = categoryRepository;
        this.commentDtoMapper = commentDtoMapper;
        this.commentRequestMapper = commentRequestMapper;
    }

    @Override
    public List<TicketDto> getAllAssignedTickets() {
        return ticketRepository
                .findAllByAssignedTo(AuthenticatedUser.get())
                .stream()
                .map(ticketDtoMapper)
                .toList();
    }

    @Override
    public TicketDto getAssignedTicket(Long id) {
        return ticketRepository
                .findByIdAndAssignedTo(id, AuthenticatedUser.get())
                .map(ticketDtoMapper)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Error, agent not assigned ticket with id '%d', check and try again".formatted(id)));
    }

    @Override
    public List<CommentDto> getTicketComments(Long id) {
        var foundTicket = ticketRepository
                .findByIdAndAssignedToWithComments(id, AuthenticatedUser.get())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Error, agent not assigned ticket with id '%d', check and try again".formatted(id)));

        return foundTicket
                .getTicketComments()
                .stream()
                .map(commentDtoMapper)
                .toList();
    }

    @Override
    @Transactional
    public CommentDto addComment(Long id, CreateCommentRequest request) {
        var foundTicket = ticketRepository
                .findByIdAndAssignedToWithComments(id, AuthenticatedUser.get())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Error, agent not assigned ticket with id '%d', check and try again".formatted(id)));

        var comment = commentRequestMapper.apply(request);
        foundTicket.addComment(comment);
        ticketRepository.save(foundTicket);
        return commentDtoMapper.apply(foundTicket.getTicketComments().getLast());
    }

    @Override
    @Transactional
    public TicketDto updateTicket(Long id, AgentUpdateTicketRequest request) {
        var foundTicket = ticketRepository
                .findByIdAndAssignedTo(id, AuthenticatedUser.get())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Error, agent not assigned ticket with id '%d', check and try again".formatted(id)));

        updateTicketDetails(request, foundTicket);
        return ticketDtoMapper.apply(ticketRepository.save(foundTicket));
    }

    private void updateTicketDetails(AgentUpdateTicketRequest request, Ticket foundTicket) {
        var category = categoryRepository.findById(request.category())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Error specified category not found, check and try again"
                ));

        foundTicket.setTitle(request.title());
        foundTicket.setDescription(request.description());
        foundTicket.setPriority(TicketPriority.valueOf(request.priority().toUpperCase()));
        foundTicket.setStatus(TicketStatus.valueOf(request.status().toUpperCase()));
        foundTicket.setCategory(category);
    }
}
