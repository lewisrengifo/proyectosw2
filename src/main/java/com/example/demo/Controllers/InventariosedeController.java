package com.example.demo.Controllers;

import com.example.demo.Entity.Inventariosede;
import com.example.demo.Entity.Sede;
import com.example.demo.Repository.InventarioSedeRepository;
import com.example.demo.Repository.InventarioproductoRepository;
import com.example.demo.Repository.SedeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/agregarStock")
    public String agregarStock(Model model,@ModelAttribute("inventariosede") Inventariosede inventariosede, @ModelAttribute("sede") Sede sede){
        Inventariosede invs = new Inventariosede();

        invs = inventariosede;
        inventariosede.setEstado("Enviado");
        inventarioSedeRepository.save(inventariosede);

        return "redirect:/inventarioSede/asignarStock";
    }

}
