package io.eskay.ticket_system.repository;

import io.eskay.ticket_system.entity.Ticket;
import io.eskay.ticket_system.entity.TicketComment;
import io.eskay.ticket_system.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TicketCommentRepository extends JpaRepository<TicketComment, Long> {
    List<TicketComment> findAllByTicket(Ticket ticket);
}