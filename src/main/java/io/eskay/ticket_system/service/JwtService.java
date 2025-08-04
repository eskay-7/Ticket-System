package io.eskay.ticket_system.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;

@Service
public class JwtService {

    @Value("${jwt.secret.key}")
    private String JWT_SECRET_KEY;

    @Value("${jwt.access.expiration.time}")
    private Long JWT_ACCESS_EXPIRATION_TIME;

    @Value("${jwt.refresh.expiration.time}")
    private Long JWT_REFRESH_EXPIRATION_TIME;

    public boolean isTokenValid(String token, UserDetails user) {
        return (user.getUsername().equals(extractUsername(token)) && !isTokenExpired(token));
    }

    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractAllClaims(token).getExpiration();
    }

    private Claims extractAllClaims(String token) {
        return Jwts
                .parser()
                .verifyWith(getKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public String generateAccessToken(UserDetails user) {
        return generateToken(user, JWT_ACCESS_EXPIRATION_TIME);
    }

    public String generateRefreshToken(UserDetails user) {
        return generateToken(user, JWT_REFRESH_EXPIRATION_TIME);
    }

    private String generateToken(UserDetails user, Long expiration) {
        var claims = new HashMap<String, Object>();

        claims.put(
                "roles",
                user.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .toList());

        return Jwts
                .builder()
                .claims(claims)
                .subject(user.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getKey())
                .compact();
    }

    private SecretKey getKey() {
        byte[] keyBytes = Decoders.BASE64URL.decode(JWT_SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
