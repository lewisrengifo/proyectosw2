package com.example.demo.Controllers;

import com.example.demo.Entity.Linea;
import com.example.demo.Repository.LineaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.Optional;

@Controller
@RequestMapping("linea")
public class LineaController {

    @Autowired
    LineaRepository lineaRepository;

    @GetMapping(value = {"lista", ""})
    public String listar(Model model){

        model.addAttribute("lista", lineaRepository.findAll());
        return "Linea/lista";
    }

    //@GetMapping("nuevo")
    //public String nuevaLinea(@ModelAttribute("linea")  Linea linea){ return "Linea/newEdit";}

//    @PostMapping("/guardar")
//    public String guardarLinea (@ModelAttribute ("linea") @Valid Linea linea, BindingResult bindingResult,
//                                Model model, RedirectAttributes attr){
//
//        if (bindingResult.hasErrors()){
//            return "Linea/newEdit";
//        }else {
//
//            if (linea.getIdlinea() == 0) {
//
//                attr.addFlashAttribute("msg", "Creado correctamente");
//            } else {
//                attr.addFlashAttribute("msg", "Actualizado correctamente");
//            }
//        }
//            lineaRepository.save(linea);
//            return "redirect:/Linea/lista";
//
//    }
//
//    @GetMapping("editar")
//    public String editarLinea (@ModelAttribute ("linea") Linea linea,
//                               Model model,
//                               @RequestParam("id") int id){
//
//        Optional<Linea> opt = lineaRepository.findById(id);
//
//        if (opt.isPresent()){
//            linea= opt.get();
//            model.addAttribute("linea", linea);
//            return "Linea/newEdit";
//
//        }else {
//            return "redirect:/Linea/lista";
//        }
//
//    }
//
//
//    @GetMapping("/delete")
//    public String borrar (@RequestParam("id") int id, RedirectAttributes attr){
//
//        Optional<Linea> opt = lineaRepository.findById(id);
//
//        if (opt.isPresent()){
//            attr.addFlashAttribute("msg", "Borrado exitosamente");
//            lineaRepository.deleteById(id);
//        }
//        return "redirect:/Linea";
//    }


}