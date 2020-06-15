package com.example.demo.Controllers;

import com.example.demo.Entity.Tienda;
import com.example.demo.Repository.TiendaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("tienda")
public class TiendaController {

    @Autowired
    TiendaRepository tiendaRepository;

    @GetMapping(value = {"lista", ""})
    public String listar (Model model){

        model.addAttribute("lista", tiendaRepository.findAll());

        return "Tienda/lista";
    }

    @GetMapping("nuevo")
    public String nuevo(@ModelAttribute("tienda") Tienda tienda){
        return "Tienda/newEdit";
    }

    @PostMapping("/guardar")
    public String guardar(@ModelAttribute("tienda") Tienda tienda,
                          Model model){

        tiendaRepository.save(tienda);

        return "redirect:/categoria/lista";
    }

    @GetMapping("editar")
    public String editar(@ModelAttribute("tienda") Tienda tienda,
                         Model model,
                         @RequestParam("id") int id){

        Optional<Tienda> opt = tiendaRepository.findById(id);

        if (opt.isPresent()){
            tienda= opt.get();
            model.addAttribute("tienda", tienda);
            return "Tienda/newEdit";

        }else {
            return "redirect:/categoria/lista";
        }

    }


    @GetMapping("/delete")
    public String borrar (@RequestParam("id") int id){

        Optional<Tienda> opt = tiendaRepository.findById(id);

        if (opt.isPresent()){
            tiendaRepository.deleteById(id);
        }
        return "redirect:/categoria";
    }




}

