package com.example.demo.Controllers;

import com.example.demo.Dto.UsuarioSedeDto;
import com.example.demo.Entity.Sede;
import com.example.demo.Entity.Usuario;
import com.example.demo.Repository.RolRepository;
import com.example.demo.Repository.SedeRepository;
import com.example.demo.Repository.UsuarioRepository;
import com.example.demo.service.SedeService;
import com.example.demo.service.SendMailService;
import com.example.demo.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.SecureRandom;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/sede")
public class SedeController {
    @Autowired
    SedeRepository sedeRepository;
    @Autowired
    SedeService sedeService;
    @Autowired
    UsuarioRepository usuarioRepository;
    @Autowired
    UsuarioService usuarioService;
    @Autowired
    RolRepository rolRepository;
    @Autowired
    SendMailService sendMailService;

    @GetMapping(value = {"", "/lista"})
    public String listarSedes(@RequestParam Map<String, Object> params, Model model) {
        try {
            int page = params.get("page") != null ? (Integer.valueOf(params.get("page").toString()) - 1) : 0;
        } catch (NumberFormatException e) {
            return "redirect:/sede/lista";
        }
        int page = params.get("page") != null ? (Integer.valueOf(params.get("page").toString()) - 1) : 0;

        if (page < 0) {
            return "redirect:/sede/lista";
        }

        Page<Usuario> pageSede = usuarioService.getAllGestSede(page);
        int totalPage = pageSede.getTotalPages();
        long totalItems = pageSede.getTotalElements();
        if (totalPage > 0) {
            List<Integer> pages = IntStream.rangeClosed(1, totalPage).boxed().collect(Collectors.toList());
            model.addAttribute("page", pages);
        }
        model.addAttribute("listSedes", pageSede.getContent());
        // model.addAttribute("usuariosdelasede", pageSede.getContent());
        model.addAttribute("totalItems", totalItems);
        model.addAttribute("listausuariosdisponibles", usuarioRepository.usuariosDisponibles());
        model.addAttribute("current", page + 1);
        model.addAttribute("next", page + 2);
        model.addAttribute("prev", page);
        model.addAttribute("last", totalPage);

        //model.addAttribute("listaUsuarios", usuarioRepository.findAll());
        return "sede/lista";

    }

    @GetMapping("/nuevo")
    public String nuevaSede(Model model, @ModelAttribute Sede sede) {

        return "sede/form";
    }

    @PostMapping("/guardar")
    public String guardarSede(@ModelAttribute("sede") @Valid Sede sede, BindingResult bindingResult, RedirectAttributes redirectAttributes, @ModelAttribute("usuariodelasede") Usuario usuariodelasede, Model model,
                              @ModelAttribute("idsede") int idsede, @ModelAttribute("usuario") Usuario usuario) {
        if (bindingResult.hasErrors()) {
            return "sede/form";

        } else {
            if (sede.getIdsede() == 0) {
                Sede sedeExiste = sedeRepository.sedePornombre(sede.getNombre());
                if (sedeExiste == null){
                    redirectAttributes.addFlashAttribute("mgs", "La sede se creo correctamente");
                    sedeRepository.save(sede);
                    model.addAttribute("listausuariosdisponibles", usuarioRepository.usuariosDisponibles());
                    model.addAttribute("idsede", sedeRepository.save(sede).getIdsede());
                    return "sede/formGestorNew";
                }else{
                    model.addAttribute("msg","La sede " + sede.getNombre() + " ya existe");
                    return "sede/form";
                }

            } else {
                redirectAttributes.addFlashAttribute("msg", "La sede se actualizó correctamente");
                //model.addAttribute(usuariodelasede);
                idsede = sede.getIdsede();
                List<UsuarioSedeDto> usuariodelasededb = usuarioRepository.usuariodelasedeint(sede.getIdsede());
                int id = 0;
                for (UsuarioSedeDto info : usuariodelasededb) {
                    usuarioRepository.actualizarGestorSede(usuariodelasededb.get(id).getUsuariodelasede());
                    id++;
                }

                model.addAttribute("idsede", sedeRepository.save(sede).getIdsede());
                model.addAttribute("listausuariosdisponibles", usuarioRepository.usuariosDisponibles());
                // model.addAttribute("usuario");
                //usuarioRepository.actualizarSededelGestor(usuariodelasededb.getUsuariodelasede());
                sedeRepository.save(sede);

                return "sede/formGestorNew";
            }

        }
    }

    @GetMapping("/editar")
    public String editarSede(@ModelAttribute("sede") Sede sede, Model model, @RequestParam("id") int id, @ModelAttribute("usuariodelasede") Usuario usuariodelasede) {
        Optional<Sede> optionalSede = sedeRepository.findById(id);
        //Usuario usuariodelasede = usuarioRepository.usuariodelasede(id);
        if (optionalSede.isPresent()) {
            sede = optionalSede.get();
            model.addAttribute(sede);
            //usuariodelasede=usuarioRepository.usuariodelasede(id);
            //model.addAttribute(usuariodelasede);
            return "sede/form";
        } else {
            return "redirect:/sede/lista";
        }

    }

