package com.example.demo.Controllers;
import com.example.demo.Entity.Usuario;
import com.example.demo.Repository.UsuarioRepository;
import com.example.demo.service.SendMailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.security.SecureRandom;
import java.util.Optional;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Controller
public class LoginController {

    @Autowired
    SendMailService sendMailService;

    @Autowired
    UsuarioRepository usuarioRepository;

    @GetMapping(value = {"","/loginForm"})
    public String loginForm(RedirectAttributes attr){
        return "login/login";
    }


    @GetMapping("/olvidoContrasenia")
    public String olvidoContrasenia(){
        return "login/olvidoContrasenia";
    }

    //envia el correo con el token
    @PostMapping("/recuperarContrasenia")
    public String recuperarContrasenia(@RequestParam("correo") String correoDestino, RedirectAttributes attr,
                                       @ModelAttribute("usuario") Usuario usuario){
        String emailPattern = "^[_a-z0-9-]+(\\.[_a-z0-9-]+)*@" +
                "[a-z0-9-]+(\\.[a-z0-9-]+)*(\\.[a-z]{2,4})$";
        Pattern pattern = Pattern.compile(emailPattern);
        Matcher matcher = pattern.matcher(correoDestino);

        if (matcher.find() == true) {
            Optional<Usuario> optionalUsuario = Optional.ofNullable(usuarioRepository.findByCorreo(correoDestino));
            String subject;
            String mensaje;
            if (optionalUsuario.isPresent()) {
                SecureRandom random = new SecureRandom();
                byte bytes[] = new byte[20];
                random.nextBytes(bytes);
                String token = bytes.toString();

                subject = "Recuperacion de contraseña - Mosqoy";
                mensaje = "Está intentando recuperar su contraseña, se le generó el token temporal: " + token;
                attr.addFlashAttribute("msg", "¡Contraseña temporal enviada al correo! :D");
                optionalUsuario.get().setToken(token);
            }else {
                subject = "Invitacion de registro - Mosqoy";
                mensaje = "No está registrado en Mosqoy :(";
                attr.addFlashAttribute("msg", "¡Correo o contraseña errada! :(");
            }
            sendMailService.sendMail(correoDestino, "saritaatanacioarenas@gmail.com", subject, mensaje);
            return "login/resetearContrasenia";
        } else {
            attr.addFlashAttribute("msg", "¡Ingresa un formato email! :(");
            return "redirect:/loginForm";
        }
    }

    @PostMapping("/cambiarContrasenia")
    public String cambiarContrasenia(RedirectAttributes attr,
                                    @RequestParam("token") String token,
                                    @RequestParam("contrasena") String contrasenia) {
        String subject;
        String mensaje;
        Optional<Usuario> optionalUsuario = Optional.ofNullable(usuarioRepository.findByToken(token));
        if (optionalUsuario.isPresent()) {
            BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
            String pww = bCryptPasswordEncoder.encode(contrasenia);
            optionalUsuario.get().setContrasena(pww);
            attr.addFlashAttribute("msg", "¡Contraseña cambiada! :D");

            SecureRandom random = new SecureRandom();
            byte bytes[] = new byte[20];
            random.nextBytes(bytes);
            String tokenNuevo = bytes.toString();
            optionalUsuario.get().setToken(tokenNuevo);
            return "redirect:/loginForm";
        } else {
            attr.addFlashAttribute("msg", "¡Error en el token o expirado! debes generar otro :(");
            return "login/resetearContrasenia";
        }
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
            if (rol.equals("Gestor sede")){
                return "redirect:/artesano";
            }else{
            return "redirect:/categoria";
            }
        }


    }
}
