package io.eskay.ticket_system.service;

import io.eskay.ticket_system.dto.request.CreateTeamRequest;
import io.eskay.ticket_system.dto.response.TeamDto;
import io.eskay.ticket_system.dto.response.UserDto;

import java.util.List;

public interface AdminService {
    List<UserDto> getAllUsers();

    List<UserDto> getAllUsersFilteredByTeam(boolean isAgent);

    List<TeamDto> getAllTeams();

    TeamDto getTeam(Long teamId);

    TeamDto createTeam(CreateTeamRequest request);
}
