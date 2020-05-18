package com.example.demo.Controllers;

import com.example.demo.Entity.Usuario;
import com.example.demo.Repository.RolRepository;
import com.example.demo.Repository.SedeRepository;
import com.example.demo.Repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
@RequestMapping("/usuario")
public class UsuarioController {
    @Autowired
    UsuarioRepository usuarioRepository;
    @Autowired
    RolRepository rolRepository;
    @Autowired
    SedeRepository sedeRepository;
    @GetMapping("/lista")
    public String listarUsuarios(Model model){
        model.addAttribute("listaUsuarios", usuarioRepository.findAll());
        return "Usuario/lista";
    }
    @GetMapping("/nuevo")
    public String nuevoUsuario(Model model){
        model.addAttribute("listaroles", rolRepository.findAll());
        model.addAttribute("listasedes", sedeRepository.findAll());
        return "Usuario/form";
    }
    @PostMapping("/guardar")
    public String guardar(Usuario usuario){
        usuarioRepository.save(usuario);
        return "redirect: /usuario";
    }
    @GetMapping("/editar")
    public String editarUsuario(@ModelAttribute("usuario") Usuario usuario, Model model, @RequestParam("id") int id, RedirectAttributes redirectAttributes){
        Optional<Usuario> optionalUsuario = usuarioRepository.findById(id);
        if(optionalUsuario.isPresent()){
            usuario = optionalUsuario.get();
            model.addAttribute("usuario", usuario);
            return "Usuario/form";
        } else {
            return"redirect: /usuario/lista";
        }
    }



}
