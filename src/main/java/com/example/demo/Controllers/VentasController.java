package com.example.demo.Controllers;


import com.example.demo.Dto.ProductoServiceApi;
import com.example.demo.Entity.*;
import com.example.demo.Repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.example.demo.Entity.Producto;
import com.example.demo.Repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/venta")
public class VentasController {
    @Autowired
    InventarioSedeRepository inventarioSedeRepository;
    @Autowired
    ProductoRepository productoRepository;

    @Autowired
    InventarioproductoRepository inventarioproductoRepository;
    @Autowired
    VentaRepository ventaRepository;
    @Autowired
    TiendaRepository tiendaRepository;

    @GetMapping(value = {"/listaVentas", ""})
    public String listarVentas(Model model, HttpSession session, @RequestParam Map<String, Object> params , RedirectAttributes attr) {

        Usuario usuariologueado = (Usuario) session.getAttribute("usuario");
        try {
            int page = params.get("page") != null ? (Integer.valueOf(params.get("page").toString()) - 1) : 0;
        } catch (NumberFormatException e) {
            return "redirect:/venta";
        }
        int page = params.get("page") != null ? (Integer.valueOf(params.get("page").toString()) - 1) : 0;

        if (page < 0) {
            return "redirect:/venta";
        }

        PageRequest pageRequest = PageRequest.of(page, 5);

        Page<Ventas> pageVent = ventaRepository.listaVentasPorSedePageable(usuariologueado.getSede_idsede().getIdsede() , pageRequest);

        int totalPage = pageVent.getTotalPages();

        int totalElementos = (int) pageVent.getTotalElements();
        if (totalPage > 0) {
            List<Integer> pages = IntStream.rangeClosed(1, totalPage).boxed().collect(Collectors.toList());
            if (page > pages.size() - 1) {
                attr.addFlashAttribute("msgPagina", "No se encuentran datos en esa página");

                return "redirect:/venta";
            }
            model.addAttribute("pages", pages);
        } else {


            return "redirect:/venta";
        }

        model.addAttribute("listaVentas", pageVent.getContent());
        model.addAttribute("current", page + 1);
        model.addAttribute("next", page + 2);
        model.addAttribute("prev", page);
        model.addAttribute("last", totalPage);
        model.addAttribute("totalElementos", totalElementos);
        return "venta/listaventa";


    }

    @GetMapping("/registroventa")
    public String registrarVenta(Model model, @ModelAttribute("ventas") Ventas ventas,
                                 RedirectAttributes redirectAttributes, HttpSession session) {

        Usuario usuariologueado = (Usuario) session.getAttribute("usuario");

        int sedeUsuario = usuariologueado.getSede_idsede().getIdsede();
        model.addAttribute("inventarioSedeProducto", inventarioSedeRepository.listarInventarioPorSede(sedeUsuario));
        model.addAttribute("listaTiendas", tiendaRepository.listaTiendasPorSede(sedeUsuario));
        model.addAttribute("usuarioRol", usuariologueado.getRol_idrol().getNombre());
        model.addAttribute("idsede", usuariologueado.getSede_idsede().getIdsede());

        return "venta/registroventa";
    }


