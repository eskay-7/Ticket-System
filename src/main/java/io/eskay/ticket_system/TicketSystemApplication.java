package io.eskay.ticket_system;

import io.eskay.ticket_system.entity.Category;
import io.eskay.ticket_system.entity.TicketPriority;
import io.eskay.ticket_system.entity.TicketStatus;
import io.eskay.ticket_system.repository.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@SpringBootApplication
@RestController
@RequestMapping("/api/info")
public class TicketSystemApplication {

    private final CategoryRepository categoryRepository;

    public TicketSystemApplication(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public static void main(String[] args) {
        SpringApplication.run(TicketSystemApplication.class, args);
    }

    @GetMapping("/categories")
    public List<Category> getCategories() {
        return categoryRepository.findAll();
    }

    @GetMapping("/statuses")
    public List<String> getStatuses() {
        return Arrays
                .stream(TicketStatus.values())
                .map(Enum::name)
                .toList();
    }

    @GetMapping("/priorities")
    public List<String> getPriorities() {
        return Arrays
                .stream(TicketPriority.values())
                .map(Enum::name)
                .toList();
    }
}
