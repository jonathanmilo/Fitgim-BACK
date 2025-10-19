package com.fit.demo.auth.service;

import java.time.Instant;
import com.fit.demo.auth.dto.RegisterRequest;
import com.fit.demo.auth.dto.TokenResponse;
import com.fit.demo.Users.entidades.UserResponse;
import com.fit.demo.auth.dto.LoginRequest;
import com.fit.demo.auth.util.JwtUtil;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import com.fit.demo.Users.entidades.User;
import com.fit.demo.Users.repositry.UserRepository;
import com.fit.demo.Users.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.fit.demo.exception.RecursoNoEncontradoException;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final OtpService otpService;

    public TokenResponse login(LoginRequest request) {
        // flujo de login sin 2FA
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RecursoNoEncontradoException());

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Credenciales inválidas");
        }

        String accessToken = jwtUtil.generateToken(user.getNombre());
        System.out.println("Access Token generado: " + accessToken); // Para depuración
        String refreshToken = jwtUtil.generateRefreshToken(user.getNombre());
        String userId = user.getId().toString();
        String nombre = user.getNombre();
        String email = user.getEmail();

        return new TokenResponse(accessToken, refreshToken, userId, nombre, email);
    }

    public void loginStartSendOtp(LoginRequest request) {
        // 1) Flujo de login con 2FA
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RecursoNoEncontradoException());
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Credenciales inválidas");
        }
        try {
            otpService.generateAndSendOtp(request.getEmail());
        } catch (MessagingException e) {
            throw new RuntimeException("No se pudo enviar OTP: " + e.getMessage(), e);
        }
    }

    public TokenResponse verifyOtpAndAuthenticate(String email, String otp) {
        if (email == null || email.isBlank() || otp == null || otp.isBlank()) {
            System.out.println("Email y OTP son requeridos");
            throw new RuntimeException("Email y OTP son requeridos");
        }

        boolean ok;
        try {
            ok = otpService.validateAndConsumeOtp(email.trim(), otp.trim());
        } catch (Exception e) {
            System.out.println("Error al validar OTP:");
            throw new RuntimeException("Error al validar OTP: " + e.getMessage(), e);
        }

        if (!ok) {
            System.out.println("OTP no valido o expirado");
            throw new RuntimeException("OTP inválido o expirado");
        }

        User user = userRepository.findByEmail(email.trim())
                .orElseThrow(() -> new RecursoNoEncontradoException());

        String accessToken = jwtUtil.generateToken(user.getNombre());
        String refreshToken = jwtUtil.generateRefreshToken(user.getNombre());
        String userId = user.getId().toString();
        String nombre = user.getNombre();

        return new TokenResponse(accessToken, refreshToken, userId, nombre, user.getEmail());
    }

//    public TokenResponse verifyOtpAndAuthenticate(String email, String otp) {
//        boolean ok = otpService.validateAndConsumeOtp(email, otp);
//        if (!ok) throw new RuntimeException("OTP inválido o expirado");
//
//        User user = userRepository.findByEmail(email)
//                .orElseThrow(() -> new RecursoNoEncontradoException());
//
//        String accessToken = jwtUtil.generateToken(user.getNombre());
//        String refreshToken = jwtUtil.generateRefreshToken(user.getNombre());
//        String userId = user.getId().toString();
//        String nombre = user.getNombre();
//
//        return new TokenResponse(accessToken, refreshToken, userId, nombre, email);
//    }

    public UserResponse createUser(RegisterRequest registerRequest) {
        UserResponse resp = userService.createUser(registerRequest);
        try {
            // Enviar OTP tras crear usuario (registro)
            otpService.generateAndSendOtp(registerRequest.getEmail());
        } catch (MessagingException e) {
            throw new RuntimeException("No se pudo enviar OTP: " + e.getMessage(), e);
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
            throw new RuntimeException("No se pudo refrescar el token: " + e.getMessage());
        }
    }
        private boolean isPasswordEncrypted(String password) {
        // Una heurística simple: si no tiene un formato de hash (e.g., $2a$ para BCrypt), asumimos que no está encriptada
        return password != null && password.matches("^\\$2[ayb]\\$.+");
    }
}
