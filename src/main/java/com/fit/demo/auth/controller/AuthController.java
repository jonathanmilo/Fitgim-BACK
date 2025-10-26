package com.fit.demo.auth.controller;

import com.fit.demo.auth.dto.*;
import com.fit.demo.Users.entidades.UserResponse;
import com.fit.demo.auth.service.IEmailService;
import com.fit.demo.auth.service.AuthService;
import com.fit.demo.auth.service.OtpService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import com.fit.demo.exception.UnauthorizedException;
@RestController
@RequiredArgsConstructor
@RequestMapping("api/auth")
public class AuthController {
    private final AuthService authService;
    private final IEmailService emailService;
    private final OtpService otpService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
            try {
                TokenResponse tokenResponse = authService.login(loginRequest);
                return ResponseEntity.ok(tokenResponse);
            } catch (UnauthorizedException e) {
                return ResponseEntity.status(e.getStatusCode()).body(e.getBody());
            }
        }

    @PostMapping("/login-2fa")
    public ResponseEntity<Void> login2fa(@RequestBody LoginRequest loginRequest) {
        authService.login2fa(loginRequest);
        return ResponseEntity.ok().build();
    }

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
    public TokenResponse refreshToken(@RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader) {
        return authService.refreshToken(authHeader);
    }

    @PostMapping("/send-mail")
    public ResponseEntity<String> sendEmail(@RequestBody EmailDTO emailDTO) throws MessagingException {
        emailService.sendMail(emailDTO);
        return new ResponseEntity<>(" Email sent successfully", HttpStatus.OK);
    }

    @PostMapping("/send-otp")
    public ResponseEntity<Void> sendOtp(@RequestBody SendOtpRequest request) throws  MessagingException {
        otpService.generateAndSendOtp(request.getEmail());
        return ResponseEntity.ok().build();
    }
}