    @PostMapping("/agregarVenta")
    public String ingresarVentas(Model model, @ModelAttribute("ventas") @Valid Ventas ventas, BindingResult bindingResult,
                                 RedirectAttributes redirectAttributes, HttpSession session) {

        Usuario usuariologueado = (Usuario) session.getAttribute("usuario");
        if (bindingResult.hasErrors()) {
            int sedeUsuario = usuariologueado.getSede_idsede().getIdsede();
            model.addAttribute("inventarioSedeProducto", inventarioSedeRepository.listarInventarioPorSede(sedeUsuario));
            model.addAttribute("listaTiendas", tiendaRepository.listaTiendasPorSede(sedeUsuario));
            model.addAttribute("usuarioRol", usuariologueado.getRol_idrol().getNombre());
            model.addAttribute("idsede", usuariologueado.getSede_idsede().getIdsede());
            return "venta/registroventa";
        } else {
            if (ventas.getIdventas() == 0) {
                for (Ventas unoDeVentas : ventaRepository.findAll()) {
                    if (ventas.getNumerodocumento().equals(unoDeVentas.getNumerodocumento())) {
                        redirectAttributes.addFlashAttribute("msg1", "Documento de venta existente.");
                        redirectAttributes.addFlashAttribute("ventas", ventas);
                        int sedeUsuario = usuariologueado.getSede_idsede().getIdsede();
                        model.addAttribute("inventarioSedeProducto", inventarioSedeRepository.listarInventarioPorSede(sedeUsuario));
                        model.addAttribute("listaTiendas", tiendaRepository.listaTiendasPorSede(sedeUsuario));
                        model.addAttribute("usuarioRol", usuariologueado.getRol_idrol().getNombre());
                        model.addAttribute("idsede", usuariologueado.getSede_idsede().getIdsede());
                        return "venta/registroventa";
                    } else {
                        int cantidadDeStockEnsede = ventas.getInventariosede().getStock();
                        if (cantidadDeStockEnsede > ventas.getCantidad()) {
                            Optional<Inventariosede> idSedeCambiaStock = inventarioSedeRepository.findById(ventas.getInventariosede().getIdiventariosede());
                            int aa = cantidadDeStockEnsede;
                            int bb = ventas.getCantidad();
                            int StockNuevo = aa - bb;
                            Optional<Inventarioproducto> CambiaCantidadProducto =
                                    inventarioproductoRepository.findById(idSedeCambiaStock.get().getInventarioproductoidinventario().getIdinventario());
                            int nuevaCantidadProducto = CambiaCantidadProducto.get().getCantidad() - ventas.getCantidad();

                            //Actualizar cantidad en inventario sede
                            inventarioSedeRepository.actualizarStockSedeXVenta(StockNuevo, idSedeCambiaStock.get().getIdiventariosede());
                            //actualizar cantidad en inventario principal
                            inventarioproductoRepository.ActualizarCantidadInventarioPrincipal(nuevaCantidadProducto, CambiaCantidadProducto.get().getIdinventario());

                            ventaRepository.save(ventas);
                        } else {
                            return "redirect:/venta/registroventa";
                        }

                        //return "redirect:/venta";
                        model.addAttribute("listaVentas", ventaRepository.listaVentasPorSede(usuariologueado.getSede_idsede().getIdsede()));
                        redirectAttributes.addFlashAttribute("msg2", "Venta registrada exitosamente.");
                        ventaRepository.save(ventas);
                        return "redirect:/venta";
                    }
                }
            } else {
                for (Ventas unoDeVentas2 : ventaRepository.buscarmenosmio(ventas.getIdventas())) {
                    if (ventas.getNumerodocumento().equals(unoDeVentas2.getNumerodocumento())) {
                        redirectAttributes.addFlashAttribute("msg1", "Documento a modificar ya existe.");
                        redirectAttributes.addFlashAttribute("ventas", ventas);
                        int sedeUsuario = usuariologueado.getSede_idsede().getIdsede();
                        model.addAttribute("inventarioSedeProducto", inventarioSedeRepository.listarInventarioPorSede(sedeUsuario));
                        model.addAttribute("listaTiendas", tiendaRepository.listaTiendasPorSede(sedeUsuario));
                        model.addAttribute("usuarioRol", usuariologueado.getRol_idrol().getNombre());
                        model.addAttribute("idsede", usuariologueado.getSede_idsede().getIdsede());
                        return "venta/registoventa";
                    } else {
                        model.addAttribute("listaVentas", ventaRepository.listaVentasPorSede(usuariologueado.getSede_idsede().getIdsede()));
                        redirectAttributes.addFlashAttribute("msg2", "Venta actualizada exitosamente.");
                        ventaRepository.save(ventas);
                        return "redirect:/venta";
                    }
                }
            }
            model.addAttribute("listaVentas", ventaRepository.listaVentasPorSede(usuariologueado.getSede_idsede().getIdsede()));
            return "redirect:/venta";

        }

    }

    @GetMapping("/buscador")
    public String buscadorSearch(@RequestParam Map<String, Object> params, HttpSession session ,Model model , RedirectAttributes attr ) {

        Usuario usuariologueado = (Usuario) session.getAttribute("usuario");

        String busqueda = (String) params.get("searchField");
        if (busqueda.isEmpty()) {
            attr.addFlashAttribute("msgBuscador", "Campo vacio. Ingrese el dato a buscar");

            return "redirect:/venta/lista";
        } else {

            try {
                int page = params.get("page") != null ? (Integer.valueOf(params.get("page").toString()) - 1) : 0;
            } catch (NumberFormatException e) {
                return "redirect:/venta/lista";
            }


            int page = params.get("page") != null ? (Integer.valueOf(params.get("page").toString()) - 1) : 0;

            PageRequest pageRequest = PageRequest.of(page, 5);


            Page<Ventas> pageVent = ventaRepository.buscadorVentas(busqueda,usuariologueado.getSede_idsede().getNombre(), pageRequest );
            int totalPage = pageVent.getTotalPages();
            if (totalPage > 0) {
                List<Integer> pages = IntStream.rangeClosed(1, totalPage).boxed().collect(Collectors.toList());
                if (page > pages.size() - 1) {
                    attr.addFlashAttribute("msgPagina", "No se encuentran datos en esa página");

                    return "redirect:/venta/lista";
                }
                model.addAttribute("pages", pages);
            } else {
                attr.addFlashAttribute("msgPagina", "No se encuentran datos en esa página");

                return "redirect:/venta/lista";


            }

            model.addAttribute("listaVentas", pageVent.getContent());
            model.addAttribute("current", page + 1);
            model.addAttribute("next", page + 2);
            model.addAttribute("busqueda", busqueda);
            model.addAttribute("prev", page);
            model.addAttribute("last", totalPage);

            return "venta/listaventa";
        }
    }
}