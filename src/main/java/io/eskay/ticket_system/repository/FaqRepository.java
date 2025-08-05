package io.eskay.ticket_system.repository;

import io.eskay.ticket_system.entity.Faq;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FaqRepository extends JpaRepository<Faq, Long> {
}