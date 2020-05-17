package com.example.demo.Controllers;

import com.example.crudbase.entity.Usuario;
import com.example.crudbase.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpSession;

@Controller
public class LoginController {

    @GetMapping("/loginForm")
    public String loginForm(){
        return "login/login";
    }
    @Autowired
    UsuarioRepository usuarioRepository;
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
            return "redirect:/categoria";
        }else {
            return "redirect:/comunidad";
        }
    }




}
