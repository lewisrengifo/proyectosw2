package com.example.demo.Controllers;

import com.example.demo.Entity.Usuario;
import com.example.demo.Repository.UsuarioRepository;
import com.example.demo.service.SendMailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;
import java.util.Random;

@Controller
public class SendMailController {
    @Autowired
    UsuarioRepository usuarioRepository;

    @Autowired
    SendMailService sendMailService;

    @PostMapping("/enviarMail")
    public void enviarCorreo(@RequestParam("correo") String correoDestino) {

        Usuario optional = usuarioRepository.findByCorreo(correoDestino);
        String subject;
        String mensaje;
        if (optional.getCorreo() != "") {
            String caracteres1 = "abcdefghijklmnopqrtsuvwxyz1234567890";
            String caracteres2 = "1234567890";
            Random random = new Random();
            StringBuilder aux = new StringBuilder();
            StringBuilder aux2 = new StringBuilder();
            for (int i = 0; i < 8; i++) {
                aux.append(caracteres1.charAt(random.nextInt(caracteres1.length())));
            }
            for (int i = 0; i < 2; i++) {
                aux2.append(caracteres2.charAt(random.nextInt(caracteres2.length())));
            }
            String contraGenerada = aux.toString() + aux2.toString();
            subject = "Recuperacion de contraseña";
            mensaje = "Está intentando recuperar su contraseña, se le generó la contraseña" + contraGenerada;
        } else {
            subject = "Invitacion de registro";
            mensaje = "No está registrado";
        }
        sendMailService.sendMail(correoDestino, "saritaatanacioarenas@gmail.com", subject, mensaje);
    }
}
