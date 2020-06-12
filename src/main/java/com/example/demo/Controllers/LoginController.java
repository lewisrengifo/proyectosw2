package com.example.demo.Controllers;


import com.example.demo.Entity.Usuario;
import com.example.demo.Repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;

@Controller
public class LoginController {

    @GetMapping("/loginForm")
    public String loginForm(){
        return "login/login";
    }
    @Autowired
    UsuarioRepository usuarioRepository;

    @GetMapping("/olvidoContrenia")
    public String olvidoContrenia(){
        return "login/olvidoContrenia";
    }

    @PostMapping("/recuperarContrasenia")
    public String recuperarContrasenia(@RequestParam("correo") int correo ){

        return "login/loginForm";
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
