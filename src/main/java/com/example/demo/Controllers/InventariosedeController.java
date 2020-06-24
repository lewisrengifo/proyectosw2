package com.example.demo.Controllers;

import com.example.demo.Entity.Artesano;
import com.example.demo.Entity.Inventarioproducto;
import com.example.demo.Entity.Inventariosede;
import com.example.demo.Entity.Sede;
import com.example.demo.Repository.InventarioSedeRepository;
import com.example.demo.Repository.InventarioproductoRepository;
import com.example.demo.Repository.SedeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/inventarioSede")
public class InventariosedeController {

    @Autowired
    InventarioproductoRepository inventarioproductoRepository;
    @Autowired
    SedeRepository sedeRepository;
    @Autowired
    InventarioSedeRepository inventarioSedeRepository;

    @GetMapping("/asignarStock")
    public String asignarStock(Model model, @ModelAttribute("sede") Sede sede,
                               @ModelAttribute("inventariosede") Inventariosede inventariosede){

        model.addAttribute("inventario", inventarioproductoRepository.findAll());
        model.addAttribute("listaSede", sedeRepository.findAll());
        return "sede/asignarStock";
    }


    public String confirmarStock(Model model){


        return "";
    }

    @GetMapping(value = {"", "/lista"})
    public String listaInventarioSede(Model model) {


        model.addAttribute("listaInventarioSede",inventarioSedeRepository.findAll());


        return "/inventario/inventariosede";
    }



}
