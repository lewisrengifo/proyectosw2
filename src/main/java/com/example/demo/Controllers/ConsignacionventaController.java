package com.example.demo.Controllers;

import com.example.demo.Entity.Consignacionyventa;
import com.example.demo.Entity.Inventarioproducto;
import com.example.demo.Repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/ConsignacionVenta")
public class ConsignacionventaController {

    @Autowired
    ConsignacionyventaRepository consignacionyventaRepository;

    @Autowired
    LineaRepository lineaRepository;
    @Autowired
    ProductoRepository productoRepository;
    @Autowired
    TamanoRepository tamanoRepository;
    @Autowired
    CategoriaRepository categoriaRepository;
    @Autowired
    InventarioproductoRepository inventarioproductoRepository;
    @GetMapping(value = {"/lista",""})
    public String listaConsignacionVenta(Model model,@ModelAttribute("consigYVenta") Consignacionyventa consigYventa){
        model.addAttribute("listaConsignacionVenta",consignacionyventaRepository.findAll());
        return "consigVenta/consignYventaLista";
    }

    @GetMapping("/agregarProductosCV")
    public String vistaagregarproductos(Model model, @ModelAttribute("inventarioProducto") Inventarioproducto invPro){

        model.addAttribute("listalinea",lineaRepository.findAll());
        model.addAttribute("listaproducto",productoRepository.findAll());
        model.addAttribute("listacategoria",categoriaRepository.findAll());
        model.addAttribute("listatamano",tamanoRepository.findAll());
        model.addAttribute("listaPedidosCv",consignacionyventaRepository.findAll());
        return "consigVenta/agregarProductos";
    }

    @PostMapping("/agregarProducto")
    public String agregarProductosEnPedido(Model model,
                                           @ModelAttribute("inventarioProducto") Inventarioproducto invPro){

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

        invPro.setCodigogenerado("cualquierhuevada");
        inventarioproductoRepository.save(invPro);
        return "redirect:/ConsignacionVenta";

    }

}
