package com.example.demo.Controllers;


import com.example.demo.Entity.Artesano;
import com.example.demo.Entity.Categoria;
import com.example.demo.Entity.Comunidad;
import com.example.demo.Entity.Inventarioproducto;
import com.example.demo.Repository.CategoriaRepository;
import com.example.demo.Repository.ComunidadRepository;
import com.example.demo.Repository.InventarioproductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/categoria")
public class CategoriaController {

    @Autowired
    CategoriaRepository categoriaRepository;

    @Autowired
    ComunidadRepository comunidadRepository;
    @Autowired
    InventarioproductoRepository inventarioproductoRepository;

    @GetMapping("")
    public String listaCategorias(Model model , @RequestParam Map<String, Object> params,RedirectAttributes attr) {

        try {
            int page = params.get("page") != null ? (Integer.valueOf(params.get("page").toString()) - 1) : 0;
        } catch (NumberFormatException e) {
            return "redirect:/categoria";
        }

        int page = params.get("page") != null ? (Integer.valueOf(params.get("page").toString()) - 1) : 0;

        if (page < 0) {
            return "redirect:/categoria";
        }
        PageRequest pageRequest = PageRequest.of(page, 5);
        Page<Categoria> pageCat = categoriaRepository.findAll(pageRequest);
        long totalItems = pageCat.getTotalElements();
        int totalPages = pageCat.getTotalPages();

        //  if (currentPage<0 ) {
        //    currentPage = 0;
        //}
        if (totalPages > 0) {
            List<Integer> pages = IntStream.rangeClosed(1, totalPages).boxed().collect(Collectors.toList());
            if (page > pages.size() - 1) {
                attr.addFlashAttribute("msgPagina", "No se encuentran datos en esa página");

                return "redirect:/categoria";
            }
            model.addAttribute("pages", pages);

        } else {
            attr.addFlashAttribute("msgPagina", "No se encuentran datos en esa página");

            return "redirect:/categoria";
        }
            List<Categoria> listacategoria = pageCat.getContent();

            model.addAttribute("totalItems", totalItems);
            model.addAttribute("listacategoria", listacategoria);
            model.addAttribute("current", page + 1);
            model.addAttribute("next", page + 2);
            model.addAttribute("prev", page);
            model.addAttribute("last", totalPages);
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
            String nom= categoria.getNombrecategoria().substring(0, 1).toUpperCase() + categoria.getNombrecategoria().substring(1).toLowerCase();
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
                                   @RequestParam("id") String id,RedirectAttributes att){
        try{
            int idcat = Integer.parseInt(id);
            Optional<Categoria> optCategory = categoriaRepository.findById(idcat);
            Inventarioproducto inventarioproducto = inventarioproductoRepository.verificaCategoriaEnInventario(idcat);
            if (inventarioproducto == null){
                if (optCategory.isPresent()) {
                    categoriaRepository.deleteById(idcat);
                    att.addFlashAttribute("msgBorradoExito", "Borrado Exitosamente");
                    return "redirect:/categoria";
                }
            }else {
                att.addFlashAttribute("msgBorrado", "Categoria se encuentra en el inventario principal");
                return "redirect:/categoria";
            }

        }catch (NumberFormatException e){
            return "redirect:/categoria";
        }


        return "redirect:/categoria";
    }

}
