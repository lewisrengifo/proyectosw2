package com.example.demo.Controllers;
import com.example.demo.Entity.Inventarioproducto;
import com.example.demo.Entity.Usuario;
import com.example.demo.Repository.UsuarioRepository;
import com.example.demo.service.SendMailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import javax.xml.bind.Element;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
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
    public String olvidoContrasenia(RedirectAttributes attr){
        return "login/olvidoContrasenia";
    }

    //envia el correo con el token
    @PostMapping("/recuperarContrasenia")
    public String recuperarContrasenia(@RequestParam("correo") String correoDestino, RedirectAttributes attr,
                                       @ModelAttribute("usuario") Usuario usuario) throws MalformedURLException {
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
                String direccion ="http://localhost:8080/UnaChiqui/cambiar1/";
                //String direccion = "http://ec2-100-25-22-199.compute-1.amazonaws.com:8080/UnaChiqui/cambiar1/";
                URL url = new URL(direccion+ token);
                mensaje = "¡Hola!<br><br>Para reestablecer su contraseña haga click: <a href='"+ direccion +token + "'>AQUÍ</a> <br><br>Atte. Área Una Chiqui</b>";;
                attr.addFlashAttribute("msg", "¡Contraseña temporal enviada al correo! :D");
                optionalUsuario.get().setToken(token);
            }else {
                subject = "Invitacion de registro - Mosqoy";
                mensaje = "No está registrado en Mosqoy :(";
                attr.addFlashAttribute("msg2", "¡Correo o contraseña errada! :(");
            }
            sendMailService.sendMail(correoDestino, "saritaatanacioarenas@gmail.com", subject, mensaje);
            return "redirect:/loginForm";
        } else {
            attr.addFlashAttribute("msg2", "¡Ingresa un formato email! :(");
            return "redirect:/loginForm";
        }
    }
    //aquí se ingresa la contraseña
    @GetMapping(value = "/cambiar1/{token}") //formato que espero el usuario coloque en URL
    public String cambiar1(@PathVariable("token") String tokenObtenido, Model model,  RedirectAttributes attr) {
        Usuario usuario = new Usuario();
        usuario.setToken(tokenObtenido);
        model.addAttribute("usuario" , usuario);
        return "login/cambiar1";
    }

    @PostMapping("/cambiarContrasenia")
    public String cambiarContrasenia(Usuario usuario, RedirectAttributes attr) {

        Optional<Usuario> optionalUsuario = Optional.ofNullable(usuarioRepository.findByToken(usuario.getToken()));
        if (optionalUsuario.isPresent()) {
            BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
            if (usuario.getContrasena()==""){
                attr.addFlashAttribute("msg2", "¡Contraseña no puede ser nula! :C");
            }else {
                String pww = bCryptPasswordEncoder.encode(usuario.getContrasena());
                optionalUsuario.get().setContrasena(pww);
                attr.addFlashAttribute("msg", "¡Contraseña cambiada! :D");

                SecureRandom random = new SecureRandom();
                byte bytes[] = new byte[20];
                random.nextBytes(bytes);
                String tokenNuevo = bytes.toString();
                optionalUsuario.get().setToken(tokenNuevo);
            }
            return "redirect:/loginForm";
        } else {
            attr.addFlashAttribute("msg2", "¡Error en el token o expirado! debes generar otro :(");
            return "/login/olvidoContrasenia";
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
        ArrayList<Inventarioproducto> listProductoPedido = new ArrayList<>();
        session.setAttribute("listaProductosEnPedido",listProductoPedido);
        if(rol.equals("Administrador")){
            return "redirect:/usuario/lista";
        }else {
            if (rol.equals("Gestor sede")){
                return "redirect:/inventarioSede/listarInvMiSede";
            }else{
            return "redirect:/inventarioPrincipal";
            }
        }


    }
}
