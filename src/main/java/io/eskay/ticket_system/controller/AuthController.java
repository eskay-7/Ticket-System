package io.eskay.ticket_system.controller;

import io.eskay.ticket_system.dto.request.LoginRequest;
import io.eskay.ticket_system.dto.request.RegisterationRequest;
import io.eskay.ticket_system.dto.response.AuthResponse;
import io.eskay.ticket_system.exception.ExceptionResponse;
import io.eskay.ticket_system.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
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

    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Register a new account",
            description = "Registers a new user using data provided in request-body")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "CREATED",
                    content = @Content(examples = @ExampleObject(value = "Success"))),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class),
                            examples = @ExampleObject(value = """
                           {
                                "status": 401,
                                "error": "UNAUTHORIZED",
                                "message": "Authentication failed",
                                "timestamp": 1754469336056
                           }
                           """))),
    })
    @PostMapping("/register")
    public ResponseEntity<?> register(
            @RequestBody @Valid RegisterationRequest request
    ) {
        authService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body("Success");
    }


    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Login into account",
            description = "Authenticates a user using data provided in request-body")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(schema = @Schema(implementation = AuthResponse.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class),
                            examples = @ExampleObject(value = """
                           {
                                "status": 401,
                                "error": "UNAUTHORIZED",
                                "message": "Authentication failed",
                                "timestamp": 1754469336056
                           }
                           """))),
    })
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(
            @RequestBody @Valid LoginRequest request,
            HttpServletResponse httpResponse) {
        var response = authService.login(request, httpResponse);

        return ResponseEntity.ok(response);
    }

    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Refresh users access_token",
            description = "This endpoint is used to refresh a users expired_access token by providing the refresh_token as a cookie")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(schema = @Schema(implementation = AuthResponse.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class),
                            examples = @ExampleObject(value = """
                           {
                                "status": 401,
                                "error": "UNAUTHORIZED",
                                "message": "Authentication failed",
                                "timestamp": 1754469336056
                           }
                           """))),
    })
    @PostMapping("/refresh_token")
    public ResponseEntity<AuthResponse> refreshToken(
            @CookieValue("refresh_token") String refreshToken
    ) {
        var response = authService.refreshToken(refreshToken);

        return ResponseEntity.ok(response);
    }
}
