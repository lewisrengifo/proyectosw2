package com.example.demo.Controllers;

import com.example.demo.Entity.Consignacionyventa;
import com.example.demo.Entity.Inventarioproducto;
import com.example.demo.Entity.Notificaciones;
import com.example.demo.Entity.Usuario;
import com.example.demo.Repository.ConsignacionyventaRepository;
import com.example.demo.Repository.NotificacionesRepository;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Controller
public class LoginController {

    @Autowired
    SendMailService sendMailService;

    @Autowired
    UsuarioRepository usuarioRepository;
    @Autowired
    ConsignacionyventaRepository consignacionyventaRepository;
    @Autowired
    NotificacionesRepository notificacionesRepository;

    @GetMapping(value = {"", "/loginForm"})
    public String loginForm(RedirectAttributes attr) {
        return "login/login";
    }


    @GetMapping("/olvidoContrasenia")
    public String olvidoContrasenia(RedirectAttributes attr) {
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
                String direccion = "http://localhost:8080/UnaChiqui/cambiar1/";
                //String direccion = "http://ec2-100-25-22-199.compute-1.amazonaws.com:8080/UnaChiqui/cambiar1/";
                URL url = new URL(direccion + token);
                mensaje = "¡Hola!<br><br>Para reestablecer su contraseña haga click: <a href='" + direccion + token + "'>AQUÍ</a> <br><br>Atte. Área Una Chiqui</b>";
                ;
                attr.addFlashAttribute("msg", "¡Contraseña temporal enviada al correo! :D");
                optionalUsuario.get().setToken(token);
            } else {
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
    public String cambiar1(@PathVariable("token") String tokenObtenido, Model model, RedirectAttributes attr) {
        Usuario usuario = new Usuario();
        usuario.setToken(tokenObtenido);
        model.addAttribute("usuario", usuario);
        return "login/cambiar1";
    }

    @PostMapping("/cambiarContrasenia")
    public String cambiarContrasenia(Usuario usuario, RedirectAttributes attr) {

        Optional<Usuario> optionalUsuario = Optional.ofNullable(usuarioRepository.findByToken(usuario.getToken()));
        if (optionalUsuario.isPresent()) {
            BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
            if (usuario.getContrasena() == "") {
                attr.addFlashAttribute("msg2", "¡Contraseña no puede ser nula! :C");
            } else {
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
    public String redirectByRol(Authentication authentication, HttpSession session) {
        String rol = "";
        for (GrantedAuthority role : authentication.getAuthorities()) {
            rol = role.getAuthority();
            break;
        }
        String username = authentication.getName();
        Usuario usuario = usuarioRepository.findByCorreo(username);
        session.setAttribute("usuario", usuario);
        ArrayList<Inventarioproducto> listProductoPedido = new ArrayList<>();
        session.setAttribute("listaProductosEnPedido", listProductoPedido);
        if (rol.equals("Administrador")) {
            return "redirect:/usuario/lista";
        } else if (rol.equals("Gestor sede")) {
            return "redirect:/inventarioSede/listarInvMiSede";
        } else if (rol.equals("Gestor principal")) {
            ZonedDateTime now = ZonedDateTime.now();
            System.out.println(now);
            Date nowdate = Date.from(now.toInstant());
            // System.out.println(nowdate.getDay());
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String formatnow = new String();

            formatnow = simpleDateFormat.format(nowdate);

            System.out.println(formatnow);
            ArrayList<String> mensajes = new ArrayList<>();
            for (Consignacionyventa consignacionyventa : consignacionyventaRepository.findAll()) {
                if (consignacionyventa.getFechafin() != null) {
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(consignacionyventa.getFechafin());
                    calendar.add(calendar.MONTH, -1);
                    System.out.println(calendar.getTime());
                    SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
                    String formatfechadb = new String();

                    formatfechadb = simpleDateFormat.format(calendar.getTime());


                    System.out.println(formatfechadb);
                    while (formatfechadb.equals(formatnow)) {
                        //List<Consignacionyventa> listaconsigs = new List<>;
                        ArrayList<Consignacionyventa> listconsigs = new ArrayList<>();
                        listconsigs.add(consignacionyventa);
                        for (Consignacionyventa info : listconsigs) {
                            String fechamsg = simpleDateFormat1.format(info.getFechafin());
                            mensajes.add("Numero de pedido : " + info.getNumeropedido() + " fecha de vencimiento: " + fechamsg + "\n");
                        }
                        break;

                    }

                }

            }
            String newline = System.lineSeparator();
            String mensajefin = String.join("<br>", mensajes);
            Usuario usuario1 = (Usuario) session.getAttribute("usuario");
            Calendar calendar3 = Calendar.getInstance();
            calendar3.setTime(nowdate);

            System.out.println(calendar3.get(Calendar.DAY_OF_MONTH));
            Notificaciones notificaciones = notificacionesRepository.findByUserId(usuario1.getIdusuario());
            Calendar calendar4FechaNotis = Calendar.getInstance();
            calendar4FechaNotis.setTime(notificaciones.getFecha());
            //calendar4FechaNotis.add(calendar4FechaNotis.MONTH,-1);
            if (calendar4FechaNotis.getTime().getMonth() != calendar3.getTime().getMonth()){
                notificacionesRepository.actualizarFlagFalse(usuario1.getIdusuario());
            }
            Notificaciones notificaciones1 = notificacionesRepository.findByUserId(usuario1.getIdusuario());
            if(!notificaciones1.isFlag()){
                if (calendar3.get(Calendar.DAY_OF_MONTH) >= 15 && calendar3.get(Calendar.DAY_OF_MONTH)<=20) {
                    if (mensajes.isEmpty()) {
                        String mensaje1 = "¡Hola! este es un mensaje automatico del sistema <br><br>En este momento ninguna consignacion esta cerca de su fecha de vencimiento";
                       sendMailService.sendMail(usuario1.getCorreo(), "saritaatanacioarenas@gmail.com", "Notificacion sobre vencimiento de consignacion - Mosqoy", mensaje1);
                    } else {

                        String mensaje = "¡Hola! este es un mensaje automatico del sistema <br><br>El sistema le avisa que el/los siguiente(s) pedido(s) :"
                                + "<br><br>" + mensajefin + "<br><br> esta(n) proximo(s) a vencer";
                        sendMailService.sendMail(usuario1.getCorreo(), "saritaatanacioarenas@gmail.com", "Notificacion sobre vencimiento de consignacion - Mosqoy", mensaje);
                    }
                }
                notificacionesRepository.actualizarFlagFecha(usuario1.getIdusuario(),calendar3.getTime());
            }


            return "redirect:/inventarioPrincipal";
        } else {
            return "login/login";
        }


    }
}
