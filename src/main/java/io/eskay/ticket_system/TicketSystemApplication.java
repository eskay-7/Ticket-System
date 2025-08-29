package io.eskay.ticket_system;

import io.eskay.ticket_system.dto.response.FaqDto;
import io.eskay.ticket_system.entity.Category;
import io.eskay.ticket_system.entity.Team;
import io.eskay.ticket_system.entity.TicketPriority;
import io.eskay.ticket_system.entity.TicketStatus;
import io.eskay.ticket_system.repository.*;
import io.eskay.ticket_system.service.FaqService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@SpringBootApplication
@RestController
@RequestMapping("/api/info")
public class TicketSystemApplication {

    private final CategoryRepository categoryRepository;
    private final TeamRepository teamRepository;
    private final FaqService faqService;

    public TicketSystemApplication(
            CategoryRepository categoryRepository,
            TeamRepository teamRepository,
            FaqService faqService
    ) {
        this.categoryRepository = categoryRepository;
        this.teamRepository = teamRepository;
        this.faqService = faqService;
    }

    public static void main(String[] args) {
        SpringApplication.run(TicketSystemApplication.class, args);
    }

    @GetMapping("/categories")
    public List<Category> getCategories() {
        return categoryRepository.findAll();
    }

    @GetMapping("/teams")
    public List<Team> getTeams() {
        return teamRepository.findAll();
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

    @GetMapping("/faqs")
    public List<FaqDto> getFaqs(@RequestParam("category_id") int category) {
        return faqService.getFaqs(category);
    }
}
