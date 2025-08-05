package io.eskay.ticket_system.util;

import io.eskay.ticket_system.entity.User;
import org.springframework.security.core.context.SecurityContextHolder;

public class AuthenticatedUser {

    public static User get() {
        return (User) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
    }
}
