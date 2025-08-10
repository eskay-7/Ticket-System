package io.eskay.ticket_system.repository;

import io.eskay.ticket_system.entity.Team;
import io.eskay.ticket_system.entity.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @EntityGraph(attributePaths = "roles")
    Optional<User> findByEmail(String email);

    @EntityGraph(attributePaths = "team")
    @Query(value = "select u from User u where u.id = :id")
    Optional<User> findByIdWithTeam(Long id);

    List<User> findByTeam(Team team);

    Optional<User> findByIdAndTeam(Long id, Team team);

    List<User> findAllByTeamIsNull();

    List<User> findAllByTeamIsNotNull();
}
