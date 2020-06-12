package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Service
public class SendMailService {
    @Autowired
    JavaMailSender javaMailSender;

    @GetMapping("/")
    public String index() {
        return "redirect:/login";
    }


    @PostMapping("/sendMail")
    public void sendMail(String correoDestino, String correoOrigen, String subject, String bodyMensaje) {

        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom(correoOrigen);
        mailMessage.setTo(correoDestino);
        mailMessage.setSubject(subject);
        mailMessage.setSubject(bodyMensaje);

        javaMailSender.send(mailMessage);
    }
}
