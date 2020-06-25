package com.example.demo.Controllers;

import com.example.demo.Entity.Consignacionyventa;
import com.example.demo.Entity.Inventarioproducto;
import com.example.demo.Entity.Producto;
import com.example.demo.Entity.Usuario;
import com.example.demo.Repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.zip.DataFormatException;

@Controller
@RequestMapping("/inventarioPrincipal")
public class InventarioproductoController {

    @Autowired
    LineaRepository lineaRepository;
    @Autowired
    ProductoRepository productoRepository;
    @Autowired
    CategoriaRepository categoriaRepository;
    @Autowired
    TamanoRepository tamanoRepository;
    @Autowired
    ArtesanoRepository artesanoRepository;
    @Autowired
    ConsignacionyventaRepository consignacionyventaRepository;
    @Autowired
    InventarioproductoRepository inventarioproductoRepository;



    @GetMapping(value = {"","/","/lista"})
    public String listaInventarioProducto(@RequestParam Map<String, Object> params, Model model, RedirectAttributes attr){


        try {
            int page = params.get("page") != null ? (Integer.valueOf(params.get("page").toString()) - 1) : 0;
        }catch (NumberFormatException e){
            return "redirect:/producto";
        }
        int page = params.get("page") != null ? (Integer.valueOf(params.get("page").toString()) - 1) : 0;

        if(page<0){
            return "redirect:/inventarioPrincipal";
        }

        PageRequest pageRequest = PageRequest.of(page, 10);

        Page<Inventarioproducto> pageProduct = inventarioproductoRepository.findAll(pageRequest);

        int totalPage = pageProduct.getTotalPages();
        if (totalPage > 0) {
            List<Integer> pages = IntStream.rangeClosed(1, totalPage).boxed().collect(Collectors.toList());
            if (page > pages.size() -1){
                attr.addFlashAttribute("msgPagina", "No se encuentran datos en esa página");

                return "redirect:/inventarioPrincipal";
            }
            model.addAttribute("pages", pages);
        }else{

            return "redirect:/inventarioPrincipal";
        }

        model.addAttribute("listaInventarioPrincipal", pageProduct.getContent());
        model.addAttribute("current", page + 1);
        model.addAttribute("next", page + 2);
        model.addAttribute("prev", page);
        model.addAttribute("last", totalPage);

        return "inventario/inventarioPrincipal";
    }

    @GetMapping("/agregarInventario")
    public String consignacionYVenta(@ModelAttribute("consigYVenta") Consignacionyventa consigYventa, Model model){
        model.addAttribute("listaArtesano",artesanoRepository.findAll());
        return "inventario/consigYventa";
    }

    @PostMapping("/agregarConsigVenta")
    public String ingresarConsignacionOventa(Model model,@ModelAttribute("inventarioProducto") Inventarioproducto invPro,
                                               @ModelAttribute("consigYVenta") Consignacionyventa consigYventa){
     consignacionyventaRepository.save(consigYventa);

       return "redirect:/inventarioPrincipal/sgteProductos";
    }

    @GetMapping("/sgteProductos")
    public String vistaagregarproductos(Model model, @ModelAttribute("inventarioProducto") Inventarioproducto invPro,
                                        @ModelAttribute("consigYVenta") Consignacionyventa consigYventa){
        Optional<Consignacionyventa> ultimaConsigOventa = consignacionyventaRepository.findById(consignacionyventaRepository.ultimoConsiyVentaIngresado());

        model.addAttribute("listalinea",lineaRepository.findAll());
        model.addAttribute("listaproducto",productoRepository.findAll());model.addAttribute("listacategoria",categoriaRepository.findAll());
        model.addAttribute("listatamano",tamanoRepository.findAll());
      model.addAttribute("consigYventa1",ultimaConsigOventa.get());
        return "inventario/inventarioProducto";
    }

    @PostMapping("/agregarProducto")
    public String agregarProductosEnPedido(Model model, @ModelAttribute("inventarioProducto") Inventarioproducto invPro,
                                           @ModelAttribute("consigYVenta") Consignacionyventa consigYventa){
        Consignacionyventa ultimaConsigOventa = consignacionyventaRepository.findTopByOrderByIdconsignacionDesc();
        invPro.setConsignacionyventa(ultimaConsigOventa);


        //Date fechatudei = new Date();
        /*
       invPro.setFechainicio(fechatudei);
        if(ultimaConsigOventa.get().getTipo().equals("consignacion")){
            StringBuilder appe = new StringBuilder().append(invPro.getColor()).append(invPro.getCategoria());
            invPro.setCodigogenerado(appe.toString());
        }else{
            StringBuilder appe1 = new StringBuilder().append(invPro.getColor()).append(invPro.getCategoria()).append(invPro.getFacilitador());
            invPro.setCodigogenerado(appe1.toString());
        }
        */

        invPro.setCodigogenerado("cualquierhuevada");
        inventarioproductoRepository.save(invPro);
        return "redirect:/inventarioPrincipal/sgteProductos";

    }

    @PostMapping("/confirmarPedido")
    public String confirmacionPedidos(Model model, @ModelAttribute("inventarioProducto") Inventarioproducto invPro,
                                      @ModelAttribute("consigYVenta") Consignacionyventa consigYventa){
        return "inventario/confirmarpedido";
    }

  /*   //@GetMapping("/buscador")
    public String buscadorSearch(@RequestParam Map<String, Object> params, Model model, RedirectAttributes att, @ModelAttribute("searchField") String textbuscador,RedirectAttributes attr) {
        if (textbuscador.isEmpty()) {
            att.addFlashAttribute("msgBuscador", "Campo vacio. Ingrese el dato a buscar");

            return "redirect:/inventarioPrincipal";
        } else {

            try {
                int page = params.get("page") != null ? (Integer.valueOf(params.get("page").toString()) - 1) : 0;
            } catch (NumberFormatException e) {
                return "redirect:/inventarioPrincipal";
            }
            int page = params.get("page") != null ? (Integer.valueOf(params.get("page").toString()) - 1) : 0;

            if (page < 0) {
                return "redirect:/inventarioPrincipal";
            }


            Page<Usuario> pageUsuario1 = usuarioService.buscador(textbuscador, page);
            int totalPage = pageUsuario1.getTotalPages();
            long totalItems = pageUsuario1.getTotalElements();
            if (totalPage > 0) {
                List<Integer> pages = IntStream.rangeClosed(1, totalPage).boxed().collect(Collectors.toList());
                if (page > pages.size() -1){
                    attr.addFlashAttribute("msgPagina", "No se encuentran datos en esa página");

                    return "redirect:/usuario/lista";
                }


                model.addAttribute("page", pages);
            }else{
                attr.addFlashAttribute("msgPagina", "No se encuentran datos en esa página");

                return "redirect:/usuario/lista";
            }
            model.addAttribute("listaUsuarios", pageUsuario1.getContent());
            model.addAttribute("totalItems", totalItems);

            model.addAttribute("current", page + 1);
            model.addAttribute("next", page + 2);
            model.addAttribute("prev", page);
            model.addAttribute("last", totalPage);
            model.addAttribute("searchField", textbuscador);
            String listaactivos = "falsooo";
            model.addAttribute("listaactivos", listaactivos);

            //model.addAttribute("listaUsuarios", usuarioRepository.findAll());
            return "Usuario/lista";
        }

    }

 */








}
