package com.example.demo.Controllers;

import com.example.demo.Entity.Consignacionyventa;
import com.example.demo.Entity.Inventarioproducto;
import com.example.demo.Repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/inventarioPrincipal")
public class InventarioproductoController {

    @Autowired
    LineaRepository lineaRepository;
    @Autowired
    ProductoRepository productoRepository;
    @Autowired
    CategoriaRepository categoriaRepository;
    @Autowired
    TamanoRepository tamanoRepository;
    @Autowired
    ArtesanoRepository artesanoRepository;
    @Autowired
    ConsignacionyventaRepository consignacionyventaRepository;
    @Autowired
    InventarioproductoRepository inventarioproductoRepository;



    @GetMapping(value = {"","/","/lista"})
    public String listaInventarioProducto(Model model){
        return "inventario/inventarioPrincipal";
    }

    @GetMapping("/agregarInventario")
    public String consignacionYVenta(@ModelAttribute("consigYVenta") Consignacionyventa consigYventa, Model model){
        model.addAttribute("listaArtesano",artesanoRepository.findAll());
        return "inventario/consigYventa";
    }

    @PostMapping("/agregarConsigVenta")
    public String ingresarConsignacionOventa(Model model,@ModelAttribute("inventarioProducto") Inventarioproducto invPro,
                                               @ModelAttribute("consigYVenta") Consignacionyventa consigYventa, HttpSession session){


        model.addAttribute("listalinea",lineaRepository.findAll());
        model.addAttribute("listaproducto",productoRepository.findAll());
        model.addAttribute("listacategoria",categoriaRepository.findAll());
        model.addAttribute("listatamano",tamanoRepository.findAll());
        model.addAttribute("consigYVenta",consigYventa);

        return "inventario/inventarioProducto";
    }



    @PostMapping("/agregarProducto")
    public String agregarProductosEnPedido(Model model, @ModelAttribute("inventarioProducto") Inventarioproducto invPro,
                                           @ModelAttribute("consigYVenta") Consignacionyventa consigYventa, HttpSession session){

        List<Inventarioproducto> listaProductosEnPedido = (List<Inventarioproducto>) session.getAttribute("listaProductosEnPedido");

        Consignacionyventa consigYventa1 = consigYventa;
        Inventarioproducto invPro1 = invPro;

        invPro1.setConsignacionyventa(consigYventa1);

        listaProductosEnPedido.add(invPro1);

        session.setAttribute("listaProductosEnPedido",listaProductosEnPedido);

        model.addAttribute("inventarioProducto",invPro = null);
        model.addAttribute("consigYVenta",consigYventa);
        return "inventario/inventarioProducto";
    }

    @PostMapping("/confirmarPedido")
    public String confirmacionPedidos(Model model, @ModelAttribute("inventarioProducto") Inventarioproducto invPro,
                                      @ModelAttribute("consigYVenta") Consignacionyventa consigYventa, HttpSession session){

        return "inventario/confirmarpedido";
    }











}
