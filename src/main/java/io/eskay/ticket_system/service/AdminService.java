package io.eskay.ticket_system.service;

import io.eskay.ticket_system.dto.request.CreateTeamRequest;
import io.eskay.ticket_system.dto.response.TeamsDto;
import io.eskay.ticket_system.dto.response.UserDto;
import jakarta.validation.Valid;

import java.util.List;

public interface AdminService {
    List<UserDto> getAllUsers();

    List<UserDto> getAllUsersFilteredByTeam(boolean isAgent);

    List<TeamsDto> getAllTeams();

    TeamsDto getTeam(Long teamId);

    TeamsDto createTeam(CreateTeamRequest request);
}
