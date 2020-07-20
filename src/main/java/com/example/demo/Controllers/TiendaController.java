package com.example.demo.Controllers;

import com.example.demo.Entity.*;
import com.example.demo.Repository.TiendaRepository;
import com.example.demo.Repository.VentaRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
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
    @Autowired
    VentaRepository ventaRepository;

    @GetMapping(value = {"lista", "","/"})
    public String listar (Model model, @RequestParam Map<String, Object> params,HttpSession session , RedirectAttributes attr){

        Usuario user = (Usuario) session.getAttribute("usuario");
        try {
            int page = params.get("page") != null ? (Integer.valueOf(params.get("page").toString()) - 1) : 0;
        } catch (NumberFormatException e) {
            return "redirect:/tienda/lista";
        }
        int page = params.get("page") != null ? (Integer.valueOf(params.get("page").toString()) - 1) : 0;

        if (page < 0) {
            return "redirect:/tienda/lista";
        }

        PageRequest pageRequest = PageRequest.of(page, 5);

        Page<Tienda> pageTienda = tiendaRepository.listaTiendasPorSedePaginado(user.getSede_idsede().getIdsede(), pageRequest) ;

        int totalPage = pageTienda.getTotalPages();
        if (totalPage > 0) {
            List<Integer> pages = IntStream.rangeClosed(1, totalPage).boxed().collect(Collectors.toList());
            if (page > pages.size() - 1) {
                attr.addFlashAttribute("msgPagina", "No se encuentran datos en esa página");

                return "redirect:/tienda/lista";
            }
            model.addAttribute("pages", pages);
        } else if (totalPage == 0) {
            model.addAttribute("lista", pageTienda.getContent());
            model.addAttribute("current", page + 1);
            model.addAttribute("next", page + 2);
            model.addAttribute("prev", page);
            model.addAttribute("last", totalPage);
            return "Tienda/lista";
        }else {

            return "redirect:/tienda/lista";
        }

        model.addAttribute("lista", pageTienda.getContent());
        model.addAttribute("current", page + 1);
        model.addAttribute("next", page + 2);
        model.addAttribute("prev", page);
        model.addAttribute("last", totalPage);
        model.addAttribute("totalItems", pageTienda.getTotalElements());
        return "Tienda/lista";



    }

    @GetMapping("nuevo")
    public String nuevo(@ModelAttribute("tienda") Tienda tienda, Model model,RedirectAttributes redirectAttributes){

        return "Tienda/newEdit";
    }

    @PostMapping("/guardar")
    public String guardar(@ModelAttribute("tienda") @Valid Tienda tienda, BindingResult bindingResult,
                          RedirectAttributes redirectAttributes, Model model , HttpSession session ) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        tienda.setNombre(tienda.getNombre().trim());

        if (bindingResult.hasErrors()) {
            model.addAttribute("msg2", "Ingrese todos los datos solicitados correctamente.");
            return "Tienda/newEdit";
        }
        tienda.setSede(usuario.getSede_idsede());
        if (tienda.getIdtienda() == 0) {
            Tienda existeTiendaSede = tiendaRepository.verificaidTienda(tienda.getNombre(), tienda.getSede().getIdsede());
            if (existeTiendaSede == null) {
                redirectAttributes.addFlashAttribute("msg", "Tienda creada exitosamente.");
            } else {
                model.addAttribute("msg2", "Tienda con nombre existente para la localidad.");
                redirectAttributes.addFlashAttribute("tienda", tienda);
                return "Tienda/newEdit";
            }
        } else {
            for (Tienda tiendaAux : tiendaRepository.buscarmenosmio(tienda.getIdtienda())) {
                if (tiendaAux.getNombre().equalsIgnoreCase(tienda.getNombre())) {
                    model.addAttribute("msg2", "Tienda con nombre existente para la localidad.");
                    redirectAttributes.addFlashAttribute("tienda", tienda);
                    return "Tienda/newEdit";
                } else if (tienda.getIdtienda() == 0) {
                    redirectAttributes.addFlashAttribute("msg", "Producto Creado Exitosamente");
                } else {
                    redirectAttributes.addFlashAttribute("msg", "Producto Actualizado Exitosamente");
                }
            }

        }

        tiendaRepository.save(tienda);
        return "redirect:/tienda";

    }

    @GetMapping("editar")
    public String editar(@ModelAttribute("tienda") Tienda tienda,
                         Model model,
                         @RequestParam("id") String id){

        try {
            int id2 = Integer.parseInt(id);
            Optional<Tienda> opt = tiendaRepository.findById(id2);

            if (opt.isPresent()){
                tienda= opt.get();
                model.addAttribute("tienda", tienda);
                return "Tienda/newEdit";

            }else {
                return "redirect:/tienda";
            }
        }catch (NumberFormatException e){
            return "redirect:/tienda";
        }

    }


    @GetMapping("/borrar")
    public String borrar (@RequestParam("id") String id, RedirectAttributes attr){
        try {

            int id1 = Integer.parseInt(id);
            Optional<Tienda> opt = tiendaRepository.findById(id1);
            Ventas verificaidTiendaenVentas = ventaRepository.verificaidTiendaenVentas(id1);
            if (verificaidTiendaenVentas == null) {
                if (opt.isPresent()) {
                    tiendaRepository.deleteById(id1);
                    attr.addFlashAttribute("msg", "Tienda borrada exitosamente");
                    return "redirect:/tienda";
                }
            }else {
                attr.addFlashAttribute("msg2","No se puede borrar, tiene Ventas realizadas");
                return "redirect:/tienda";
        }

        }catch (NumberFormatException e){
            return "redirect:/tienda";
        }
        return "redirect:/tienda";
    }
}