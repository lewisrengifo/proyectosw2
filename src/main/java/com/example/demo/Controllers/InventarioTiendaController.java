package com.example.demo.Controllers;


import com.example.demo.Entity.*;
import com.example.demo.Repository.InventarioSedeRepository;
import com.example.demo.Repository.InventarioTiendaRepository;
import com.example.demo.Repository.ProductoRepository;
import com.example.demo.Repository.TiendaRepository;
import com.example.demo.service.InventarioTiendaService;
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

    @Autowired
    InventarioTiendaService inventarioTiendaService;


    @GetMapping(value = {"","/","/lista"})
    public String listaInventarioTienda(Model model,@RequestParam Map<String, Object> params, HttpSession session){

        Usuario user = (Usuario) session.getAttribute("usuario");
        int page = params.get("page") != null ? (Integer.valueOf(params.get("page").toString()) - 1) : 0;

        Page<Inventariotienda> pageInvTienda = inventarioTiendaService.listaInventarioTiendaMiSede(user.getSede_idsede().getIdsede(),page);
        int totalPage = pageInvTienda.getTotalPages();
        if (totalPage > 0) {
            List<Integer> pages = IntStream.rangeClosed(1, totalPage).boxed().collect(Collectors.toList());
            model.addAttribute("pages", pages);
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
        model.addAttribute("listaTiendas", tiendaRepository.listaTiendasPorSede(usuario.getSede_idsede().getIdsede()));
        return "Tienda/asignarStock";
    }

    @PostMapping("/agregarStock")
    public String agregarStock(Model model, @ModelAttribute("inventariotienda") Inventariotienda inventariotienda, @ModelAttribute("tienda") Tienda tienda,HttpSession session, RedirectAttributes attr) {
        Usuario user = (Usuario) session.getAttribute("usuario");
        Inventariotienda invt = new Inventariotienda();
        invt = inventariotienda;
        inventariotienda.setEstado("Entregado");
        int cantidadParaTienda = inventariotienda.getStocktienda();
        int buscarIdInventarioPrincipal = inventariotienda.getInventariosede().getInventarioproductoidinventario().getIdinventario();
        Inventariosede inventarioPrincipalProducto = inventarioSedeRepository.obtenerStockSedePrincipal(user.getSede_idsede().getIdsede(), buscarIdInventarioPrincipal);
        int cantidadProductostock = inventarioPrincipalProducto.getStock();

        if(cantidadProductostock>cantidadParaTienda){
            int stockActualPrincipal = cantidadProductostock-cantidadParaTienda;
            int sedePrincipal = user.getSede_idsede().getIdsede();
            Inventariotienda inventarioTiendaCambia = inventarioTiendaRepository.ObtenerInventariParacambiarStockParaTienda(inventariotienda.getInventariosede().getIdiventariosede(), inventariotienda.getTienda().getIdtienda());
            if (inventarioTiendaCambia==null) {
                inventarioSedeRepository.actualizarStockSede(stockActualPrincipal,inventarioPrincipalProducto.getIdiventariosede());
                inventarioTiendaRepository.save(inventariotienda);
            }else{
                int nuevoTotal = inventarioTiendaCambia.getStocktienda() + cantidadParaTienda;
                //actuliza el producto de la tienda
                inventarioSedeRepository.actualizarStockTienda(nuevoTotal,inventarioTiendaCambia.getIdiventariotienda());
                //actuliza el producto en el principal
                inventarioSedeRepository.actualizarStockSede(stockActualPrincipal,inventarioPrincipalProducto.getIdiventariosede());
                attr.addFlashAttribute("msgRepetido", "Se mando producto a sede exitosamente");
            }
        }else{
            attr.addFlashAttribute("msgRepetido", "La Cantidad asignada excede la del producto");
            return "redirect:/inventarioTienda/asignarStock";
        }


        return "redirect:/inventarioTienda/asignarStock";
    }

}



