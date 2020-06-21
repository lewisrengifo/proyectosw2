package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.mail.internet.MimeMessage;

@Service
public class SendMailService {
    @Autowired
    JavaMailSender javaMailSender;

    @GetMapping("/")
    public String index() {
        return "redirect:/login";
    }

    //@PostMapping("/sendMail")
    //public void sendMail(String correoDestino, String correoOrigen, String subject, String bodyMensaje) {


       // SimpleMailMessage mailMessage = new SimpleMailMessage();
       // mailMessage.setFrom(correoOrigen);
       // mailMessage.setTo(correoDestino);
      //  mailMessage.setSubject(subject);
      //  mailMessage.setText(bodyMensaje);
       // javaMailSender.send(mailMessage);

    //}
    @PostMapping("/sendMail")
    public void sendMail(String correoDestino, String correoOrigen, String subject, String bodyMensaje) {
        MimeMessagePreparator messagePreparator = mimeMessage -> {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, false, "utf-8");
            messageHelper.setFrom(correoOrigen);
            messageHelper.setTo(correoDestino);
            messageHelper.setSubject(subject);
            messageHelper.setText(bodyMensaje, true);
        };
        javaMailSender.send(messagePreparator);
    }
}
