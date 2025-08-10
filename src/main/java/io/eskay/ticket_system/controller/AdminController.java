package io.eskay.ticket_system.controller;

import io.eskay.ticket_system.dto.request.CreateTeamRequest;
import io.eskay.ticket_system.dto.response.TeamsDto;
import io.eskay.ticket_system.dto.response.UserDto;
import io.eskay.ticket_system.service.AdminService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    /*get all users
    * get users without a team
    * get agents*/

    @GetMapping("/users")
    public List<UserDto> getAllUsers(
            @RequestParam(
                    name = "is_agent",
                    required = false)
            Boolean isAgent
    ) {
        List<UserDto> users;
        if (isAgent == null)
            users = adminService.getAllUsers();
        else
            users = adminService.getAllUsersFilteredByTeam(isAgent);

        return users;
    }

    @GetMapping("/teams")
    public List<TeamsDto> getAllTeams() {
        return adminService.getAllTeams();
    }

    @GetMapping("/teams/{team_id}")
    public ResponseEntity<TeamsDto> getTeam(@PathVariable(name = "team_id") Long teamId) {
        var foundTeam = adminService.getTeam(teamId);
        return ResponseEntity.ok(foundTeam);
    }

    @PostMapping("/teams")
    public ResponseEntity<TeamsDto> createTeam(@RequestBody @Valid CreateTeamRequest request) {
        var createdTeam = adminService.createTeam(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTeam);
    }

//    @DeleteMapping("/teams/{team_id}")
//    public ResponseEntity<?> deleteTeam(@PathVariable(name = "team_id") Long teamId) {
//        adminService.deleteTeam()
//        return ResponseEntity.noContent().build();
//    }
}
