package gr.hua.dit.rentEstate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendRegisterNotification(String toEmail, String username) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("Welcome to RentEstate");
        message.setText("Hello " + username + ",\n\nYou have successfully created your account.");
        message.setFrom("rentestate.app@gmail.com");
        mailSender.send(message);
    }
}
