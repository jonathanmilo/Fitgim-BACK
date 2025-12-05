package com.fit.demo.auth.service;

import com.fit.demo.auth.dto.RegisterRequest;
import com.fit.demo.auth.dto.TokenResponse;
import com.fit.demo.Users.entidades.UserResponse;
import com.fit.demo.auth.dto.LoginRequest;
import com.fit.demo.auth.util.JwtUtil;
import com.fit.demo.exception.DisabledUserException;
import com.fit.demo.exception.UnauthorizedException;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import main.java.com.fit.demo.exception.RefreshInvalidoException;

import com.fit.demo.Users.entidades.User;
import com.fit.demo.Users.repositry.UserRepository;
import com.fit.demo.Users.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final OtpService otpService;

    public TokenResponse login(LoginRequest request) {
        User user = validateUserCredentials(request);

        String accessToken = jwtUtil.generateToken(user.getNombre());
        String refreshToken = jwtUtil.generateRefreshToken(user.getNombre());
        String userId = user.getId().toString();
        String nombre = user.getNombre();
        String email = user.getEmail();
        System.out.println("Access Token generado: " + accessToken);

        return new TokenResponse(accessToken, refreshToken, userId, nombre, email);
    }

    public void login2fa(LoginRequest request) {
        validateUserCredentials(request);

        try {
            otpService.generateAndSendOtp(request.getEmail());
        } catch (MessagingException e) {
            throw new RuntimeException("No se pudo enviar OTP: " + e.getMessage(), e);
        }
    }

    public TokenResponse verifyOtpAndAuthenticate(String email, String otp) {
        if (email == null || email.isBlank() || otp == null || otp.isBlank()) {
            String errorMsg = "Email y OTP son requeridos";
            System.err.println("[AuthService]: " + errorMsg);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, errorMsg);
        }

        User user = userRepository.findByEmail(email.trim())
                .orElseThrow(() -> new UnauthorizedException());

        boolean ok;
        ok = otpService.validateAndConsumeOtp(email.trim(), otp.trim());

        if (!ok) {
            String errorMsg = "OTP inválido o expirado";
            System.err.println("[AuthService]: " + errorMsg);
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, errorMsg);
        }

        String accessToken = jwtUtil.generateToken(user.getNombre());
        String refreshToken = jwtUtil.generateRefreshToken(user.getNombre());
        String userId = user.getId().toString();
        String nombre = user.getNombre();
        System.out.println("Access Token generado: " + accessToken);

        return new TokenResponse(accessToken, refreshToken, userId, nombre, user.getEmail());
    }


    public UserResponse createUser(RegisterRequest registerRequest) {
        UserResponse resp = userService.createUser(registerRequest);
        try {
            otpService.generateAndSendOtp(registerRequest.getEmail());
        } catch (MessagingException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No se pudo enviar OTP: " + e.getMessage(), e);
        }
        return resp;
    }

    public TokenResponse refreshToken(String refreshToken) {
        try {
            // Validar el refresh token
            String username = jwtUtil.extractUsername(refreshToken);
            if (jwtUtil.validateToken(refreshToken, username)) {
                User user = userRepository.findByNombre(username)
                        .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

                String newAccessToken = jwtUtil.generateToken(user.getNombre());
                String newRefreshToken = jwtUtil.generateRefreshToken(user.getNombre());
                String userId = user.getId().toString();
                String nombre = user.getNombre();
                String email = user.getEmail();

                return new TokenResponse(newAccessToken, newRefreshToken, userId, nombre, email);
            } else {
                throw new RuntimeException("Refresh token inválido o expirado");
            }
        } catch (Exception e) {
            if (e.getMessage().contains("JWT expired")) {
                throw new RefreshInvalidoException();
            }
            throw new RuntimeException("No se pudo refrescar el token: " + e.getMessage());
        }
    }

    private User validateUserCredentials(LoginRequest loginRequest) {
        User user = userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new UnauthorizedException());
        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new UnauthorizedException();
        }
        if (!user.isEnabled()) {
            throw new DisabledUserException();
        }
        return user;
    }

    private boolean isPasswordEncrypted(String password) {
        // Una heurística simple: si no tiene un formato de hash (e.g., $2a$ para BCrypt), asumimos que no está encriptada
        return password != null && password.matches("^\\$2[ayb]\\$.+");
    }
}
