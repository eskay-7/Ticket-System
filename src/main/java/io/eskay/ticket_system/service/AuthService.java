package io.eskay.ticket_system.service;

import io.eskay.ticket_system.dto.request.LoginRequest;
import io.eskay.ticket_system.dto.request.RegisterationRequest;
import io.eskay.ticket_system.dto.response.AuthResponse;
import io.eskay.ticket_system.dto.response.UserDto;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.transaction.annotation.Transactional;

public interface AuthService {
    AuthResponse login(LoginRequest request, HttpServletResponse response);

    AuthResponse refreshToken(String refreshToken);

    void registerUser(RegisterationRequest request);

    void registerAgent(RegisterationRequest request);

    void generateCode(String email);

}
