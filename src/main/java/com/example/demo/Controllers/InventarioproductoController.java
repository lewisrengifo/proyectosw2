package com.example.demo.Controllers;

import com.example.demo.Entity.Inventarioproducto;
import com.example.demo.Repository.InventarioproductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/inventarioPrincipal")
public class InventarioproductoController {

    @Autowired
    InventarioproductoRepository inventarioproductoRepository;

    @GetMapping(value = {"","/","/lista"})
    public String listaInventarioProducto(Model model){

        //model.addAttribute("listaInventarioProducto", inventarioproductoRepository.findAll());

        return "inventario/inventarioPrincipal";
    }

    @GetMapping("/agregarInventario")
    public String agregarInventario(@ModelAttribute("inventarioProducto")Inventarioproducto invPro){
        return "inventario/newEditInventarioPrin";
    }









}
