package com.fit.demo.auth.controller;

import com.fit.demo.auth.dto.EmailDTO;
import com.fit.demo.auth.dto.LoginRequest;
import com.fit.demo.auth.dto.OtpVerifyRequest;
import com.fit.demo.auth.dto.RegisterRequest;
import com.fit.demo.auth.dto.TokenResponse;
import com.fit.demo.Users.entidades.UserResponse;
import com.fit.demo.auth.service.IEmailService;
import com.fit.demo.auth.service.AuthService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/auth")
public class AuthController {
    private final AuthService authService;
    private final IEmailService emailService;

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(authService.login(loginRequest));
    }

    @PostMapping("/login-2fa")
    public ResponseEntity<Void> login2fa(@RequestBody LoginRequest loginRequest) {
        authService.login2fa(loginRequest);
        return ResponseEntity.ok().build();
    }

    // Endpoint unificado para verificar OTP (tanto register como login)
    @PostMapping("/verify-otp")
    public ResponseEntity<TokenResponse> verifyOtp(@RequestBody OtpVerifyRequest req) {
        TokenResponse tokens = authService.verifyOtpAndAuthenticate(req.getEmail(), req.getOtp());
        return ResponseEntity.ok(tokens);
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@RequestBody RegisterRequest registerRequest) {
        return ResponseEntity.ok(authService.createUser(registerRequest));
    }

    @PostMapping("/refresh")
    public void refreshToken(@RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader) {
        // TODO: terminar esto
    }

    @PostMapping("/send-mail")
    public ResponseEntity<String> sendEmail(@RequestBody EmailDTO emailDTO) throws MessagingException {
        emailService.sendMail(emailDTO);
        return new ResponseEntity<>(" Email sent successfully", HttpStatus.OK);
    }
}