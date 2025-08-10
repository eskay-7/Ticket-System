package io.eskay.ticket_system.repository;

import io.eskay.ticket_system.entity.Team;
import io.eskay.ticket_system.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TeamRepository extends JpaRepository<Team, Long> {

    Optional<Team> findByTeamLead(User user);
}