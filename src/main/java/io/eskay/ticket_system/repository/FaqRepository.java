package io.eskay.ticket_system.repository;

import io.eskay.ticket_system.entity.Category;
import io.eskay.ticket_system.entity.Faq;
import io.eskay.ticket_system.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface FaqRepository extends JpaRepository<Faq, Long> {
    @Query(value = "select f from Faq f left join f.category c where c.defaultTeam = :team")
    List<Faq> findAllByTeam(Team team);

    @Query(value = "select f from Faq f left join f.category c where f.id = :id and c.defaultTeam = :team")
    Optional<Faq> findByIdAndTeam(Long id, Team team);

    List<Faq> findAllByCategory(Category category);
}