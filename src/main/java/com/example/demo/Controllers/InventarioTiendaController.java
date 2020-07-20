package com.example.demo.Controllers;


import com.example.demo.Entity.*;
import com.example.demo.Repository.*;
import com.example.demo.service.InventarioTiendaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
import java.util.Optional;
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
    @Autowired
    InventarioTiendaService inventarioTiendaService;
    @Autowired
    InventarioproductoRepository inventarioproductoRepository;


    @GetMapping(value = {"", "/", "/lista"})
    public String listaInventarioTienda(Model model, @RequestParam Map<String, Object> params, RedirectAttributes attr, HttpSession session) {

        Usuario user = (Usuario) session.getAttribute("usuario");

        try {
            int page = params.get("page") != null ? (Integer.valueOf(params.get("page").toString()) - 1) : 0;
        } catch (NumberFormatException e) {
            return "redirect:/inventarioTienda/lista";
        }


        int page = params.get("page") != null ? (Integer.valueOf(params.get("page").toString()) - 1) : 0;
        if(page<0){
            return "redirect:/inventarioTienda/lista";
        }
        Page<Inventariotienda> pageInvTienda = inventarioTiendaService.listaTiendasPorSede(user.getSede_idsede().getIdsede(), page);

        int totalPage = pageInvTienda.getTotalPages();

        if (totalPage > 0) {
            List<Integer> pages = IntStream.rangeClosed(1, totalPage).boxed().collect(Collectors.toList());
            if (page > pages.size() - 1) {
                attr.addFlashAttribute("msgPagina", "No se encuentran datos en esa página");

                return "redirect:/inventarioTienda/lista";
            }
            model.addAttribute("pages", pages);
        } else {
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

    @GetMapping(value = {"/listaTotal"})
    public String listaInventarioTiendaTotal(Model model, @RequestParam Map<String, Object> params, RedirectAttributes attr, HttpSession session) {

        Usuario user = (Usuario) session.getAttribute("usuario");

        try {
            int page = params.get("page") != null ? (Integer.valueOf(params.get("page").toString()) - 1) : 0;
        } catch (NumberFormatException e) {
            return "redirect:/inventarioTienda/lista";
        }


        int page = params.get("page") != null ? (Integer.valueOf(params.get("page").toString()) - 1) : 0;

        if(page<0){
            return "redirect:/inventarioTienda/lista";
        }
        PageRequest pageRequest = PageRequest.of(page, 5);

        Page<Inventariotienda> pageInvTienda = inventarioTiendaRepository.findmenosDevuelto(pageRequest);
        int totalPage = pageInvTienda.getTotalPages();


        if (totalPage > 0) {
            List<Integer> pages = IntStream.rangeClosed(1, totalPage).boxed().collect(Collectors.toList());
            if (page > pages.size() - 1) {
                attr.addFlashAttribute("msgPagina", "No se encuentran datos en esa página");

                return "redirect:/inventarioTienda/lista";
            }
            model.addAttribute("pages", pages);
        } else {
            attr.addFlashAttribute("msgPagina", "No se encuentran datos en esa página");

            return "redirect:/inventarioTienda/lista";


        }

        model.addAttribute("listaInventarioTienda", pageInvTienda.getContent());
        model.addAttribute("current", page + 1);
        model.addAttribute("next", page + 2);
        model.addAttribute("prev", page);
        model.addAttribute("last", totalPage);

        return "inventario/listaTotal";
    }


    @GetMapping("/asignarStock")
    public String asignarStock(Model model, @ModelAttribute("tienda") Tienda tienda,
                               @ModelAttribute("inventariotienda") Inventariotienda inventariotienda, HttpSession session) {

        Usuario usuario = (Usuario) session.getAttribute("usuario");
        //model.addAttribute("inventario", inventarioTiendaRepository.listaProductoEnTienda(idTienda));
        model.addAttribute("inventario", inventarioSedeRepository.listarInventarioPorSedeConStock(usuario.getSede_idsede().getIdsede()));
        model.addAttribute("tiendas", tiendaRepository.listaTiendasPorSede(usuario.getSede_idsede().getIdsede()));
        return "Tienda/asignarStock";
    }

    @PostMapping("/agregarStock")
    public String agregarStock(Model model, @ModelAttribute("inventariotienda") Inventariotienda inventariotienda, @ModelAttribute("tienda") Tienda tienda, RedirectAttributes att) {

        int idTienda = inventariotienda.getTienda().getIdtienda();
        int invSedeParaTienda = inventariotienda.getInventariosede().getIdiventariosede();
        Inventariotienda inventariotiendaExiste = inventarioTiendaRepository.productoEnTienda(idTienda, invSedeParaTienda);

        if (inventariotienda.getStocktienda() <= inventariotienda.getInventariosede().getStock()) {
            if (inventariotiendaExiste == null) {
                int reducirCantidadenSede = inventariotienda.getStocktienda();
                Optional<Inventariosede> invSedeReducirCantidad = inventarioSedeRepository.findById(inventariotienda.getInventariosede().getIdiventariosede());
                int cantidadProducto = invSedeReducirCantidad.get().getStock();
                int nuevoTotalCantidad = cantidadProducto - reducirCantidadenSede;
                inventarioSedeRepository.actualizarStockSede(nuevoTotalCantidad, invSedeReducirCantidad.get().getIdiventariosede());
                inventariotienda.setEstado("recibido");
                inventarioTiendaRepository.save(inventariotienda);
                att.addFlashAttribute("msg", "Producto enviado a tienda Exitosamente");

            } else {
                int aumentarCantidadentienda = inventariotienda.getStocktienda();
                int cantidadAntesEnTienda = inventariotiendaExiste.getStocktienda();
                int nuevoTotal = aumentarCantidadentienda + cantidadAntesEnTienda;
                inventarioTiendaRepository.ActualizarCantidadInventarioTienda(nuevoTotal, inventariotiendaExiste.getIdiventariotienda());
                Optional<Inventariosede> invSedeReduceCantidad = inventarioSedeRepository.findById(inventariotiendaExiste.getInventariosede().getIdiventariosede());
                int nuevaStockEnsede = invSedeReduceCantidad.get().getStock() - aumentarCantidadentienda;
                inventarioSedeRepository.actualizarStockSede(nuevaStockEnsede, inventariotiendaExiste.getInventariosede().getIdiventariosede());
                att.addFlashAttribute("msg", "Se sumo Producto a tienda Exitosamente");
            }
        } else {
            att.addFlashAttribute("msgAlerta", "La cantidad ingresada debe ser menor a la de la Cantidad Disponible");
            return "redirect:/inventarioTienda/asignarStock";
        }
        return "redirect:/inventarioTienda/asignarStock";
    }

    @GetMapping("/buscador")
    public String buscadorSearch(@RequestParam Map<String, Object> params, Model model, RedirectAttributes attr, HttpSession session) {
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
            if(page<0){
                return "redirect:/inventarioTienda/lista";
            }
            PageRequest pageRequest = PageRequest.of(page, 10);
            Usuario usuarioses = (Usuario) session.getAttribute("usuario");

            Page<Inventariotienda> pageInvTienda = inventarioTiendaRepository.buscadorInventarioTienda(busqueda, usuarioses.getSede_idsede().getNombre(), pageRequest);
            int totalPage = pageInvTienda.getTotalPages();
            if (totalPage > 0) {
                List<Integer> pages = IntStream.rangeClosed(1, totalPage).boxed().collect(Collectors.toList());
                if (page > pages.size() - 1) {
                    attr.addFlashAttribute("msgPagina", "No se encuentran datos en esa página");

                    return "redirect:/inventarioTienda/lista";
                }
                model.addAttribute("pages", pages);
            } else {
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
    @GetMapping("/buscadortotal")
    public String buscadortiendatotal(@RequestParam Map<String, Object> params, Model model, RedirectAttributes attr, HttpSession session) {
        String busqueda = (String) params.get("searchField");
        if (busqueda.isEmpty()) {
            attr.addFlashAttribute("msgBuscador", "Campo vacio. Ingrese el dato a buscar");

            return "redirect:/inventarioTienda/listaTotal";
        } else {

            try {
                int page = params.get("page") != null ? (Integer.valueOf(params.get("page").toString()) - 1) : 0;
            } catch (NumberFormatException e) {
                return "redirect:/inventarioTienda/listaTotal";
            }


            int page = params.get("page") != null ? (Integer.valueOf(params.get("page").toString()) - 1) : 0;
            if(page<0){
                return "redirect:/inventarioTienda/listaTotal";
            }
            PageRequest pageRequest = PageRequest.of(page, 10);
            Usuario usuarioses = (Usuario) session.getAttribute("usuario");

            Page<Inventariotienda> pageInvTienda = inventarioTiendaRepository.buscadorInventarioTiendaTotal(busqueda, pageRequest);
            int totalPage = pageInvTienda.getTotalPages();
            if (totalPage > 0) {
                List<Integer> pages = IntStream.rangeClosed(1, totalPage).boxed().collect(Collectors.toList());
                if (page > pages.size() - 1) {
                    attr.addFlashAttribute("msgPagina", "No se encuentran datos en esa página");

                    return "redirect:/inventarioTienda/listaTotal";
                }
                model.addAttribute("pages", pages);
            } else {
                attr.addFlashAttribute("msgPagina", "No se encuentran datos en esa página");

                return "redirect:/inventarioTienda/listaTotal";


            }

            model.addAttribute("listaInventarioTienda", pageInvTienda.getContent());
            model.addAttribute("current", page + 1);
            model.addAttribute("next", page + 2);
            model.addAttribute("busqueda", busqueda);
            model.addAttribute("prev", page);
            model.addAttribute("last", totalPage);

            return "inventario/listaTotal";
        }
    }


    @GetMapping("/devolver")
    public String devolverProductoASede(@RequestParam("id") int id, RedirectAttributes attributes) {
        Optional<Inventariotienda> byId = inventarioTiendaRepository.findById(id);
        if (byId.isPresent()) {
            int StockEnTienda = byId.get().getStocktienda();
            int idInvenSede = byId.get().getInventariosede().getIdiventariosede();
            Optional<Inventariosede> AgregarStockInvSede = inventarioSedeRepository.findById(idInvenSede);
            int nuevoStockEnsede = StockEnTienda + AgregarStockInvSede.get().getStock();
            inventarioSedeRepository.actualizarStockSede(nuevoStockEnsede, AgregarStockInvSede.get().getIdiventariosede());
            //poner estado devuelto en tienda
            inventarioTiendaRepository.DevolverProductoASede(0, "devuelto", byId.get().getIdiventariotienda());
        } else {
            return "redirect:/inventarioTienda/lista";
        }

        attributes.addFlashAttribute("msg", "Producto devuelto Exitosamente");
        return "redirect:/inventarioTienda/lista";
    }


}



