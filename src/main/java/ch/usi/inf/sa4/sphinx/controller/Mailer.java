package ch.usi.inf.sa4.sphinx.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class Mailer {

    @Autowired
    public JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromAddress;

    public void send(String to, String subject, String message) {

        SimpleMailMessage mail = new SimpleMailMessage();

        mail.setTo(to);
        mail.setSubject(subject);
        mail.setText(message);
        mail.setFrom(fromAddress);

        mailSender.send(mail);
    }

}
