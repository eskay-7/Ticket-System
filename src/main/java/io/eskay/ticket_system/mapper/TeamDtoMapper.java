package io.eskay.ticket_system.mapper;

import io.eskay.ticket_system.dto.response.TeamDto;
import io.eskay.ticket_system.entity.Team;
import io.eskay.ticket_system.entity.User;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class TeamDtoMapper implements Function<Team, TeamDto> {
    @Override
    public TeamDto apply(Team team) {
        return new TeamDto(
                team.getId(),
                team.getName());
    }

    private String getUserFullName(User user) {
        if (user == null)
            return "Not assigned";
        return "%s %s".formatted(user.getFirstName(), user.getLastName());
    }
}
