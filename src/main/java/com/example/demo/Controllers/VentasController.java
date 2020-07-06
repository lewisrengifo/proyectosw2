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
    public String registrarVenta(Model model, @ModelAttribute("ventas")@Valid Ventas ventas, BindingResult bindingResult,
                                 RedirectAttributes redirectAttributes, HttpSession session) {

        Usuario usuariologueado = (Usuario) session.getAttribute("usuario");

        int sedeUsuario = usuariologueado.getSede_idsede().getIdsede();
        model.addAttribute("inventarioSedeProducto", inventarioSedeRepository.listarInventarioPorSede(sedeUsuario));
        model.addAttribute("listaTiendas",tiendaRepository.listaTiendasPorSede(sedeUsuario));
        model.addAttribute("usuarioRol",usuariologueado.getRol_idrol().getNombre());
        model.addAttribute("idsede",usuariologueado.getSede_idsede().getIdsede());

        if (bindingResult.hasErrors()) {
            return "venta/registoventa";
        }
        for (Ventas ventas1 : ventaRepository.findAll()) {
            if (ventas1.getIdventas()== 0) {
                if (ventas.getNumerodocumento().equals(ventas1.getNumerodocumento())) {
                    redirectAttributes.addFlashAttribute("msg1", "Documento de venta existente.");
                    redirectAttributes.addFlashAttribute("ventas", ventas);
                    return "venta/registoventa";
                } else if (ventas1.getIdventas() == 0) {
                    redirectAttributes.addFlashAttribute("msg2", "Venta registrada exitosamente.");
                    return "redirect:/venta";
                //}
                //BLOQUE PARA EDITAR VENTA
                } else {
                    redirectAttributes.addFlashAttribute("msg2", "Venta actualizada exitosamente");
                }
            } else{
                for (Ventas ventas2 : ventaRepository.buscarmenosmio(ventas.getIdventas())) {
                    if (ventas.getNumerodocumento().equals(ventas2.getNumerodocumento())) {
                        redirectAttributes.addFlashAttribute("msg1", "Documento de venta ya existe.");
                        redirectAttributes.addFlashAttribute("ventas", ventas);
                        return "venta/registoventa";
                    //}
                    /* BLOQUE PARA EDITAR VENTA*/
                    } else {
                        redirectAttributes.addFlashAttribute("msg2", "Venta actualizada exitosamente");
                    }
                }
            }

        }
        ventaRepository.save(ventas);
        return "venta/registoventa";
    }

    @PostMapping("/agregarVenta")
    public String ingresarVentas(Model model,@ModelAttribute("ventas")Ventas ventas){

        ventaRepository.save(ventas);
        return "redirect:/venta";
    }
}