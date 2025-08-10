package io.eskay.ticket_system.repository;

import io.eskay.ticket_system.entity.Team;
import io.eskay.ticket_system.entity.Ticket;
import io.eskay.ticket_system.entity.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {
        @EntityGraph(attributePaths = {"category", "raisedBy", "assignedTo"})
        @Query(value = "select t from Ticket t where t.id = :id")
        Optional<Ticket> findByIdWithInfo(Long id);

  /**
   * Finds a ticket using the provided {id} and the current authenticated user together with it's comments
   * @param id id of ticket to find
   * @param user the current authenticated user
   * @return Optional of found Ticket object, empty if no ticket was found
   */
  @EntityGraph(attributePaths = "ticketComments")
  @Query(value = "select t from Ticket t where t.id = :id and t.raisedBy = :user")
  Optional<Ticket> findByIdAndRaisedByWithComments(Long id, User user);

    /**
     * Finds a ticket using the provided {id} and the current authenticated user
     * @param id id of ticket to find
     * @param user the current authenticated user
     * @return Optional of found Ticket object, empty if no ticket was found
     */
  Optional<Ticket> findByIdAndRaisedBy(Long id, User user);

  List<Ticket> findAllByRaisedBy(User user);

  List<Ticket> findAllByAssignedTo(User user);

  Optional<Ticket> findByIdAndAssignedTo(Long id, User user);

  @EntityGraph(attributePaths = "ticketComments")
  @Query(value = "select t from Ticket t where t.id = :id and t.assignedTo = :user")
  Optional<Ticket> findByIdAndAssignedToWithComments(Long id, User user);

  List<Ticket> findByTeam(Team team);
  Optional<Ticket> findByIdAndTeam(Long id, Team team);
}