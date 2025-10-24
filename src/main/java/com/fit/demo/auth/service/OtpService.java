package com.fit.demo.auth.service;

import com.fit.demo.Users.entidades.User;
import com.fit.demo.Users.repositry.UserRepository;
import com.fit.demo.auth.dto.EmailDTO;
import com.fit.demo.exception.UnauthorizedException;
import jakarta.mail.MessagingException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class OtpService {

    private final UserRepository userRepository;
    private final IEmailService emailService;
    private final SecureRandom secureRandom;

    public OtpService(UserRepository userRepository, IEmailService emailService) {
        this.userRepository = userRepository;
        this.emailService = emailService;
        SecureRandom sr;
        try {
            sr = SecureRandom.getInstanceStrong();
        } catch (Exception e) {
            sr = new SecureRandom();
        }
        this.secureRandom = sr;
    }

    public void generateAndSendOtp(String email) throws MessagingException {
        Optional<User> opt = userRepository.findByEmail(email);
        if (opt.isEmpty()) {
            String errorMsg = "Email no encontrado para enviar OTP: " + email;
            System.err.println("[OtpService]: " + errorMsg);
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, errorMsg);
        }
        User user = opt.get();

        String otp = String.format("%06d", secureRandom.nextInt(1_000_000));
        LocalDateTime expiry = LocalDateTime.now().plusMinutes(5);

        user.setOtpCode(otp);
        user.setOtpExpiryDate(expiry);
        try {
            userRepository.save(user);
        } catch (DataIntegrityViolationException e) {
            String errorMsg = "Error al guardar OTP para el usuario con email: " + email;
            System.err.println("[OtpService]: " + errorMsg);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST ,errorMsg);
        }

        EmailDTO dto = new EmailDTO();
        dto.setDestinatary(user.getEmail());
        dto.setSubject("Tu código OTP");
        dto.setBody(otp);

        emailService.sendMail(dto);
    }

    public boolean validateAndConsumeOtp(String email, String inputOtp) {
        Optional<User> opt = userRepository.findByEmail(email);
        if (opt.isEmpty()) return false;

        User user = opt.get();
        String storedOtp = user.getOtpCode();
        LocalDateTime expiry = user.getOtpExpiryDate();
        LocalDateTime now = LocalDateTime.now();

        if (storedOtp == null || inputOtp == null) return false;
        if (!storedOtp.equals(inputOtp)) return false;
        if (expiry == null || expiry.isBefore(now)) return false;

        // Consumir OTP
        user.setOtpCode(null);
        user.setOtpExpiryDate(null);

        // Si venía por registro, activar usuario
        if (!user.isEnabled()) {
            user.setEnabled(true);
        }

        userRepository.save(user);
        return true;
    }
}