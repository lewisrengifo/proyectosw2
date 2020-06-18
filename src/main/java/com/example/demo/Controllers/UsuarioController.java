package com.example.demo.Controllers;

import com.example.demo.Dto.UsuarioServiceApi;
import com.example.demo.Entity.Rol;
import com.example.demo.Entity.Usuario;
import com.example.demo.Repository.RolRepository;
import com.example.demo.Repository.SedeRepository;
import com.example.demo.Repository.UsuarioRepository;
import com.example.demo.service.SendMailService;
import com.example.demo.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.naming.Binding;
import javax.validation.Valid;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.SecureRandom;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/usuario")
public class UsuarioController {
    @Autowired
    UsuarioService usuarioService;
    @Autowired
    UsuarioRepository usuarioRepository;
    @Autowired
    RolRepository rolRepository;
    @Autowired
    SedeRepository sedeRepository;
    @Autowired
    SendMailService sendMailService;

    @GetMapping(value = {"", "/lista"})
    public String listarUsuarios(@RequestParam Map<String, Object> params,Model model) {
        try {
            int page = params.get("page") != null ? (Integer.valueOf(params.get("page").toString()) - 1) : 0;
        }catch (NumberFormatException e){
            return "redirect:/usuario/lista";
        }
        int page = params.get("page") != null ? (Integer.valueOf(params.get("page").toString()) - 1) : 0;

        if(page<0){
            return "redirect:/usuario/lista";
        }

        Page<Usuario> pageUsuario = usuarioService.getAll(page);
        int totalPage = pageUsuario.getTotalPages();
        long totalItems = pageUsuario.getTotalElements();
        if(totalPage>0){
            List<Integer> pages = IntStream.rangeClosed(1, totalPage).boxed().collect(Collectors.toList());
            model.addAttribute("pages", pages);
        }
        model.addAttribute("listaUsuarios", pageUsuario.getContent());
        model.addAttribute("totalItems", totalItems);

        model.addAttribute("current", page + 1);
        model.addAttribute("next", page + 2);
        model.addAttribute("prev", page);
        model.addAttribute("last", totalPage);
        //model.addAttribute("listaUsuarios", usuarioRepository.findAll());
        return "Usuario/lista";
    }

    @GetMapping("/nuevo")
    public String nuevoUsuario(Model model, @ModelAttribute Usuario usuario) {
        model.addAttribute("listaroles", rolRepository.findAll());
        model.addAttribute("listasedes", sedeRepository.findAll());
        return "Usuario/form";
    }

    @PostMapping("/guardar")
    public String guardar(@ModelAttribute("usuario") @Valid Usuario usuario, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model, @RequestParam(name = "rol_idrol")int rol_idrol) throws MalformedURLException {
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
            } else {
                for (Usuario usuario2 : usuarioRepository.buscarmenosmio(usuario.getIdusuario())) {
                    if (usuario2.getDni().equals(usuario.getDni()) || usuario2.getCorreo().equals(usuario.getCorreo())) {
                        if (usuario1.getDni().equals(usuario.getDni())) {
                            redirectAttributes.addFlashAttribute("msgdni", "El DNI ingresado le pertenece a otra persona");
                            redirectAttributes.addFlashAttribute("usuario", usuario);
                        }
                        if (usuario1.getCorreo().equals(usuario.getCorreo())) {
                            redirectAttributes.addFlashAttribute("msgcorreo", "El correo ya se encuentra en uso");
                            redirectAttributes.addFlashAttribute("usuario", usuario);
                        }
                        return "redirect:/usuario/lista";

                    } else {
                        redirectAttributes.addFlashAttribute("msg", "Usuario actualizado exitosamente");

                    }

                }
            }
        }
        try {
            if (sedeRepository.findByIdrol(usuario.getSede_idrol().getIdrol()) == null) {

            }
        } catch (NullPointerException e) {
            redirectAttributes.addFlashAttribute("msgsede", "El usuario no se creó o actualizó debido a que no seleccionó una sede valida");

            return "redirect:/usuario/lista";
        }
        if(usuario.getIdusuario()==0){

            //aca se envia la contraseña generada..
            SecureRandom random = new SecureRandom();
            byte bytes[] = new byte[20];
            random.nextBytes(bytes);
            String token = bytes.toString();
            String direccion ="http://localhost:8081/UnaChiqui/cambiar1/";
            //String direccion = "http://ec2-54-237-112-13.compute-1.amazonaws.com:8080/UnaChiqui/cambiar1/";
            URL url = new URL(direccion+ token);
            String mensaje = "¡Hola!<br><br>Para cambiar su contraseña haga click: <a href='"+ direccion +token + "'>AQUÍ</a> <br><br>Atte. Área Una Chiqui</b>";;

            sendMailService.sendMail(usuario.getCorreo(), "saritaatanacioarenas@gmail.com", "Envio de contraseña", mensaje);
            usuario.setContrasena(encriptar(usuario.getContrasena()));
            usuario.setToken(token);
        }else{
            Optional<Usuario> optionalUsuario = usuarioRepository.findById(usuario.getIdusuario());
            usuario.setContrasena(optionalUsuario.get().getContrasena());
        }
        //Optional<Usuario> optionalUsuario = usuarioRepository.findById(usuarioRepository.ultimoidinsertado());
        if(rol_idrol==1){

            usuario.setSede_idrol(null);
            usuarioRepository.save(usuario);
        }else {
            usuarioRepository.save(usuario);
        }
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
            return "redirect:/usuario/lista";
        }
    }

    public String encriptar(String pww) {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        pww = bCryptPasswordEncoder.encode(pww);
        return pww;
    }

    @PostMapping("/buscador")
    public String buscadorSearch(@RequestParam("searchField") String buscador, Model model) {

        model.addAttribute("listaUsuarios", usuarioRepository.buscarUsuario(buscador));
        return "Usuario/lista";


    }
    /*
    public String findAll(@RequestParam Map<String, Object> params, Model model){
        int page = params.get("page") != null ? (Integer.valueOf(params.get("page").toString()) -1) :0;
        PageRequest pageRequest = PageRequest.of(page, 10);
        Page<Usuario> pageUsuario = usuarioServiceApi.getAll(pageRequest);
        int totalPage = pageUsuario.getTotalPages();
        if(totalPage>0){
            List<Integer> pages = IntStream.rangeClosed(1, totalPage).boxed().collect(Collectors.toList());
            model.addAttribute("pages", pages);
        }
        model.addAttribute("listaUsuarios", pageUsuario.getContent());

        return "Usuario/lista";
    }*/


}
