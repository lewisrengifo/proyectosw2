package com.example.demo.Controllers;


import com.example.demo.Entity.Comunidad;
import com.example.demo.Entity.Usuario;
import com.example.demo.Repository.UsuarioRepository;
import com.example.demo.service.SendMailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.util.Optional;
import java.util.Random;

@Controller
public class LoginController {

    @Autowired
    SendMailService sendMailService;

    @Autowired
    UsuarioRepository usuarioRepository;

    @GetMapping("/loginForm")
    public String loginForm(RedirectAttributes attr){
        return "login/login";
    }


    @GetMapping("/olvidoContrasenia")
    public String olvidoContrasenia(){
        return "login/olvidoContrasenia";
    }

    @PostMapping("/recuperarContrasenia")
    public String recuperarContrasenia(@RequestParam("correo") String correoDestino, RedirectAttributes attr){

        Usuario optional = usuarioRepository.findByCorreo(correoDestino);
        String subject;
        String mensaje;

        if (optional.getCorreo() != "") {
            String caracteres1 = "abcdefghijklmnopqrtsuvwxyz1234567890";
            String caracteres2 = "1234567890";
            Random random = new Random();
            StringBuilder aux = new StringBuilder();
            StringBuilder aux2 = new StringBuilder();
            for (int i = 0; i < 7; i++) {
                aux.append(caracteres1.charAt(random.nextInt(caracteres1.length())));
            }
            for (int i = 0; i < 3; i++) {
                aux2.append(caracteres2.charAt(random.nextInt(caracteres2.length())));
            }
            String contraGenerada = aux.toString() + aux2.toString();
            subject = "Recuperacion de contraseña - Mosqoy";
            mensaje = "Está intentando recuperar su contraseña, se le generó la contraseña: " + contraGenerada;

        }else {
            subject = "Invitacion de registro - Mosqoy";
            mensaje = "No está registrado en Mosqoy :(";

        }

        attr.addFlashAttribute("msg",  (optional.getCorreo()!= null ? "¡Contraseña temporal enviada!" : "Correo o usuario errado :("));
        sendMailService.sendMail(correoDestino, "saritaatanacioarenas@gmail.com", subject, mensaje);
        //}

        return "redirect:/loginForm";
    }


    @GetMapping("/redirectByRol")
    public String redirectByRol(Authentication authentication, HttpSession session){
        String rol = "";
        for(GrantedAuthority role : authentication.getAuthorities()){
            rol = role.getAuthority();
            break;
        }
        String username = authentication.getName();
        Usuario usuario = usuarioRepository.findByCorreo(username);
        session.setAttribute("usuario",usuario);

        if(rol.equals("Administrador")){
            return "redirect:/usuario/lista";
        }else {
            if (rol.equals("sede")){
                return "redirect:/artesano";
            }else{
            return "redirect:/categoria";
            }
        }


    }




}
