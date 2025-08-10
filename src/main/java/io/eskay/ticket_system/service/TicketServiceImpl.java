package io.eskay.ticket_system.service;

import io.eskay.ticket_system.dto.request.CreateCommentRequest;
import io.eskay.ticket_system.dto.request.CreateTicketRequest;
import io.eskay.ticket_system.dto.request.UpdateTicketRequest;
import io.eskay.ticket_system.dto.response.CommentDto;
import io.eskay.ticket_system.dto.response.TicketDto;
import io.eskay.ticket_system.entity.Ticket;
import io.eskay.ticket_system.entity.TicketStatus;
import io.eskay.ticket_system.exception.ForbiddenOperationException;
import io.eskay.ticket_system.exception.ResourceNotFoundException;
import io.eskay.ticket_system.mapper.CommentDtoMapper;
import io.eskay.ticket_system.mapper.CommentRequestMapper;
import io.eskay.ticket_system.mapper.TicketDtoMapper;
import io.eskay.ticket_system.mapper.TicketRequestMapper;
import io.eskay.ticket_system.repository.CategoryRepository;
import io.eskay.ticket_system.repository.TicketRepository;
import io.eskay.ticket_system.util.AuthenticatedUser;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TicketServiceImpl implements TicketService {

    private final TicketRepository ticketRepository;
    private final TicketDtoMapper ticketDtoMapper;
    private final TicketRequestMapper requestMapper;
    private final CategoryRepository categoryRepository;
    private final CommentDtoMapper commentDtoMapper;
    private final CommentRequestMapper commentRequestMapper;

    public TicketServiceImpl(
            TicketRepository ticketRepository,
            TicketDtoMapper ticketDtoMapper,
            TicketRequestMapper requestMapper,
            CategoryRepository categoryRepository,
            CommentDtoMapper commentDtoMapper,
            CommentRequestMapper commentRequestMapper
    ) {
        this.ticketRepository = ticketRepository;
        this.ticketDtoMapper = ticketDtoMapper;
        this.requestMapper = requestMapper;
        this.categoryRepository = categoryRepository;
        this.commentDtoMapper = commentDtoMapper;
        this.commentRequestMapper = commentRequestMapper;
    }

    @Override
    public List<TicketDto> getUserTickets() {
        return ticketRepository
                .findAllByRaisedBy(AuthenticatedUser.get())
                .stream()
                .map(ticketDtoMapper)
                .toList();
    }

    @Override
    public TicketDto getTicket(Long id) {
        return ticketDtoMapper.apply(getTicketByIdAndRaisedBy(id));
    }

    @Override
    @Transactional
    public TicketDto createTicket(CreateTicketRequest request) {
        var category = categoryRepository
                .findByIdWithTeam(request.category())
                .orElseThrow(() -> new IllegalArgumentException(
                        "Error specified category not found, check and try again"
                ));

        var ticket = requestMapper.apply(request);
        ticket.setCategory(category);
        ticket.setTeam(category.getDefaultTeam());

        return ticketDtoMapper.apply(ticketRepository.save(ticket));
    }

    @Override
    @Transactional
    public TicketDto updateTicket(Long id, UpdateTicketRequest request) {
        var foundTicket = getTicketByIdAndRaisedBy(id);

        updateTicketDetails(request, foundTicket);
        return ticketDtoMapper.apply(ticketRepository.save(foundTicket));
    }

    @Override
    @Transactional
    public void deleteTicket(Long id) {
        var foundTicket = getTicketByIdAndRaisedBy(id);
        if (
                foundTicket.getStatus().equals(TicketStatus.PENDING) ||
                foundTicket.getStatus().equals(TicketStatus.CLOSED)
        ) {
            ticketRepository.deleteById(id);
            return;
        }

        throw new ForbiddenOperationException(
                "Forbidden, ticket cannot be deleted whiles it is %s"
                        .formatted(foundTicket.getStatus().name()));
    }

    @Override
    public List<CommentDto> getTicketComments(Long id) {
        var foundTicket = getTicketByIdAndRaisedByWithComments(id);

        return foundTicket
                .getTicketComments()
                .stream()
                .map(commentDtoMapper)
                .toList();
    }

    @Override
    @Transactional
    public CommentDto addCommentToTicket(Long id, CreateCommentRequest request) {
        var foundTicket = getTicketByIdAndRaisedByWithComments(id);

        var comment = commentRequestMapper.apply(request);
        foundTicket.addComment(comment);
        ticketRepository.save(foundTicket);

        return commentDtoMapper.apply(foundTicket.getTicketComments().getLast());
    }


    private Ticket getTicketByIdAndRaisedBy(Long id) {
        return ticketRepository
                .findByIdAndRaisedBy(id, AuthenticatedUser.get())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User does not have a ticket with id '%d', check and try again".formatted(id)));
    }

    private Ticket getTicketByIdAndRaisedByWithComments(Long id) {
        return ticketRepository
                .findByIdAndRaisedByWithComments(id, AuthenticatedUser.get())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User does not have a ticket with id '%d', check and try again".formatted(id)));
    }

    private void updateTicketDetails(
            UpdateTicketRequest request,
            Ticket foundTicket
    ) {
        foundTicket.setTitle(request.title());
        foundTicket.setDescription(request.description());
    }
}
