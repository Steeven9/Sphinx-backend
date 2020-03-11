package ch.usi.inf.sa4.sphinx.controller;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class Mailer {

    public JavaMailSender mailSender;

    public void send(String to, String subject, String message) {

        SimpleMailMessage mail = new SimpleMailMessage();

        mail.setTo(to);
        mail.setSubject(subject);
        mail.setText(message);

        // TODO: maybe put this into a configuration file somewhere somehow?
        mail.setFrom("info@smarthut.xyz");

        mailSender.send(mail);
    }

}
