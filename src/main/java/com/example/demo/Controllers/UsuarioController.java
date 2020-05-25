package com.example.demo.Controllers;

import com.example.demo.Entity.Usuario;
import com.example.demo.Repository.RolRepository;
import com.example.demo.Repository.SedeRepository;
import com.example.demo.Repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.naming.Binding;
import javax.validation.Valid;
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
    public String listarUsuarios(Model model) {
        model.addAttribute("listaUsuarios", usuarioRepository.findAll());
        return "Usuario/lista";
    }

    @GetMapping("/nuevo")
    public String nuevoUsuario(Model model, @ModelAttribute Usuario usuario) {
        model.addAttribute("listaroles", rolRepository.findAll());
        model.addAttribute("listasedes", sedeRepository.findAll());
        return "Usuario/form";
    }

    @PostMapping("/guardar")
    public String guardar(@ModelAttribute("usuario") @Valid Usuario usuario, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("listaroles", rolRepository.findAll());
            model.addAttribute("listasedes", sedeRepository.findAll());
            return "Usuario/form";
        }

        for (Usuario usuario1 : usuarioRepository.findAll()) {
            if (usuario.getIdusuario() == 0) {
                if (usuario1.getDni().equals(usuario.getDni()) || usuario1.getCorreo().equals(usuario.getCorreo())) {
                    if (usuario1.getDni().equals(usuario.getDni())) {
                        redirectAttributes.addFlashAttribute("msg", "Usuario con DNI existente");
                        redirectAttributes.addFlashAttribute("usuario", usuario);
                    }
                    if (usuario1.getCorreo().equals(usuario.getCorreo())) {
                        redirectAttributes.addFlashAttribute("msg2", "Usuario con correo existente");
                        redirectAttributes.addFlashAttribute("usuario", usuario);
                    }
                    return "redirect:/usuario/nuevo";

                } else if (usuario.getIdusuario() == 0) {
                    redirectAttributes.addFlashAttribute("msg", "Usuario creado exitosamente");
                } else {
                    redirectAttributes.addFlashAttribute("msg", "Usuario actualizado exitosamente");
                }
            }
        }
        /*
        if (usuario.getIdusuario()==0) {
            if ((!usuario.isEnable() == true || !usuario.isEnable() == false)) {
                redirectAttributes.addFlashAttribute("msg", "No son valores booleanos ctmr que chucha inspeccionas");
                return "Usuario/form";
            }
        }
        if (usuario.getIdusuario()==0) {
            if ((!usuario.isEnable() == true || !usuario.isEnable() == false)) {
                redirectAttributes.addFlashAttribute("msg", "No son valores booleanos ctmr que chucha inspeccionas");
                return "Usuario/form";
            }
        }
        */

        usuario.setContrasena(encriptar(usuario.getContrasena()));
        usuarioRepository.save(usuario);
        return "redirect:/usuario/lista";
    }

    @GetMapping("/editar")
    public String editarUsuario(@ModelAttribute("usuario") Usuario usuario, Model model, @RequestParam("id") int id, RedirectAttributes redirectAttributes) {

        Optional<Usuario> optionalUsuario = usuarioRepository.findById(id);
        if (optionalUsuario.isPresent()) {
            model.addAttribute("listaroles", rolRepository.findAll());
            model.addAttribute("listasedes", sedeRepository.findAll());
            usuario = optionalUsuario.get();
            model.addAttribute("usuario", usuario);
            return "Usuario/form";
        } else {
            return "redirect: /usuario/lista";
        }
    }

    public String encriptar(String pww) {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        pww = bCryptPasswordEncoder.encode(pww);
        return pww;
    }


}
