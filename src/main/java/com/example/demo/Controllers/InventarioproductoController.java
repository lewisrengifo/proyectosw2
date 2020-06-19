package com.example.demo.Controllers;

import com.example.demo.Entity.Inventarioproducto;
import com.example.demo.Repository.*;
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
    LineaRepository lineaRepository;
    @Autowired
    ProductoRepository productoRepository;
    @Autowired
    CategoriaRepository categoriaRepository;
    @Autowired
    TamanoRepository tamanoRepository;
    @Autowired
    InventarioproductoRepository inventarioproductoRepository;

    @GetMapping(value = {"","/","/lista"})
    public String listaInventarioProducto(Model model){

        //model.addAttribute("listaInventarioProducto", inventarioproductoRepository.findAll());
        return "inventario/inventarioPrincipal";
    }

    @GetMapping("/agregarInventario")
    public String agregarInventario(@ModelAttribute("inventarioProducto")Inventarioproducto invPro,Model model){
        model.addAttribute("listalinea",lineaRepository.findAll());
        model.addAttribute("listaproducto",productoRepository.findAll());
        model.addAttribute("listacategoria",categoriaRepository.findAll());
        model.addAttribute("listatamano",tamanoRepository.findAll());
        return "inventario/newEditInventarioPrin";
    }









}
