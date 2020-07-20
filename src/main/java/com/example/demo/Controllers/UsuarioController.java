package com.example.demo.Controllers;

import com.example.demo.Dto.UsuarioServiceApi;
import com.example.demo.Entity.Notificaciones;
import com.example.demo.Entity.Rol;
import com.example.demo.Entity.Sede;
import com.example.demo.Entity.Usuario;
import com.example.demo.Repository.NotificacionesRepository;
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
import java.time.ZonedDateTime;
import java.util.Date;
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
    @Autowired
    NotificacionesRepository notificacionesRepository;

    @GetMapping(value = {"", "/lista"})
    public String listarUsuarios(@RequestParam Map<String, Object> params, Model model, @ModelAttribute("searchField") String searchField, RedirectAttributes attr) {


        try {
            int page = params.get("page") != null ? (Integer.valueOf(params.get("page").toString()) - 1) : 0;
        } catch (NumberFormatException e) {
            return "redirect:/usuario/lista";
        }
        int page = params.get("page") != null ? (Integer.valueOf(params.get("page").toString()) - 1) : 0;

        if (page < 0) {
            return "redirect:/usuario/lista";
        }

        Page<Usuario> pageUsuario = usuarioService.getAll(page);
        int totalPage = pageUsuario.getTotalPages();
        long totalItems = pageUsuario.getTotalElements();
        if (totalPage > 0) {
            List<Integer> pages = IntStream.rangeClosed(1, totalPage).boxed().collect(Collectors.toList());
            if (page > pages.size() - 1) {
                attr.addFlashAttribute("msgPagina", "No se encuentran datos en esa página");

                return "redirect:/usuario/lista";
            }

            model.addAttribute("page", pages);
        } else {
            attr.addFlashAttribute("msgPagina", "No se encuentran datos en esa página");

            return "redirect:/usuario/lista";
        }
        model.addAttribute("listaUsuarios", pageUsuario.getContent());
        model.addAttribute("totalItems", totalItems);

        model.addAttribute("current", page + 1);
        model.addAttribute("next", page + 2);
        model.addAttribute("prev", page);
        model.addAttribute("last", totalPage);
        String listaactivos = "falsooo";
        model.addAttribute("listaactivos", listaactivos);

        //model.addAttribute("listaUsuarios", usuarioRepository.findAll());
        return "Usuario/lista";
    }

    @GetMapping(value = {"/listaractivos"})
    public String listarUsuariosActivos(@RequestParam Map<String, Object> params, Model model, @ModelAttribute("searchField") String searchField) {
        try {
            int page = params.get("page") != null ? (Integer.valueOf(params.get("page").toString()) - 1) : 0;
        } catch (NumberFormatException e) {
            return "redirect:/usuario/lista";
        }
        int page = params.get("page") != null ? (Integer.valueOf(params.get("page").toString()) - 1) : 0;

        if (page < 0) {
            return "redirect:/usuario/lista";
        }

        Page<Usuario> pageUsuario = usuarioService.getAllActivos(page);
        int totalPage = pageUsuario.getTotalPages();
        long totalItems = pageUsuario.getTotalElements();
        if (totalPage > 0) {
            List<Integer> pages = IntStream.rangeClosed(1, totalPage).boxed().collect(Collectors.toList());
            model.addAttribute("page", pages);
        }
        model.addAttribute("listaUsuarios", pageUsuario.getContent());
        model.addAttribute("totalItems", totalItems);

        model.addAttribute("current", page + 1);
        model.addAttribute("next", page + 2);
        model.addAttribute("prev", page);
        model.addAttribute("last", totalPage);
        String listaactivos = "verdadero";
        model.addAttribute("listaactivos", listaactivos);

        //model.addAttribute("listaUsuarios", usuarioRepository.findAll());
        return "Usuario/lista";
    }

    @GetMapping(value = {"/listardesactivados"})
    public String listarUsuariosDesactivados(@RequestParam Map<String, Object> params, Model model, @ModelAttribute("searchField") String searchField) {
        try {
            int page = params.get("page") != null ? (Integer.valueOf(params.get("page").toString()) - 1) : 0;
        } catch (NumberFormatException e) {
            return "redirect:/usuario/lista";
        }
        int page = params.get("page") != null ? (Integer.valueOf(params.get("page").toString()) - 1) : 0;

        if (page < 0) {
            return "redirect:/usuario/lista";
        }

        Page<Usuario> pageUsuario = usuarioService.getAllDesactivados(page);
        int totalPage = pageUsuario.getTotalPages();
        long totalItems = pageUsuario.getTotalElements();
        if (totalPage > 0) {
            List<Integer> pages = IntStream.rangeClosed(1, totalPage).boxed().collect(Collectors.toList());
            model.addAttribute("page", pages);
        }
        model.addAttribute("listaUsuarios", pageUsuario.getContent());
        model.addAttribute("totalItems", totalItems);

        model.addAttribute("current", page + 1);
        model.addAttribute("next", page + 2);
        model.addAttribute("prev", page);
        model.addAttribute("last", totalPage);
        String listaactivos = "desactivados";
        model.addAttribute("listaactivos", listaactivos);

        //model.addAttribute("listaUsuarios", usuarioRepository.findAll());
        return "Usuario/lista";
    }

    @GetMapping("/nuevo")
    public String nuevoUsuario(Model model, @ModelAttribute Usuario usuario) {
        model.addAttribute("listaroles", rolRepository.rolgestorprincipal());
        model.addAttribute("listasedes", sedeRepository.findAll());
        return "Usuario/form";
    }

    @PostMapping("/guardar")
    public String guardar(@ModelAttribute("usuario") @Valid Usuario usuario, BindingResult bindingResult,
                          RedirectAttributes redirectAttributes, Model model, @RequestParam(name = "rol_idrol") int rol_idrol) throws MalformedURLException {
        if (bindingResult.hasErrors()) {
            model.addAttribute("listaroles", rolRepository.rolgestorprincipal());
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


        if (usuario.getIdusuario() == 0) {

            //aca se envia la contraseña generada..
            SecureRandom random = new SecureRandom();
            byte bytes[] = new byte[20];
            random.nextBytes(bytes);
            String token = bytes.toString();
            //String direccion = "http://localhost:8080/UnaChiqui/cambiar1/";
            String direccion = "http://ec2-54-162-44-212.compute-1.amazonaws.com:8080/UnaChiqui/cambiar1/";
            URL url = new URL(direccion + token);
            String mensaje = "¡Hola! Usted ha sido registrado usuario en el portal de gestion de Mosqoy<br><br>Para cambiar su contraseña haga click: <a href='" + direccion + token + "'>AQUÍ</a> <br><br>Atte. Área Una Chiqui.pe</b>";
            ;

            sendMailService.sendMail(usuario.getCorreo(), "saritaatanacioarenas@gmail.com", "Envio de contraseña", mensaje);
            usuario.setContrasena(encriptar(usuario.getContrasena()));
            usuario.setToken(token);
        } else {

            Optional<Usuario> optionalUsuario = usuarioRepository.findById(usuario.getIdusuario());
            usuario.setContrasena(optionalUsuario.get().getContrasena());
        }
        //Optional<Usuario> optionalUsuario = usuarioRepository.findById(usuarioRepository.ultimoidinsertado());
        //if (rol_idrol == 1||rol_idrol==2) {

        // usuario.setSede_idsede(null);
        //usuarioRepository.save(usuario);
        //} else {
        //Rol rol = new Rol();
        //rol.setIdrol(2);
        //usuario.setRol_idrol(rol);
        if (rol_idrol == 2) {
            Sede sede = new Sede();
            sede.setIdsede(3);
            usuario.setSede_idsede(sede);
            ZonedDateTime now = ZonedDateTime.now();
            Date nowdate = Date.from(now.toInstant());
            Notificaciones notificaciones = new Notificaciones();
            notificaciones.setFecha(nowdate);
            notificaciones.setFlag(false);
            //Usuario usuario1 = new Usuario();
            notificaciones.setUsuario(usuarioRepository.save(usuario));
            notificacionesRepository.save(notificaciones);
        } else {
            usuario.setSede_idsede(null);
        }

        usuarioRepository.save(usuario);
        // }

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

    @GetMapping("/buscador")
    public String buscadorSearch(@RequestParam Map<String, Object> params, Model model, RedirectAttributes att, @ModelAttribute("searchField") String textbuscador, RedirectAttributes attr) {

        if (textbuscador.isEmpty()) {
            att.addFlashAttribute("msgBuscador", "Campo vacio. Ingrese el dato a buscar");

            return "redirect:/usuario/lista";
        } else {

            try {
                int page = params.get("page") != null ? (Integer.valueOf(params.get("page").toString()) - 1) : 0;
            } catch (NumberFormatException e) {
                return "redirect:/usuario/lista";
            }
            int page = params.get("page") != null ? (Integer.valueOf(params.get("page").toString()) - 1) : 0;

            if (page < 0) {
                return "redirect:/usuario/lista";
            }


            Page<Usuario> pageUsuario1 = usuarioService.buscador(textbuscador, page);
            int totalPage = pageUsuario1.getTotalPages();
            long totalItems = pageUsuario1.getTotalElements();
            if (totalPage > 0) {
                List<Integer> pages = IntStream.rangeClosed(1, totalPage).boxed().collect(Collectors.toList());
                if (page > pages.size() - 1) {
                    attr.addFlashAttribute("msgPagina", "No se encuentran datos en esa página");

                    return "redirect:/usuario/lista";
                }


                model.addAttribute("page", pages);
            } else {
                attr.addFlashAttribute("msgPagina", "No se encuentran datos en esa página");

                return "redirect:/usuario/lista";
            }
            model.addAttribute("listaUsuarios", pageUsuario1.getContent());
            model.addAttribute("totalItems", totalItems);

            model.addAttribute("current", page + 1);
            model.addAttribute("next", page + 2);
            model.addAttribute("prev", page);
            model.addAttribute("last", totalPage);
            model.addAttribute("searchField", textbuscador);
            String listaactivos = "falsooo";
            model.addAttribute("listaactivos", listaactivos);

            //model.addAttribute("listaUsuarios", usuarioRepository.findAll());
            return "Usuario/lista";
        }

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
