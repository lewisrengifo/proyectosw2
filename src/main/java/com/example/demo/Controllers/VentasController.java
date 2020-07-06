package com.example.demo.Controllers;


import com.example.demo.Dto.ProductoServiceApi;
import com.example.demo.Entity.*;
import com.example.demo.Repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.example.demo.Entity.Producto;
import com.example.demo.Repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/venta")
public class VentasController {
    @Autowired
    InventarioSedeRepository inventarioSedeRepository;
    @Autowired
    ProductoRepository productoRepository;

    @Autowired
    InventarioproductoRepository inventarioproductoRepository;
    @Autowired
    VentaRepository ventaRepository;
    @Autowired
    TiendaRepository tiendaRepository;

    @GetMapping(value={"/listaVentas",""})
    public String listarVentas(Model model, HttpSession session){
        Usuario usuariologueado = (Usuario) session.getAttribute("usuario");
        model.addAttribute("listaVentas", ventaRepository.listaVentasPorSede(usuariologueado.getSede_idsede().getIdsede()));
        return "venta/listaventa";
    }

    @GetMapping("/registroventa")
    public String registrarVenta(Model model, @ModelAttribute("ventas") Ventas ventas,
                                 RedirectAttributes redirectAttributes, HttpSession session) {

        Usuario usuariologueado = (Usuario) session.getAttribute("usuario");

        int sedeUsuario = usuariologueado.getSede_idsede().getIdsede();
        model.addAttribute("inventarioSedeProducto", inventarioSedeRepository.listarInventarioPorSede(sedeUsuario));
        model.addAttribute("listaTiendas",tiendaRepository.listaTiendasPorSede(sedeUsuario));
        model.addAttribute("usuarioRol",usuariologueado.getRol_idrol().getNombre());
        model.addAttribute("idsede",usuariologueado.getSede_idsede().getIdsede());

        return "venta/registroventa";
    }


    @PostMapping("/agregarVenta")
    public String ingresarVentas(Model model,@ModelAttribute("ventas")@Valid Ventas ventas, BindingResult bindingResult,
                                 RedirectAttributes redirectAttributes, HttpSession session){

        Usuario usuariologueado = (Usuario) session.getAttribute("usuario");
        if (bindingResult.hasErrors()) {
            int sedeUsuario = usuariologueado.getSede_idsede().getIdsede();
            model.addAttribute("inventarioSedeProducto", inventarioSedeRepository.listarInventarioPorSede(sedeUsuario));
            model.addAttribute("listaTiendas",tiendaRepository.listaTiendasPorSede(sedeUsuario));
            model.addAttribute("usuarioRol",usuariologueado.getRol_idrol().getNombre());
            model.addAttribute("idsede",usuariologueado.getSede_idsede().getIdsede());
            return "venta/registroventa";
        }else{
            if (ventas.getIdventas() == 0) {
                for (Ventas unoDeVentas : ventaRepository.findAll()) {
                    if (ventas.getNumerodocumento().equals(unoDeVentas.getNumerodocumento())) {
                        redirectAttributes.addFlashAttribute("msg1", "Documento de venta existente.");
                        redirectAttributes.addFlashAttribute("ventas", ventas);
                        int sedeUsuario = usuariologueado.getSede_idsede().getIdsede();
                        model.addAttribute("inventarioSedeProducto", inventarioSedeRepository.listarInventarioPorSede(sedeUsuario));
                        model.addAttribute("listaTiendas",tiendaRepository.listaTiendasPorSede(sedeUsuario));
                        model.addAttribute("usuarioRol",usuariologueado.getRol_idrol().getNombre());
                        model.addAttribute("idsede",usuariologueado.getSede_idsede().getIdsede());
                        return "venta/registroventa";
                    } else {
                        model.addAttribute("listaVentas", ventaRepository.listaVentasPorSede(usuariologueado.getSede_idsede().getIdsede()));
                        redirectAttributes.addFlashAttribute("msg2", "Venta registrada exitosamente.");
                        ventaRepository.save(ventas);
                        return "redirect:/venta";
                    }
                }
            } else {
                for (Ventas unoDeVentas2 : ventaRepository.buscarmenosmio(ventas.getIdventas())) {
                    if (ventas.getNumerodocumento().equals(unoDeVentas2.getNumerodocumento())) {
                        redirectAttributes.addFlashAttribute("msg1", "Documento a modificar ya existe.");
                        redirectAttributes.addFlashAttribute("ventas", ventas);
                        int sedeUsuario = usuariologueado.getSede_idsede().getIdsede();
                        model.addAttribute("inventarioSedeProducto", inventarioSedeRepository.listarInventarioPorSede(sedeUsuario));
                        model.addAttribute("listaTiendas",tiendaRepository.listaTiendasPorSede(sedeUsuario));
                        model.addAttribute("usuarioRol",usuariologueado.getRol_idrol().getNombre());
                        model.addAttribute("idsede",usuariologueado.getSede_idsede().getIdsede());
                        return "venta/registoventa";
                    } else {
                        model.addAttribute("listaVentas", ventaRepository.listaVentasPorSede(usuariologueado.getSede_idsede().getIdsede()));
                        redirectAttributes.addFlashAttribute("msg2", "Venta actualizada exitosamente.");
                        ventaRepository.save(ventas);
                        return "redirect:/venta";
                    }
                }
            }
            model.addAttribute("listaVentas", ventaRepository.listaVentasPorSede(usuariologueado.getSede_idsede().getIdsede()));
            return "redirect:/venta";

        }

    }
}