package com.example.demo.Controllers;

import com.example.demo.Entity.Inventariosede;
import com.example.demo.Entity.Sede;
import com.example.demo.Repository.InventarioproductoRepository;
import com.example.demo.Repository.SedeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/inventarioSede")
public class InventariosedeController {

    @Autowired
    InventarioproductoRepository inventarioproductoRepository;
    @Autowired
    SedeRepository sedeRepository;

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

}
