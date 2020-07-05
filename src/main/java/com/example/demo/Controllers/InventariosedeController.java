package com.example.demo.Controllers;

import com.example.demo.Dto.ProductoServiceApi;
import com.example.demo.Entity.*;
import com.example.demo.Repository.InventarioSedeRepository;
import com.example.demo.Repository.InventarioproductoRepository;
import com.example.demo.Repository.SedeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/inventarioSede")
public class InventariosedeController {

    @Autowired
    ProductoServiceApi productoServiceApi;

    @Autowired
    InventarioproductoRepository inventarioproductoRepository;
    @Autowired
    SedeRepository sedeRepository;
    @Autowired
    InventarioSedeRepository inventarioSedeRepository;


    @GetMapping("/asignarStock")
    public String asignarStock(Model model, @ModelAttribute("sede") Sede sede,
                               @ModelAttribute("inventariosede") Inventariosede inventariosede) {

        model.addAttribute("inventario", inventarioproductoRepository.findAll());
        model.addAttribute("listaSede", sedeRepository.findAll());
        return "sede/asignarStock";
    }

    @PostMapping("/agregarStock")
    public String agregarStock(Model model, @ModelAttribute("inventariosede") Inventariosede inventariosede, @ModelAttribute("sede") Sede sede) {
        Inventariosede invs = new Inventariosede();

        invs = inventariosede;
        inventariosede.setEstado("Enviado");
        inventarioSedeRepository.save(inventariosede);

        return "redirect:/inventarioSede/asignarStock";
    }

    @GetMapping(value = {"", "/lista"})
    public String listaInventarioSede(@RequestParam Map<String, Object> params, Model model, RedirectAttributes attr, HttpSession session) {

        Usuario usuario = (Usuario) session.getAttribute("usuario");
        // String miSede =  usuario.getSede_idsede().getNombre();

        try {
            int page = params.get("page") != null ? (Integer.valueOf(params.get("page").toString()) - 1) : 0;
        } catch (NumberFormatException e) {
            return "redirect:/inventarioSede";
        }
        int page = params.get("page") != null ? (Integer.valueOf(params.get("page").toString()) - 1) : 0;

        if (page < 0) {
            return "redirect:/inventarioSede";
        }

        PageRequest pageRequest = PageRequest.of(page, 5);

        Page<Inventariosede> pageProduct = inventarioSedeRepository.findAll(pageRequest);

        int totalPage = pageProduct.getTotalPages();
        if (totalPage > 0) {
            List<Integer> pages = IntStream.rangeClosed(1, totalPage).boxed().collect(Collectors.toList());
            if (page > pages.size() - 1) {
                attr.addFlashAttribute("msgPagina", "No se encuentran datos en esa página");

                return "redirect:/inventarioSede";
            }
            model.addAttribute("pages", pages);
        } else {

            return "redirect:/inventarioSede";
        }

        model.addAttribute("listaInventarioSede", pageProduct.getContent());
        model.addAttribute("current", page + 1);
        model.addAttribute("next", page + 2);
        model.addAttribute("prev", page);
        model.addAttribute("last", totalPage);
        return "inventario/inventariosede";


    }

    @GetMapping(value = {"/listarInvMiSede"})
    public String listaInventarioMiSede(@RequestParam Map<String, Object> params, Model model, RedirectAttributes attr, HttpSession session) {

        Usuario usuario = (Usuario) session.getAttribute("usuario");
        String miSede = usuario.getSede_idsede().getNombre();

        try {
            int page = params.get("page") != null ? (Integer.valueOf(params.get("page").toString()) - 1) : 0;
        } catch (NumberFormatException e) {
            return "redirect:/inventarioSede/listarInvMiSede";
        }
        int page = params.get("page") != null ? (Integer.valueOf(params.get("page").toString()) - 1) : 0;

        if (page < 0) {
            return "redirect:/inventarioSede/listarInvMiSede";
        }

        PageRequest pageRequest = PageRequest.of(page, 10);

        Page<Inventariosede> pageProduct = productoServiceApi.getEverInvs(miSede, pageRequest);

        int totalPage = pageProduct.getTotalPages();
        if (totalPage > 0) {
            List<Integer> pages = IntStream.rangeClosed(1, totalPage).boxed().collect(Collectors.toList());
            if (page > pages.size() - 1) {
                attr.addFlashAttribute("msgPagina", "No se encuentran datos en esa página");

                return "redirect:/inventarioSede/listarInvMiSede";
            }
            model.addAttribute("pages", pages);
        } else {

            return "redirect:/inventarioSede/listarInvMiSede";
        }

        model.addAttribute("listaInventarioSede", pageProduct.getContent());
        model.addAttribute("current", page + 1);
        model.addAttribute("next", page + 2);
        model.addAttribute("prev", page);
        model.addAttribute("last", totalPage);
        return "inventario/inventariomisede";

    }
    @GetMapping("/actualizarEstado")
    public String actualizarEstado(@Param("estado") String estado, @Param("idinventariosede") int idinventariosede){
        inventarioSedeRepository.actualizarEstado(estado, idinventariosede);
        return "redirect:/inventarioSede/listarInvMiSede";
    }
    @GetMapping("/actualizarObservaciones")
    public String actualizarObservaciones(@Param("observaciones") String observaciones, @Param("idinventariosede") int idinventariosede){
        inventarioSedeRepository.actualizarObservaciones(observaciones, idinventariosede);
        return "redirect:/inventarioSede/listarInvMiSede";
    }






}
