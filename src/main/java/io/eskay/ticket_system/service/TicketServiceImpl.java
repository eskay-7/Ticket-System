package io.eskay.ticket_system.service;

import io.eskay.ticket_system.dto.request.CreateTicketRequest;
import io.eskay.ticket_system.dto.request.UpdateTicketRequest;
import io.eskay.ticket_system.dto.response.TicketDto;
import io.eskay.ticket_system.entity.Ticket;
import io.eskay.ticket_system.exception.ResourceNotFoundException;
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
    private final TicketDtoMapper dtoMapper;
    private final TicketRequestMapper requestMapper;
    private final CategoryRepository categoryRepository;

    public TicketServiceImpl(
            TicketRepository ticketRepository,
            TicketDtoMapper dtoMapper,
            TicketRequestMapper requestMapper,
            CategoryRepository categoryRepository
    ) {
        this.ticketRepository = ticketRepository;
        this.dtoMapper = dtoMapper;
        this.requestMapper = requestMapper;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public List<TicketDto> getUserTickets() {
        return ticketRepository
                .findAllByRaisedBy(AuthenticatedUser.get())
                .stream()
                .map(dtoMapper)
                .toList();
    }

    @Override
    public TicketDto getTicket(Long id) {
        return ticketRepository
                .findByIdAndRaisedBy(id, AuthenticatedUser.get())
                .map(dtoMapper)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Ticket with id '%d' not found, check and try again".formatted(id)));
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

        return dtoMapper.apply(ticketRepository.save(ticket));
    }

    @Override
    @Transactional
    public TicketDto updateTicket(Long id, UpdateTicketRequest request) {
        var foundTicket = ticketRepository
                .findByIdAndRaisedBy(id, AuthenticatedUser.get())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Ticket with id '%d' not found, check and try again".formatted(id)));

        updateTicket(request, foundTicket);
        return dtoMapper.apply(ticketRepository.save(foundTicket));
    }

    @Override
    @Transactional
    public void deleteTicket(Long id) {
        ticketRepository
                .findByIdAndRaisedBy(id, AuthenticatedUser.get())
                .map(dtoMapper)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Ticket with id '%d' not found, check and try again".formatted(id)));
        ticketRepository.deleteById(id);
    }

    private void updateTicket(
            UpdateTicketRequest request,
            Ticket foundTicket
    ) {
        foundTicket.setTitle(request.title());
        foundTicket.setDescription(request.description());
    }
}
