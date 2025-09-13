package com.fit.demo.auth.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.fit.demo.Users.entidades.User;
import com.fit.demo.Users.repositry.UserRepository;
import com.fit.demo.exception.RecursoNoEncontradoException;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

    private final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    private final long expirationTime = 1000 * 60 * 60; // 1 hora

    public String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(key)
                .compact();
    }

    public String extractUsername(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }
    
    public User getUserFromToken(String token,UserRepository userRepository) {
        String username= Jwts.parserBuilder().setSigningKey(key).build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
        return userRepository.findByNombre(username)
                .orElseThrow(() -> new RecursoNoEncontradoException());


    }
    public boolean validateToken(String token, String username) {
        try {
            // Parsear el token y obtener las claims
            var claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            String tokenUsername = claims.getSubject(); // subject = username
            return (tokenUsername.equals(username) && !isTokenExpired(claims));

        } catch (JwtException e) {
            return false;
        }
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Método auxiliar para verificar expiración
    private boolean isTokenExpired(Claims claims) {
        return claims.getExpiration().before(new Date());
    }
}