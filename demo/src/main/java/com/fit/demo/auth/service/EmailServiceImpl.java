package com.fit.demo.auth.service;

import com.fit.demo.auth.dto.EmailDTO;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
public class EmailServiceImpl implements IEmailService{

    private final JavaMailSender javaMailSender;
    private final TemplateEngine templateEngine;

    public EmailServiceImpl(JavaMailSender javaMailSender, TemplateEngine templateEngine) {
        this.javaMailSender = javaMailSender;
        this.templateEngine = templateEngine;
    }

    @Override
    public void sendMail(EmailDTO emailDTO) throws MessagingException {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setTo(emailDTO.getDestinatary());
            helper.setSubject(emailDTO.getSubject());
            //helper.setText(emailDTO.getBody(), true); para enviar texto plano, no es el caso
            Context context = new Context();
            context.setVariable("message", emailDTO.getBody());
            String html = templateEngine.process("otpEmail", context);

            helper.setText(html, true);
            javaMailSender.send(message);
        }catch (Exception e){
            throw new RuntimeException("Error al enviar el correo: " + e.getMessage(), e);
        }

    }
}
