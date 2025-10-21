package com.fit.demo.auth.service;

import com.fit.demo.auth.dto.EmailDTO;
import jakarta.mail.MessagingException;

public interface IEmailService {

    public void sendMail(EmailDTO emailDTO) throws MessagingException;
}
