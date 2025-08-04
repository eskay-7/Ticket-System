package io.eskay.ticket_system;

import io.eskay.ticket_system.entity.Role;
import io.eskay.ticket_system.entity.User;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashSet;

@SpringBootApplication
@RestController
public class TicketSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(TicketSystemApplication.class, args);
    }

    @GetMapping("/")
    public String welcome() {
        return "Welcome";
    }
}