    @GetMapping("/agregargestor")
    public String agregarGestor(Model model, @ModelAttribute("usuario") Usuario usuario, @ModelAttribute("sede") Sede sede) {
        //model.addAttribute("listaroles", rolRepository.rolgestorsede());
        model.addAttribute("listausuariosdisponibles", usuarioRepository.usuariosDisponibles());
        //int idsederec = idsede;
        model.addAttribute("sede", sede);
        return "sede/formGestorNew";
    }

    @GetMapping("/agregargestorAdicional")
    public String agregarGestorAdicional(Model model, @ModelAttribute("usuario") Usuario usuario, @RequestParam("id") int idsede) {
        //model.addAttribute("listaroles", rolRepository.rolgestorsede());
        model.addAttribute("listausuariosdisponibles", usuarioRepository.usuariosDisponibles());
        //int idsederec = idsede;
        model.addAttribute("idsede", idsede);
        return "sede/formGestorNew";
    }

    @PostMapping("/guardarGestor")
    public String guardarGestor(@ModelAttribute("usuario") @Valid Usuario usuario, BindingResult bindingResult,
                                RedirectAttributes redirectAttributes, Model model, @RequestParam(name = "rol_idrol") int rol_idrol) throws MalformedURLException {
        if (bindingResult.hasErrors()) {
            model.addAttribute("listaroles", rolRepository.rolgestorsede());
            // model.addAttribute("listasedes", sedeRepository.findAll());
            return "sede/formGestor";
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
                        return "redirect:/sede/lista";

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
            String direccion = "http://localhost:8080/UnaChiqui/cambiar1/";
            //String direccion = "http://ec2-54-237-112-13.compute-1.amazonaws.com:8080/UnaChiqui/cambiar1/";
            URL url = new URL(direccion + token);
            String mensaje = "¡Hola!<br><br>Para cambiar su contraseña haga click: <a href='" + direccion + token + "'>AQUÍ</a> <br><br>Atte. Área Una Chiqui</b>";
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
        usuario.setSede_idsede(sedeRepository.findTopByOrderByIdsedeDesc());
        usuarioRepository.save(usuario);
        // }
        return "redirect:/sede/lista";
    }

    @GetMapping("/editarGest")
    public String editarGestorsede(@ModelAttribute("usuario") Usuario usuario, Model model, @ModelAttribute("usuariodelasede") Usuario usuariosede, RedirectAttributes redirectAttributes) {

        Optional<Usuario> optionalUsuario = usuarioRepository.findById(usuariosede.getIdusuario());
        if (optionalUsuario.isPresent()) {
            model.addAttribute("listaroles", rolRepository.rolgestorsede());
            // model.addAttribute("listasedes", sedeRepository.findAll());
            usuario = optionalUsuario.get();
            model.addAttribute("usuario", usuario);
            return "sede/formGestor";
        } else {
            return "redirect:/sede/lista";
        }
    }

    @GetMapping("/buscador")
    public String buscador(@RequestParam Map<String, Object> params, Model model, RedirectAttributes att, @ModelAttribute("searchField") String textbuscador) {
        if (textbuscador.isEmpty()) {
            att.addFlashAttribute("msgBuscador", "Campo vacio. Ingrese el dato a buscar");

            return "redirect:/sede/lista";
        } else {

            try {
                int page = params.get("page") != null ? (Integer.valueOf(params.get("page").toString()) - 1) : 0;
            } catch (NumberFormatException e) {
                return "redirect:/sede/lista";
            }
            int page = params.get("page") != null ? (Integer.valueOf(params.get("page").toString()) - 1) : 0;

            if (page < 0) {
                return "redirect:/sede/lista";
            }

            Page<Sede> pageUsuario1 = sedeService.buscador(textbuscador, page);
            int totalPage = pageUsuario1.getTotalPages();
            long totalItems = pageUsuario1.getTotalElements();
            if (totalPage > 0) {
                List<Integer> pages = IntStream.rangeClosed(1, totalPage).boxed().collect(Collectors.toList());
                model.addAttribute("page", pages);
            }
            model.addAttribute("listSedes", pageUsuario1.getContent());
            model.addAttribute("totalItems", totalItems);

            model.addAttribute("current", page + 1);
            model.addAttribute("next", page + 2);
            model.addAttribute("prev", page);
            model.addAttribute("last", totalPage);
            model.addAttribute("searchField", textbuscador);
            //model.addAttribute("listaUsuarios", usuarioRepository.findAll());
            return "sede/lista";
        }

    }

    public String encriptar(String pww) {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        pww = bCryptPasswordEncoder.encode(pww);
        return pww;
    }

    @GetMapping("/guardarGestorNew")
    public String guardarGestorNew(@RequestParam("usuario") int idsuario, @RequestParam("idsede") int idsede) {
        int idsederec = idsede;

        int iduser = idsuario;
        usuarioRepository.actualizarRolSede(idsederec, 3, iduser);


        //usuarioRepository.actualizarRolSede(idsuario, 3, idsede);


        return "redirect:/sede/lista";
    }

    @GetMapping("/editarGestorNew")
    public String editarGestorNew(@RequestParam("usuario") int idsuario) {
        return "";
    }

    @ModelAttribute
    public void provideIdsede(Model model) {
        model.addAttribute("idsede", new Integer(1));
    }
}
