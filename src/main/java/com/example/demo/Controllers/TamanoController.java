package com.example.demo.Controllers;

import com.example.demo.Entity.Tamano;
import com.example.demo.Repository.TamanoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.Optional;

@Controller
@RequestMapping("tamano")
public class TamanoController {

    @Autowired
    TamanoRepository tamanoRepository;

    @GetMapping(value = {"lista",""})
    public String listar(Model model){
        model.addAttribute("lista", tamanoRepository.findAll());
        return "Tamano/lista";
    }

//    @GetMapping("nuevo")
//    public String nuevo(@ModelAttribute("tamano") Tamano tamano){
//        return "Tamano/newEdit";
//    }
//
//    @PostMapping("/guardar")
//    public String guardar(@ModelAttribute("tamano") @Valid Tamano tamano, BindingResult bindingResult,
//                          RedirectAttributes attr, Model model){
//
//        if (bindingResult.hasErrors()){
//            return "Tamano/newEdit";
//        }else {
//            if (tamano.getIdtamano()==0){
//                attr.addFlashAttribute("msg", "Creado exitosamente");
//            }else {
//                attr.addFlashAttribute("msg", "Actualizado exitosamente");
//            }
//        }
//        tamanoRepository.save(tamano);
//        return "redirect:/Tamano/lista";
//    }
//
//    @GetMapping("editar")
//    public String editar(@ModelAttribute("tamano") Tamano tamano,
//                         Model model,
//                         @RequestParam("id") int id){
//
//        Optional<Tamano> opt = tamanoRepository.findById(id);
//
//        if (opt.isPresent()){
//            tamano= opt.get();
//            model.addAttribute("tamano", tamano);
//            return "Tamano/newEdit";
//
//        }else {
//            return "redirect:/categoria/lista";
//        }
//
//    }
//
//
//    @GetMapping("/delete")
//    public String borrar (@RequestParam("id") int id, RedirectAttributes attr){
//
//        Optional<Tamano> opt = tamanoRepository.findById(id);
//
//        if (opt.isPresent()){
//            attr.addFlashAttribute("msg", "Borrado exitosamente");
//            tamanoRepository.deleteById(id);
//        }
//        return "redirect:/Tamano";
//    }


}

