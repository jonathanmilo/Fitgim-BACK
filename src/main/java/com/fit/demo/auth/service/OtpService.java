package com.fit.demo.auth.service;

import com.fit.demo.Users.entidades.User;
import com.fit.demo.Users.repositry.UserRepository;
import com.fit.demo.auth.dto.EmailDTO;
import jakarta.mail.MessagingException;
import org.springframework.stereotype.Service;

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
            throw new RuntimeException("Usuario no encontrado para enviar OTP");
        }
        User user = opt.get();

        String otp = String.format("%06d", secureRandom.nextInt(1_000_000));
        LocalDateTime expiry = LocalDateTime.now().plusMinutes(5);

        user.setOtpCode(otp);
        user.setOtpExpiryDate(expiry);
        userRepository.save(user);

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