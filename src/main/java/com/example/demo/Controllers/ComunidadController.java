package com.example.demo.Controllers;


import com.example.demo.Dto.RendirizadorPaginas;
import com.example.demo.Entity.Comunidad;
import com.example.demo.Repository.ComunidadRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.awt.print.Pageable;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/comunidad")
public class ComunidadController {

    @Autowired
    ComunidadRepository comunidadRepository;

   
    @GetMapping("/nuevo")
    public String nuevaComunidad(@ModelAttribute("comunidad") Comunidad c){

        return "comunidad/newEdit";
    }

    @GetMapping("/editar")
    public String editarComunidad(@ModelAttribute("comunidad") Comunidad comunidad,
                                  @RequestParam("id") int id, Model model,RedirectAttributes att){
        Optional<Comunidad> optionalComunidad = comunidadRepository.findById(id);
        if(optionalComunidad.isPresent() && optionalComunidad.get().getIdcomunidad()>5 ){
            comunidad = optionalComunidad.get();
            model.addAttribute("comunidad",comunidad);
            return "comunidad/newEdit";
        }
        else {
            att.addFlashAttribute("msgE", "No puede editar esta Comunidad");
            return  "redirect:/comunidad";
        }

    }

    @GetMapping("/borrar")
    public String borrarComunidad(Model model,
                                  @RequestParam("id") int id, RedirectAttributes att){

        Optional<Comunidad> optionalComunidad = comunidadRepository.findById(id);
        if(optionalComunidad.isPresent() && optionalComunidad.get().getIdcomunidad()>5){
            att.addFlashAttribute("msgCo", "Comunidad Borrada Exitosamente");
            comunidadRepository.deleteById(id);
            return "redirect:/comunidad";
        }
        else
        {
            att.addFlashAttribute("msgE", "No puede borrar esta Comunidad");
            return "redirect:/comunidad";
        }

    }

    @PostMapping("/guardar")
    public String guardarComunidad(@ModelAttribute("comunidad") @Valid Comunidad comunidad, BindingResult bindingResult,
                                        RedirectAttributes att){

        if(bindingResult.hasErrors()){
            return "comunidad/newEdit";
        }else{
            if(comunidad.getIdcomunidad()==0){
                for (Comunidad com : comunidadRepository.findAll()) {
                    if (com.getNombrecomunidad().equalsIgnoreCase(comunidad.getNombrecomunidad()) || com.getCodigocomunidad().equalsIgnoreCase(comunidad.getCodigocomunidad())) {
                        if (com.getNombrecomunidad().equalsIgnoreCase(comunidad.getNombrecomunidad())) {
                            att.addFlashAttribute("msg1", "Nombre de Comunidad ya exite");
                            att.addFlashAttribute("comunidad", comunidad);
                        }
                        if (com.getCodigocomunidad().equalsIgnoreCase(comunidad.getCodigocomunidad())) {
                            att.addFlashAttribute("msg2", "Codigo de Comunidad ya existe");
                            att.addFlashAttribute("comunidad", comunidad);
                        }
                        return "redirect:/comunidad/nuevo";

                    } else if (comunidad.getIdcomunidad() == 0) {
                        att.addFlashAttribute("msgCo", "Comunidad Creada Exitosamente");
                    } else {
                        att.addFlashAttribute("msgCo", "Comunidad Actualizada Exitosamente");
                    }
                }
            }
            else{
                for(Comunidad comunidad1:comunidadRepository.mio(comunidad.getIdcomunidad())){
                    if (comunidad1.getNombrecomunidad().equalsIgnoreCase(comunidad.getNombrecomunidad()) || comunidad1.getCodigocomunidad().equalsIgnoreCase(comunidad.getCodigocomunidad())) {
                        if (comunidad1.getNombrecomunidad().equalsIgnoreCase(comunidad.getNombrecomunidad())) {
                            att.addFlashAttribute("msg1", "Nombre de Comunidad ya exite");
                            att.addFlashAttribute("comunidad", comunidad);
                        }
                        if (comunidad1.getCodigocomunidad().equalsIgnoreCase(comunidad.getCodigocomunidad())) {
                            att.addFlashAttribute("msg2", "Codigo de Comunidad ya existe");
                            att.addFlashAttribute("comunidad", comunidad);
                        }
                        return "redirect:/comunidad/nuevo";

                    } else if (comunidad.getIdcomunidad() == 0) {
                        att.addFlashAttribute("msgCo", "Comunidad Creada Exitosamente");
                    } else {
                        att.addFlashAttribute("msgCo", "Comunidad Actualizada Exitosamente");
                    }
                }
            }
            //String nom= comunidad.getNombrecomunidad().substring(0, 1).toUpperCase() + comunidad.getNombrecomunidad().substring(1);
            String nom=comunidad.getNombrecomunidad().substring(0, 1).toUpperCase() + comunidad.getNombrecomunidad().substring(1).toLowerCase();
            comunidad.setNombrecomunidad(nom);
            String cod=comunidad.getCodigocomunidad().toUpperCase();
            comunidad.setCodigocomunidad(cod);
            comunidadRepository.save(comunidad);
            return "redirect:/comunidad";
        }
    }

    @PostMapping("/buscar")
    public String filtar(@RequestParam("nom") String nom,Model model,RedirectAttributes att){
        if(nom.isEmpty()){
            att.addFlashAttribute("msgBuscador", "Campo vacio. Ingrese el dato a buscar");
            return "redirect:/comunidad";
        }
        else{

            model.addAttribute("listaComunidad",comunidadRepository.filtro(nom));
            return "comunidad/lista";
        }

    }


}
