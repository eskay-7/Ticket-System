package io.eskay.ticket_system.service;

import io.eskay.ticket_system.dto.request.CreateCommentRequest;
import io.eskay.ticket_system.dto.request.CreateTicketRequest;
import io.eskay.ticket_system.dto.request.UpdateTicketRequest;
import io.eskay.ticket_system.dto.response.CommentDto;
import io.eskay.ticket_system.dto.response.TicketDto;
import io.eskay.ticket_system.entity.Ticket;
import io.eskay.ticket_system.exception.ResourceNotFoundException;
import io.eskay.ticket_system.mapper.CommentDtoMapper;
import io.eskay.ticket_system.mapper.CommentRequestMapper;
import io.eskay.ticket_system.mapper.TicketDtoMapper;
import io.eskay.ticket_system.mapper.TicketRequestMapper;
import io.eskay.ticket_system.repository.CategoryRepository;
import io.eskay.ticket_system.repository.TicketCommentRepository;
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
    private final TicketCommentRepository commentRepository;
    private final CommentDtoMapper commentDtoMapper;
    private final CommentRequestMapper commentRequestMapper;

    public TicketServiceImpl(
            TicketRepository ticketRepository,
            TicketDtoMapper ticketDtoMapper,
            TicketRequestMapper requestMapper,
            CategoryRepository categoryRepository,
            TicketCommentRepository commentRepository,
            CommentDtoMapper commentDtoMapper,
            CommentRequestMapper commentRequestMapper) {
        this.ticketRepository = ticketRepository;
        this.ticketDtoMapper = ticketDtoMapper;
        this.requestMapper = requestMapper;
        this.categoryRepository = categoryRepository;
        this.commentRepository = commentRepository;
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
        return ticketRepository
                .findByIdAndRaisedBy(id, AuthenticatedUser.get())
                .map(ticketDtoMapper)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User does not have a ticket with id '%d', check and try again".formatted(id)));
    }

    @Override
    @Transactional
    public TicketDto createTicket(CreateTicketRequest request) {
        var category = categoryRepository
                .findById(request.category())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Error specified category not found, check and try again"
                ));

        var ticket = requestMapper.apply(request);
        ticket.setCategory(category);

        return ticketDtoMapper.apply(ticketRepository.save(ticket));
    }

    @Override
    @Transactional
    public TicketDto updateTicket(Long id, UpdateTicketRequest request) {
        var foundTicket = ticketRepository
                .findByIdAndRaisedBy(id, AuthenticatedUser.get())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User does not have a ticket with id '%d', check and try again".formatted(id)));

        updateTicket(request, foundTicket);
        return ticketDtoMapper.apply(ticketRepository.save(foundTicket));
    }

    @Override
    @Transactional
    public void deleteTicket(Long id) {
        ticketRepository
                .findByIdAndRaisedBy(id, AuthenticatedUser.get())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User does not have a ticket with id '%d', check and try again".formatted(id)));
        ticketRepository.deleteById(id);
    }

    @Override
    public List<CommentDto> getTicketComments(Long id) {
        var foundTicket = ticketRepository
                .findByIdAndRaisedBy(id, AuthenticatedUser.get())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User does not have a ticket with id '%d', check and try again".formatted(id)));

        return commentRepository
                .findAllByAuthorAndTicket
                        (AuthenticatedUser.get(), foundTicket)
                .stream()
                .map(commentDtoMapper)
                .toList();
    }

    @Override
    public CommentDto addComment(Long id, CreateCommentRequest request) {
        var foundTicket = ticketRepository
                .findByIdAndRaisedBy(id, AuthenticatedUser.get())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User does not have a ticket with id '%d', check and try again".formatted(id)));

        var comment = commentRequestMapper.apply(request);
        comment.setTicket(foundTicket);
        return commentDtoMapper.apply(commentRepository.save(comment));
    }

    private void updateTicket(
            UpdateTicketRequest request,
            Ticket foundTicket
    ) {
        foundTicket.setTitle(request.title());
        foundTicket.setDescription(request.description());
    }
}
