package com.dokanne.DokaneeBackend.service;

import lombok.AllArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;

@AllArgsConstructor
@Service
public class EmailService {
    private final JavaMailSender javaMailSender;
    PasswordEncoder encoder;

    public String sendMail(String sendTo, String subject, String body) throws MessagingException, IOException {

        MimeMessage msg = javaMailSender.createMimeMessage();

        MimeMessageHelper helper = new MimeMessageHelper(msg, true);
        helper.setFrom("dokaneeteam@gmail.com", "Dokanee Team");

        helper.setTo(sendTo);

        helper.setSubject(subject);

        helper.setText(body, true);

        javaMailSender.send(msg);
        return "Send to " + sendTo;
    }

}
