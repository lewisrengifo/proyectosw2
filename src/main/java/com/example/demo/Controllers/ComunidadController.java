package com.example.demo.Controllers;


import com.example.demo.Entity.Comunidad;
import com.example.demo.Repository.ComunidadRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.Optional;

@Controller
@RequestMapping("/comunidad")
public class ComunidadController {

    @Autowired
    ComunidadRepository comunidadRepository;

    @GetMapping("")
    public String listaComunidad(Model model){
        model.addAttribute("listaComunidad",comunidadRepository.findAll());
        return "comunidad/lista";

    }

    @GetMapping("/nuevo")
    public String nuevaComunidad(@ModelAttribute("comunidad") Comunidad c){

        return "comunidad/newEdit";
    }

    @GetMapping("/editar")
    public String editarComunidad(@ModelAttribute("comunidad") Comunidad comunidad,
                                  @RequestParam("id") int id, Model model){
        Optional<Comunidad> optionalComunidad = comunidadRepository.findById(id);
        if(optionalComunidad.isPresent()){
            comunidad = optionalComunidad.get();
            model.addAttribute("comunidad",comunidad);
            return "comunidad/newEdit";
        }
        else {
            return  "redirect:/comunidad";
        }

    }

    @GetMapping("/borrar")
    public String borrarComunidad(Model model,
                                  @RequestParam("id") int id, RedirectAttributes att){

        Optional<Comunidad> optionalComunidad = comunidadRepository.findById(id);
        if(optionalComunidad.isPresent()){
            att.addFlashAttribute("msgCo", "Borrado Exitosamente");
            comunidadRepository.deleteById(id);
            return "redirect:/comunidad";
        }
        return "redirect:/comunidad";
    }

    @PostMapping("/guardar")
    public String guardarComunidad(@ModelAttribute("comunidad") @Valid Comunidad comunidad, BindingResult bindingResult,
                                        RedirectAttributes att){

        if(bindingResult.hasErrors()){
            return "comunidad/newEdit";
        }else{
            if (comunidad.getIdcomunidad()==0){
                att.addFlashAttribute("msgCo","Comunidad Creada Exitosamente");
            }else{
                att.addFlashAttribute("msgCo","Comunidad Actualizada Exitosamente");
            }
            comunidadRepository.save(comunidad);
            return "redirect:/comunidad";
        }
    }


}
