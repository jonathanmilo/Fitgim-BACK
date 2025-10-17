
package com.fit.demo.auth.controller;
import com.fit.demo.auth.dto.EmailDTO;
import com.fit.demo.auth.dto.LoginRequest;
import com.fit.demo.auth.dto.RegisterRequest;
import com.fit.demo.auth.dto.TokenResponse;
import com.fit.demo.Users.entidades.UserResponse;

import com.fit.demo.auth.service.IEmailService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.fit.demo.auth.service.AuthService;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/auth")
public class AuthController {
    private final AuthService authService;
    @Autowired
    private IEmailService emailService;

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> authenticate(@RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(authService.login(loginRequest));
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@RequestBody RegisterRequest registerRequest) {
        return ResponseEntity.ok(authService.createUser(registerRequest));
    }
    // TODO:terminar esto
    @PostMapping("/refresh")
    public void refreshToken(@RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader) {

    }

    @PostMapping("/send-mail")
    public ResponseEntity<String> sendEmail(@RequestBody EmailDTO emailDTO) throws MessagingException {
        emailService.sendMail(emailDTO);
        return new ResponseEntity<>(" Email sent successfully", HttpStatus.OK);
    }
}
