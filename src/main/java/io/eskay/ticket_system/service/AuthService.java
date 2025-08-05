package io.eskay.ticket_system.service;

import io.eskay.ticket_system.dto.request.LoginRequest;
import io.eskay.ticket_system.dto.request.RegisterationRequest;
import io.eskay.ticket_system.dto.response.AuthResponse;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

public interface AuthService {
    AuthResponse login(LoginRequest request, HttpServletResponse response);

    AuthResponse refreshToken(String refreshToken);

    void register(RegisterationRequest request);
}
