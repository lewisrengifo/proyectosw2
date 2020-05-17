package com.example.demo.Controllers;

import com.example.demo.Entity.Tamano;
import com.example.demo.Repository.TamanoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller
public class TamanoController {

    @Autowired
    TamanoRepository tamanoRepository;

    @GetMapping("nuevo")
    public String nuevo(@ModelAttribute("tamano") Tamano tamano){


        return "tamano/newEdit";
    }

    @PostMapping("/guardar")
    public String guardar(@ModelAttribute("tamano") Tamano tamano,
                          Model model){

        tamanoRepository.save(tamano);

        return "redirect:/categoria/lista";
    }

    @GetMapping("editar")
    public String editar(@ModelAttribute("tamano") Tamano tamano,
                         Model model,
                         @RequestParam("id") int id){

        Optional<Tamano> opt = tamanoRepository.findById(id);

        if (opt.isPresent()){
            tamano= opt.get();
            model.addAttribute("tamano", tamano);
            return "tamano/newEdit";

        }else {
            return "redirect:/categoria/lista";
        }

    }


    @GetMapping("/delete")
    public String borrar (@RequestParam("id") int id){

        Optional<Tamano> opt = tamanoRepository.findById(id);

        if (opt.isPresent()){
            tamanoRepository.deleteById(id);
        }
        return "redirect:/categoria";
    }


}

