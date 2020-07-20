package com.example.demo.Controllers;

import com.example.demo.Entity.Inventariosede;
import com.example.demo.Entity.Tienda;
import com.example.demo.Entity.Usuario;
import com.example.demo.Repository.TiendaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequestMapping("tienda")
public class TiendaController {

    @Autowired
    TiendaRepository tiendaRepository;

    @GetMapping(value = {"lista", "","/"})
    public String listar (Model model, @RequestParam Map<String, Object> params,HttpSession session , RedirectAttributes attr){

        Usuario user = (Usuario) session.getAttribute("usuario");
        try {
            int page = params.get("page") != null ? (Integer.valueOf(params.get("page").toString()) - 1) : 0;
        } catch (NumberFormatException e) {
            return "redirect:/tienda";
        }
        int page = params.get("page") != null ? (Integer.valueOf(params.get("page").toString()) - 1) : 0;

        if (page < 0) {
            return "redirect:/tienda";
        }

        PageRequest pageRequest = PageRequest.of(page, 5);

        Page<Tienda> pageTienda = tiendaRepository.listaTiendasPorSedePaginado(user.getSede_idsede().getIdsede(), pageRequest) ;

        int totalPage = pageTienda.getTotalPages();
        if (totalPage > 0) {
            List<Integer> pages = IntStream.rangeClosed(1, totalPage).boxed().collect(Collectors.toList());
            if (page > pages.size() - 1) {
                attr.addFlashAttribute("msgPagina", "No se encuentran datos en esa página");

                return "redirect:/tienda";
            }
            model.addAttribute("pages", pages);
        } else {

            return "redirect:/tienda";
        }

        model.addAttribute("lista", pageTienda.getContent());
        model.addAttribute("current", page + 1);
        model.addAttribute("next", page + 2);
        model.addAttribute("prev", page);
        model.addAttribute("last", totalPage);
        return "Tienda/lista";



    }

    @GetMapping("nuevo")
    public String nuevo(@ModelAttribute("tienda") Tienda tienda){

        return "Tienda/newEdit";
    }

    @PostMapping("/guardar")
    public String guardar(@ModelAttribute("tienda") Tienda tienda,
                          Model model ,  HttpSession session ){

        Usuario usuario = (Usuario) session.getAttribute("usuario");
        tienda.setSede(usuario.getSede_idsede());
        tiendaRepository.save(tienda);

        return "redirect:/tienda";
    }

    @GetMapping("editar")
    public String editar(@ModelAttribute("tienda") Tienda tienda,
                         Model model,
                         @RequestParam("id") int id){

        Optional<Tienda> opt = tiendaRepository.findById(id);

        if (opt.isPresent()){
            tienda= opt.get();
            model.addAttribute("tienda", tienda);
            return "Tienda/newEdit";

        }else {
            return "redirect:/categoria/lista";
        }

    }


    @GetMapping("/borrar")
    public String borrar (@RequestParam("id") int id){

        Optional<Tienda> opt = tiendaRepository.findById(id);

        if (opt.isPresent()){
            tiendaRepository.deleteById(id);
        }
        return "redirect:/tienda";
    }




}

