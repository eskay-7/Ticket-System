package io.eskay.ticket_system.mapper;

import io.eskay.ticket_system.dto.response.UserDto;
import io.eskay.ticket_system.entity.User;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class UserDtoMapper implements Function<User, UserDto> {
    @Override
    public UserDto apply(User user) {
        return new UserDto(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail()
        );
    }
}
