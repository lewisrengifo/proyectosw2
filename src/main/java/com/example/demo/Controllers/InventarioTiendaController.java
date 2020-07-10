package com.example.demo.Controllers;


import com.example.demo.Entity.*;
import com.example.demo.Repository.InventarioSedeRepository;
import com.example.demo.Repository.InventarioTiendaRepository;
import com.example.demo.Repository.ProductoRepository;
import com.example.demo.Repository.TiendaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/inventarioTienda")
public class InventarioTiendaController {

    @Autowired
    InventarioTiendaRepository inventarioTiendaRepository;

    @Autowired
    InventarioSedeRepository inventarioSedeRepository;

    @Autowired
    TiendaRepository tiendaRepository;


    @GetMapping(value = {"","/","/lista"})
    public String listaInventarioTienda(Model model,@RequestParam Map<String, Object> params , RedirectAttributes attr) {



            try {
                int page = params.get("page") != null ? (Integer.valueOf(params.get("page").toString()) - 1) : 0;
            } catch (NumberFormatException e) {
                return "redirect:/inventarioTienda/lista";
            }


            int page = params.get("page") != null ? (Integer.valueOf(params.get("page").toString()) - 1) : 0;

            PageRequest pageRequest = PageRequest.of(page, 10);


            Page<Inventariotienda> pageInvTienda = inventarioTiendaRepository.findAll(pageRequest);
            int totalPage = pageInvTienda.getTotalPages();
            if (totalPage > 0) {
                List<Integer> pages = IntStream.rangeClosed(1, totalPage).boxed().collect(Collectors.toList());
                if (page > pages.size() - 1) {
                    attr.addFlashAttribute("msgPagina", "No se encuentran datos en esa página");

                    return "redirect:/inventarioTienda/lista";
                }
                model.addAttribute("pages", pages);
            }else{
                attr.addFlashAttribute("msgPagina", "No se encuentran datos en esa página");

                return "redirect:/inventarioTienda/lista";



            }

            model.addAttribute("listaInventarioTienda", pageInvTienda.getContent());
            model.addAttribute("current", page + 1);
            model.addAttribute("next", page + 2);

            model.addAttribute("prev", page);
            model.addAttribute("last", totalPage);

            return "inventario/inventarioTienda";
        }



    @GetMapping("/asignarStock")
    public String asignarStock(Model model, @ModelAttribute("tienda") Tienda tienda,
                               @ModelAttribute("inventariotienda") Inventariotienda inventariotienda, HttpSession session) {

        Usuario usuario = (Usuario) session.getAttribute("usuario");
        model.addAttribute("inventario", inventarioSedeRepository.obtenerInvDeMiSedeNormal(usuario.getSede_idsede().getNombre()));
        model.addAttribute("listaTiendas", tiendaRepository.findAll());
        return "Tienda/asignarStock";
    }

    @PostMapping("/agregarStock")
    public String agregarStock(Model model, @ModelAttribute("inventariotienda") Inventariotienda inventariotienda, @ModelAttribute("tienda") Tienda tienda) {
        Inventariotienda invt = new Inventariotienda();

        invt = inventariotienda;
        inventariotienda.setEstado("Entregado");
        inventarioTiendaRepository.save(inventariotienda);

        return "redirect:/inventarioTienda/asignarStock";
    }

    @GetMapping("/buscador")
    public String buscadorSearch(@RequestParam Map<String, Object> params, Model model, RedirectAttributes attr) {
        String busqueda = (String) params.get("searchField");
        if (busqueda.isEmpty()) {
            attr.addFlashAttribute("msgBuscador", "Campo vacio. Ingrese el dato a buscar");

            return "redirect:/inventarioTienda/lista";
        } else {

            try {
                int page = params.get("page") != null ? (Integer.valueOf(params.get("page").toString()) - 1) : 0;
            } catch (NumberFormatException e) {
                return "redirect:/inventarioTienda/lista";
            }


            int page = params.get("page") != null ? (Integer.valueOf(params.get("page").toString()) - 1) : 0;

            PageRequest pageRequest = PageRequest.of(page, 10);


            Page<Inventariotienda> pageInvTienda = inventarioTiendaRepository.buscadorInventarioTienda(busqueda,pageRequest);
            int totalPage = pageInvTienda.getTotalPages();
            if (totalPage > 0) {
                List<Integer> pages = IntStream.rangeClosed(1, totalPage).boxed().collect(Collectors.toList());
                if (page > pages.size() - 1) {
                    attr.addFlashAttribute("msgPagina", "No se encuentran datos en esa página");

                    return "redirect:/inventarioTienda/lista";
                }
                model.addAttribute("pages", pages);
            }else{
                attr.addFlashAttribute("msgPagina", "No se encuentran datos en esa página");

                return "redirect:/inventarioTienda/lista";



            }

            model.addAttribute("listaInventarioTienda", pageInvTienda.getContent());
            model.addAttribute("current", page + 1);
            model.addAttribute("next", page + 2);
            model.addAttribute("busqueda", busqueda);
            model.addAttribute("prev", page);
            model.addAttribute("last", totalPage);

            return "inventario/inventarioTienda";
        }
    }


}



