package com.example.demo.Controllers;

import com.example.demo.Entity.Artesano;
import com.example.demo.Entity.Comunidad;
import com.example.demo.Repository.ArtesanoRepository;
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
@RequestMapping("/artesano")
public class ArtesanoController {

    @Autowired
    ArtesanoRepository artesanoRepository;
    @Autowired
    ComunidadRepository comunidadRepository;

    @GetMapping("")
    public String listaArtesano(Model model){
        model.addAttribute("listaArtesano",artesanoRepository.findAll());
        model.addAttribute("listacomunidades",comunidadRepository.findAll());
        return "artesano/lista";

    }

    @GetMapping("/nuevo")
    public String nuevoArtesano(@ModelAttribute("artesano") Artesano a,Model model){
        model.addAttribute("listacomunidades",comunidadRepository.findAll());
        return "artesano/newEdit";
    }

    @GetMapping("/editar")
    public String editarArtesano(@ModelAttribute("artesano") Artesano artesano,
                                  @RequestParam("id") int id, Model model){

        Optional<Artesano> optionalArtesano = artesanoRepository.findById(id);
        if(optionalArtesano.isPresent()){
            artesano = optionalArtesano.get();
            model.addAttribute("artesano",artesano);
            model.addAttribute("listacomunidades",comunidadRepository.findAll());

            return "artesano/newEdit";
        }
        else {
            return  "redirect:/artesano";
        }

    }

    @GetMapping("/borrar")
    public String borrarArtesano(Model model,
                                  @RequestParam("id") int id, RedirectAttributes att){

        Optional<Artesano> optionalArtesano = artesanoRepository.findById(id);
        if(optionalArtesano.isPresent()){
            att.addFlashAttribute("msgAr", "Borrado Exitosamente");
            artesanoRepository.deleteById(id);
            return "redirect:/artesano";
        }
        return "redirect:/artesano";
    }

    @PostMapping("/guardar")
    public String guardarArtesano(@ModelAttribute("comunidad") @Valid Artesano artesano, BindingResult bindingResult,
                                   RedirectAttributes att){

        if(bindingResult.hasErrors()){
            return "artesano/newEdit";
        }else{
            if (artesano.getIdartesano()==0){
                att.addFlashAttribute("msgAr","Artesano Creado Exitosamente");
            }else{
                att.addFlashAttribute("msgAr","Artesano Actualizado Exitosamente");
            }
            artesanoRepository.save(artesano);
            return "redirect:/artesano";
        }
    }

    @PostMapping("/buscar")
    public String filtarArtesanoPorComunidad(@RequestParam("idcomunidad") int idcomunidad,Model model){

        model.addAttribute("listaArtesanos",artesanoRepository.filtarPorComunidad(idcomunidad));
        model.addAttribute("listacomunidades",comunidadRepository.findAll());
        return "artesano/lista";
    }


}
