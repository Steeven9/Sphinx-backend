package ch.usi.inf.sa4.sphinx.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;

public class Mailer implements EmailService {
    @Autowired
    public JavaMailSender mailSender;

    public void send(String to, String subject, String message) {

    }
}
