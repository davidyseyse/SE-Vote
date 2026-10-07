package com.isslab.se_form_backend.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Date;
import java.util.List;

@Slf4j
public class JwtUtil {

    private final String SECRET_KEY;
    private final long EXPIRATION_MS = 60 * 60 * 1000;
    private final long RESRT_PASSWORD_EXPIRATION_MS = 15 * 60 * 1000;

    public JwtUtil(String SECRET_KEY) {
        log.info("{}", SECRET_KEY);
        this.SECRET_KEY = SECRET_KEY;
    }

    public String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .claim("roles", List.of("ROLE_USER"))
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_MS))
                .signWith(Keys.hmacShaKeyFor(SECRET_KEY.getBytes()), SignatureAlgorithm.HS256)
                .compact();
    }

    public String generateAdminToken() {
        return Jwts.builder()
                .setSubject("admin")
                .claim("roles", List.of("ROLE_ADMIN","ROLE_USER"))
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_MS))
                .signWith(Keys.hmacShaKeyFor(SECRET_KEY.getBytes()), SignatureAlgorithm.HS256)
                .compact();
    }

//    public String generateResetPasswordToken(String username) {
//        return Jwts.builder()
//                .setSubject(username)
//                .claim("purpose", "RESET_PASSWORD")
//                .setExpiration(new Date(System.currentTimeMillis() + RESRT_PASSWORD_EXPIRATION_MS))
//                .signWith(Keys.hmacShaKeyFor(SECRET_KEY.getBytes()), SignatureAlgorithm.HS256)
//                .compact();
//    }

    public String extractUsername(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY.getBytes())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public boolean isValid(String token) {
        try {
            Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(SECRET_KEY.getBytes()))
                .build()
                .parseClaimsJws(token);
            return true;
        } catch (JwtException e) {
            return false;
        }
    }

    public boolean validateResetPasswordToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(SECRET_KEY.getBytes()))
                .build()
                .parseClaimsJws(token)
                .getBody();

        return claims.get("purpose", String.class).equals("RESET_PASSWORD")
                && claims.getExpiration().after(new Date());
    }

    public Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8)))
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
