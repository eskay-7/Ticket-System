package io.eskay.ticket_system.mapper;

import io.eskay.ticket_system.dto.request.RegisterationRequest;
import io.eskay.ticket_system.entity.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class RegistrationRequestMapper implements Function<RegisterationRequest, User> {

    private final PasswordEncoder encoder;

    public RegistrationRequestMapper(PasswordEncoder encoder) {
        this.encoder = encoder;
    }

    @Override
    public User apply(RegisterationRequest request) {
        return User.
                builder()
                .firstName(request.firstName()) //completely build it
                .lastName(request.lastName())
                .email(request.email())
                .password(encoder.encode(request.password()))
                .build();
    }
}
