package io.eskay.ticket_system.service;

import io.eskay.ticket_system.dto.request.CreateTeamRequest;
import io.eskay.ticket_system.dto.response.TeamsDto;
import io.eskay.ticket_system.dto.response.UserDto;
import io.eskay.ticket_system.entity.Team;
import io.eskay.ticket_system.entity.User;
import io.eskay.ticket_system.exception.ForbiddenOperationException;
import io.eskay.ticket_system.exception.ResourceNotFoundException;
import io.eskay.ticket_system.mapper.TeamDtoMapper;
import io.eskay.ticket_system.mapper.UserDtoMapper;
import io.eskay.ticket_system.repository.TeamRepository;
import io.eskay.ticket_system.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminServiceImpl implements AdminService {
    private final UserRepository userRepository;
    private final UserDtoMapper userDtoMapper;
    private final TeamRepository teamRepository;
    private final TeamDtoMapper teamDtoMapper;

    public AdminServiceImpl(
            UserRepository userRepository,
            UserDtoMapper userDtoMapper,
            TeamRepository teamRepository,
            TeamDtoMapper teamDtoMapper
    ) {
        this.userRepository = userRepository;
        this.userDtoMapper = userDtoMapper;
        this.teamRepository = teamRepository;
        this.teamDtoMapper = teamDtoMapper;
    }

    @Override
    public List<UserDto> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(userDtoMapper)
                .toList();
    }

    @Override
    public List<UserDto> getAllUsersFilteredByTeam(boolean isAgent) {
        List<User> users;
        if (isAgent)
            users = userRepository.findAllByTeamIsNotNull();
        else
            users = userRepository.findAllByTeamIsNull();

       return users
                .stream()
                .map(userDtoMapper)
                .toList();
    }

    @Override
    public List<TeamsDto> getAllTeams() {
        return teamRepository.findAll()
                .stream()
                .map(teamDtoMapper)
                .toList();
    }

    @Override
    public TeamsDto getTeam(Long teamId) {
        return teamRepository.findById(teamId)
                .map(teamDtoMapper)
                .orElseThrow(() -> new ResourceNotFoundException("Error, team with id '%d' not found, check and try again"
                        .formatted(teamId)));
    }

    @Override
    public TeamsDto createTeam(CreateTeamRequest request) {
        var user = userRepository.findByIdWithTeam(request.teamLead())
                .orElseThrow(() -> new ResourceNotFoundException("Error, user with id '%d' not found, check and try again"
                        .formatted(request.teamLead())));

        checkIfUserIsAlreadyTeamLead(request, user);

        var team = new Team(request.name());
        team.setTeamLead(user);

        return teamDtoMapper.apply(teamRepository.save(team));
    }

    private void checkIfUserIsAlreadyTeamLead(CreateTeamRequest request, User user) {
        var tempTeam = teamRepository.findByTeamLead(user);
        if (tempTeam.isPresent())
            throw new ForbiddenOperationException("Forbidden, user with id '%d' is already a team lead for another team"
                    .formatted(request.teamLead()));
    }
}
