package io.eskay.ticket_system.controller;

import io.eskay.ticket_system.dto.request.LoginRequest;
import io.eskay.ticket_system.dto.request.RegisterationRequest;
import io.eskay.ticket_system.dto.response.AuthResponse;
import io.eskay.ticket_system.service.AuthService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(
            @RequestBody @Valid RegisterationRequest request
    ) {
        authService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body("Success");
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(
            @RequestBody @Valid LoginRequest request,
            HttpServletResponse httpResponse) {
        var response = authService.login(request, httpResponse);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/refresh_token")
    public ResponseEntity<AuthResponse> refreshToken(
            @CookieValue("refresh_token") String refreshToken
    ) {
        var response = authService.refreshToken(refreshToken);

        return ResponseEntity.ok(response);
    }
}
