package io.eskay.ticket_system.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.eskay.ticket_system.exception.ExceptionResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Value("${cors.frontend.url}")
    private String frontendUrl;

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .exceptionHandling(handler -> handler.authenticationEntryPoint(authenticationEntryPoint()))
                .exceptionHandling(handler -> handler.accessDeniedHandler(accessDeniedHandler()))
                .authorizeHttpRequests(req -> req
                        .requestMatchers("/api/info/**").permitAll()
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers("/api/tickets/**").hasAnyRole("USER","AGENT","ADMIN")
                        .requestMatchers("/api/agents/**").hasRole("AGENT")
                        .requestMatchers("/swagger-ui/**", "/swagger-resources/**",
                                "/v3/api-docs/**", "/swagger-ui.html").permitAll()
                        .anyRequest().authenticated())

                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        var corsConfig = new CorsConfiguration();
        corsConfig.addAllowedOrigin(frontendUrl);
        corsConfig.setAllowedMethods(List.of("GET","POST","PUT","PATCH","DELETE","OPTIONS"));
        corsConfig.addAllowedHeader("*");

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/api/**",corsConfig);
        return source;
    }

    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint() {
        return (request, response, ex) -> {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setHeader(HttpHeaders.WWW_AUTHENTICATE, "");

            var jwtException = (Throwable) request.getAttribute("jwt_exception");
            String message = jwtException == null? "Authentication failed" : jwtException.getMessage();

            var error = new ExceptionResponse(
                    HttpStatus.UNAUTHORIZED.value(), HttpStatus.UNAUTHORIZED,
                    message, Timestamp.valueOf(LocalDateTime.now())
            );
            response.getWriter().write(formatException(error));
        };
    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return (request, response, ex) -> {
            response.setStatus(HttpStatus.FORBIDDEN.value());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setHeader(HttpStatus.FORBIDDEN.name(), "");

            var error = new ExceptionResponse(
                    HttpStatus.FORBIDDEN.value(), HttpStatus.FORBIDDEN,
                    ex.getMessage(), Timestamp.valueOf(LocalDateTime.now())
            );
            response.getWriter().write(formatException(error));
        };
    }

    private <T> String formatException(T data) throws JsonProcessingException {
        var objMapper = new ObjectMapper();
        return objMapper.writeValueAsString(data);
    }
}
