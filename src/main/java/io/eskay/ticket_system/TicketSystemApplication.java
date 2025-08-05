package io.eskay.ticket_system;

import io.eskay.ticket_system.entity.Category;
import io.eskay.ticket_system.repository.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@SpringBootApplication
@RestController
public class TicketSystemApplication {

    private final CategoryRepository categoryRepository;

    public TicketSystemApplication(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public static void main(String[] args) {
        SpringApplication.run(TicketSystemApplication.class, args);
    }

    @GetMapping("/api/categories")
    public List<Category> welcome() {
        return categoryRepository.findAll();
    }
}
