package io.eskay.ticket_system.service;

import io.eskay.ticket_system.dto.request.LoginRequest;
import io.eskay.ticket_system.dto.request.RegisterationRequest;
import io.eskay.ticket_system.dto.response.AuthResponse;
import io.eskay.ticket_system.dto.response.UserDto;
import io.eskay.ticket_system.entity.Role;
import io.eskay.ticket_system.entity.User;
import io.eskay.ticket_system.exception.AccountAlreadyExistsException;
import io.eskay.ticket_system.exception.ResourceNotFoundException;
import io.eskay.ticket_system.mapper.RegistrationRequestMapper;
import io.eskay.ticket_system.mapper.UserDtoMapper;
import io.eskay.ticket_system.repository.TeamRepository;
import io.eskay.ticket_system.repository.UserRepository;
import io.eskay.ticket_system.util.AuthenticatedUser;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserDtoMapper userDtoMapper;
    @Value("${jwt.refresh.expiration.time}")
    private Integer JWT_REFRESH_EXPIRATION_TIME;

    private final UserRepository repository;
    private final RegistrationRequestMapper requestMapper;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final TeamRepository teamRepository;

    public AuthServiceImpl(
            UserRepository repository,
            RegistrationRequestMapper requestMapper,
            JwtService jwtService,
            AuthenticationManager authenticationManager,
            TeamRepository teamRepository,
            UserDtoMapper userDtoMapper
    ) {
        this.repository = repository;
        this.requestMapper = requestMapper;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.teamRepository = teamRepository;
        this.userDtoMapper = userDtoMapper;
    }

    @Override
    public AuthResponse login(LoginRequest request, HttpServletResponse httpResponse) {
//       var user = repository.findByEmailWithTeam(request.email())
//                .orElseThrow(() -> new ResourceNotFoundException("Error, account not found"));
        var auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.email(),request.password()));

        var user = (User) auth.getPrincipal();
        var refreshToken = jwtService.generateRefreshToken(user);
        httpResponse.addCookie(createCookie(refreshToken));

        return new AuthResponse(jwtService.generateAccessToken(user));
    }

    @Override
    public AuthResponse refreshToken(String refreshToken) {
        var userEmail = jwtService.extractUsername(refreshToken);
        var user = repository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Error, account not found"));

        jwtService.isTokenValid(refreshToken, user);

        var accessToken = jwtService.generateAccessToken(user);
        return new AuthResponse(accessToken);
    }

    @Override
    @Transactional
    public void registerUser(RegisterationRequest request) {
        var foundUser = repository.findByEmail(request.email());
        if (foundUser.isPresent())
            throw new AccountAlreadyExistsException("Error, you cannot register with this email, kindly use another one");

        var user = requestMapper.apply(request);
        user.addRole(Role.USER);
        repository.save(user);
    }

    @Override
    @Transactional
    public void registerAgent(RegisterationRequest request) {

        var team = teamRepository.findById(request.teams_id())
                .orElseThrow(()-> new IllegalArgumentException("Error, no teams_id found with id %d"
                        .formatted(request.teams_id())));

        var foundUser = repository.findByEmail(request.email());
        if (foundUser.isPresent())
            throw new AccountAlreadyExistsException("Error, you cannot register with this email, kindly use another one");

        var user = requestMapper.apply(request);
        user.addRole(Role.AGENT);
        user.setTeam(team);
        repository.save(user);
    }

    @Override
    public void generateCode(String email) {
        System.out.println();
    }


    private Cookie createCookie(String refreshToken) {
        var cookie = new Cookie("refresh_token", refreshToken);
        cookie.setHttpOnly(true);
        cookie.setPath("/api/auth/refresh_token");
        cookie.setMaxAge(JWT_REFRESH_EXPIRATION_TIME);

        return cookie;
    }
}
