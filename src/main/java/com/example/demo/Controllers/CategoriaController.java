package com.example.demo.Controllers;


import com.example.demo.Entity.Categoria;
import com.example.demo.Entity.Comunidad;
import com.example.demo.Repository.CategoriaRepository;
import com.example.demo.Repository.ComunidadRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/categoria")
public class CategoriaController {

    @Autowired
    CategoriaRepository categoriaRepository;

    @Autowired
    ComunidadRepository comunidadRepository;
    @GetMapping("")
    public String listaCategorias(Model model){

        List<Comunidad> listaComunidad = comunidadRepository.findAll();
        model.addAttribute("listaComunidad",listaComunidad);

        List<Categoria> listaCate = categoriaRepository.findAll();
        model.addAttribute("listacategoria",listaCate);

        return "categoria/lista";
    }

    @GetMapping("/nuevo")
    public String nuevaCategoria(@ModelAttribute("categoria") Categoria c){
        return "categoria/newEdit";

    }

    @PostMapping("/guardar")
    public String guardarCategoria(@ModelAttribute("categoria") @Valid Categoria categoria,
                                   BindingResult bindingResult,
                                   RedirectAttributes att, Model model){
        if(bindingResult.hasErrors()){
            return "categoria/newEdit";
        }else{
            if(categoria.getIdcategoria()==0){
                for (Categoria com : categoriaRepository.findAll()) {
                    if (com.getNombrecategoria().equalsIgnoreCase(categoria.getNombrecategoria()) || com.getCodigocategoria().equalsIgnoreCase(categoria.getCodigocategoria())) {
                        if (com.getNombrecategoria().equalsIgnoreCase(categoria.getNombrecategoria())) {
                            att.addFlashAttribute("msg1", "Nombre de Categoría ya existe");
                            att.addFlashAttribute("categoria", categoria);
                        }
                        if (com.getCodigocategoria().equalsIgnoreCase(categoria.getCodigocategoria())) {
                            att.addFlashAttribute("msg2", "Código de Categoría ya existe");
                            att.addFlashAttribute("categoria", categoria);
                        }
                        return "redirect:/categoria/nuevo";

                    } else if (categoria.getIdcategoria() == 0) {
                        att.addFlashAttribute("msgCo", "Categoria Creada Exitosamente");
                    } else {
                        att.addFlashAttribute("msgCo", "Categoria Actualizada Exitosamente");
                    }
                }
            }
            else{
                for(Categoria categoria1:categoriaRepository.mio(categoria.getIdcategoria())){
                    if (categoria1.getNombrecategoria().equalsIgnoreCase(categoria.getNombrecategoria()) || categoria1.getCodigocategoria().equalsIgnoreCase(categoria.getCodigocategoria())) {
                        if (categoria1.getNombrecategoria().equalsIgnoreCase(categoria.getNombrecategoria())) {
                            att.addFlashAttribute("msg1", "Nombre de Categoría ya existe");
                            att.addFlashAttribute("categoria", categoria);
                        }
                        if (categoria1.getCodigocategoria().equalsIgnoreCase(categoria.getCodigocategoria())) {
                            att.addFlashAttribute("msg2", "Codigo de Categoría ya existe");
                            att.addFlashAttribute("categoria", categoria);
                        }
                        return "redirect:/categoria/nuevo";

                    } else if (categoria.getIdcategoria() == 0) {
                        att.addFlashAttribute("msgCo", "Categoria Creada Exitosamente");
                    } else {
                        att.addFlashAttribute("msgCo", "Categoria Actualizada Exitosamente");
                    }
                }
            }
            String nom= categoria.getNombrecategoria().substring(0, 1).toUpperCase() + categoria.getNombrecategoria().substring(1);
            categoria.setNombrecategoria(nom);
            String cod=categoria.getCodigocategoria().toUpperCase();
            categoria.setCodigocategoria(cod);
            categoriaRepository.save(categoria);
            return "redirect:/categoria";
        }


    }

    @GetMapping("/editar")
    public String editarCategoria(@ModelAttribute("categoria") Categoria categoria,
                                  @RequestParam("id") int id, Model model){

        Optional<Categoria> CategoriaId = categoriaRepository.findById(id);
         if(CategoriaId.isPresent()){
             categoria = CategoriaId.get();
             model.addAttribute("categoria",categoria);
                     return "categoria/newEdit";
         }   else{return  "redirect:/categoria";}
    }
    @GetMapping("/borrar")
    public String borrarCategoria(Model model,
                                   @RequestParam("id") int id,RedirectAttributes att){
        Optional<Categoria> elimniarCate = categoriaRepository.findById(id);
        if(elimniarCate.isPresent()){
            att.addFlashAttribute("msg", "Borrado Exitosamente");
            categoriaRepository.deleteById(id);
            return "redirect:/categoria";
        }
        return "redirect:/categoria";
    }

}
