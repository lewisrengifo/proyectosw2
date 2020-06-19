package com.example.demo.Controllers;

import com.example.demo.Entity.Sede;
import com.example.demo.Entity.Usuario;
import com.example.demo.Repository.SedeRepository;
import com.example.demo.service.SedeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.constraints.Positive;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/sede")
public class SedeController {
    @Autowired
    SedeRepository sedeRepository;
    @Autowired
    SedeService sedeService;
    @GetMapping(value = {"", "lista"})
    public String listarSedes(@RequestParam Map<String, Object> params, Model model){
        try {
            int page = params.get("page") != null ? (Integer.valueOf(params.get("page").toString()) - 1) : 0;
        } catch (NumberFormatException e) {
            return "redirect:/sede/lista";
        }
        int page = params.get("page") != null ? (Integer.valueOf(params.get("page").toString()) - 1) : 0;

        if (page < 0) {
            return "redirect:/sede/lista";
        }

        Page<Sede> pageSede = sedeService.getAll(page);
        int totalPage = pageSede.getTotalPages();
        long totalItems = pageSede.getTotalElements();
        if (totalPage > 0) {
            List<Integer> pages = IntStream.rangeClosed(1, totalPage).boxed().collect(Collectors.toList());
            model.addAttribute("page", pages);
        }
        model.addAttribute("listSedes", pageSede.getContent());
        model.addAttribute("totalItems", totalItems);

        model.addAttribute("current", page + 1);
        model.addAttribute("next", page + 2);
        model.addAttribute("prev", page);
        model.addAttribute("last", totalPage);

        //model.addAttribute("listaUsuarios", usuarioRepository.findAll());
        return "sede/lista";

    }
    @GetMapping("/nuevo")
    public String nuevaSede(Model model, @ModelAttribute Sede sede){

        return "sede/form";
    }
    @PostMapping("/guardar")
    public String guardarSede(@ModelAttribute("sede") Sede sede, RedirectAttributes redirectAttributes){
        if(sede.getIdrol()==0){
            redirectAttributes.addFlashAttribute("mgs", "La sede se creo correctamente");

        }else {
            redirectAttributes.addFlashAttribute("msg", "La sede se actualizó correctamente");
        }
        sedeRepository.save(sede);
        return "redirect:/sede/lista";
    }
    @GetMapping("/editar")
    public String editarSede(@ModelAttribute("sede") Sede sede, Model model, @RequestParam("id") int id){
        Optional<Sede> optionalSede = sedeRepository.findById(id);
        if(optionalSede.isPresent()){
            sede =optionalSede.get();
            model.addAttribute(sede);
            return "sede/form";
        } else{
            return "redirect:/sede/lista";
        }

    }
}
