package io.eskay.ticket_system.repository;

import io.eskay.ticket_system.entity.Category;
import io.eskay.ticket_system.entity.Team;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {
    List<Category> findByDefaultTeam(Team team);

    @EntityGraph(attributePaths = "defaultTeam")
    @Query(value = "select c from Category c where c.id = :id")
    Optional<Category> findByIdWithTeam(Integer id);
}