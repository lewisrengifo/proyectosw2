package com.example.demo.Controllers;

import com.example.demo.Entity.Linea;
import com.example.demo.Repository.LineaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("linea")
public class LineaController {

    @Autowired
    LineaRepository lineaRepository;


    @GetMapping("nuevo")
    public String nuevaLinea(@ModelAttribute("linea") Linea linea){


        return "Linea/newEdit";
    }

    @PostMapping("/guardar")
    public String guardarLinea (@ModelAttribute ("linea") Linea linea,
                                Model model){

        lineaRepository.save(linea);

        return "redirect:/categoria/lista";
    }

    @GetMapping("editar")
    public String editarLinea (@ModelAttribute ("linea") Linea linea,
                               Model model,
                               @RequestParam("id") int id){

        Optional<Linea> opt = lineaRepository.findById(id);

        if (opt.isPresent()){
            linea= opt.get();
            model.addAttribute("linea", linea);
            return "Linea/newEdit";

        }else {
            return "redirect:/categoria/lista";
        }

    }


    @GetMapping("/delete")
    public String borrar (@RequestParam("id") int id){

        Optional<Linea> opt = lineaRepository.findById(id);

        if (opt.isPresent()){
            lineaRepository.deleteById(id);
        }
        return "redirect:/categoria";
    }


}