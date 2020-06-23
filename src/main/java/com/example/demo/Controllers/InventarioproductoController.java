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
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.zip.DataFormatException;

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
        model.addAttribute("listaInventarioPrincipal", inventarioproductoRepository.findAll());
        return "inventario/inventarioPrincipal";
    }

    @GetMapping("/agregarInventario")
    public String consignacionYVenta(@ModelAttribute("consigYVenta") Consignacionyventa consigYventa, Model model){
        model.addAttribute("listaArtesano",artesanoRepository.findAll());
        return "inventario/consigYventa";
    }

    @PostMapping("/agregarConsigVenta")
    public String ingresarConsignacionOventa(Model model,@ModelAttribute("inventarioProducto") Inventarioproducto invPro,
                                               @ModelAttribute("consigYVenta") Consignacionyventa consigYventa){
     consignacionyventaRepository.save(consigYventa);

       return "redirect:/inventarioPrincipal/sgteProductos";
    }

    @GetMapping("/sgteProductos")
    public String vistaagregarproductos(Model model, @ModelAttribute("inventarioProducto") Inventarioproducto invPro,
                                        @ModelAttribute("consigYVenta") Consignacionyventa consigYventa){
        Optional<Consignacionyventa> ultimaConsigOventa = consignacionyventaRepository.findById(consignacionyventaRepository.ultimoConsiyVentaIngresado());

        model.addAttribute("listalinea",lineaRepository.findAll());
        model.addAttribute("listaproducto",productoRepository.findAll());model.addAttribute("listacategoria",categoriaRepository.findAll());
        model.addAttribute("listatamano",tamanoRepository.findAll());
      model.addAttribute("consigYventa1",ultimaConsigOventa.get());
        return "inventario/inventarioProducto";
    }

    @PostMapping("/agregarProducto")
    public String agregarProductosEnPedido(Model model, @ModelAttribute("inventarioProducto") Inventarioproducto invPro,
                                           @ModelAttribute("consigYVenta") Consignacionyventa consigYventa){


        //List<Inventarioproducto> listaProductosEnPedido = (List<Inventarioproducto>) session.getAttribute("listaProductosEnPedido");

        //Optional<Consignacionyventa> ultimaConsigOventa = consignacionyventaRepository.findById(consignacionyventaRepository.ultimoConsiyVentaIngresado());


        //Date fechatudei = new Date();
        /*
       invPro.setFechainicio(fechatudei);
        if(ultimaConsigOventa.get().getTipo().equals("consignacion")){
            StringBuilder appe = new StringBuilder().append(invPro.getColor()).append(invPro.getCategoria());
            invPro.setCodigogenerado(appe.toString());
        }else{
            StringBuilder appe1 = new StringBuilder().append(invPro.getColor()).append(invPro.getCategoria()).append(invPro.getFacilitador());
            invPro.setCodigogenerado(appe1.toString());
        }
        */
        Consignacionyventa ultimaConsigOventa = consignacionyventaRepository.findTopByOrderByIdconsignacionDesc();
        invPro.setConsignacionyventa(ultimaConsigOventa);

        invPro.setCodigogenerado("cualquierhuevada");
        inventarioproductoRepository.save(invPro);
        return "redirect:/inventarioPrincipal/sgteProductos";

    }

    @PostMapping("/confirmarPedido")
    public String confirmacionPedidos(Model model, @ModelAttribute("inventarioProducto") Inventarioproducto invPro,
                                      @ModelAttribute("consigYVenta") Consignacionyventa consigYventa){
        return "inventario/confirmarpedido";
    }











}
