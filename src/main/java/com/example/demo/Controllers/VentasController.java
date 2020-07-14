package com.example.demo.Controllers;


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

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.example.demo.Repository.ProductoRepository;
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
    InventarioTiendaRepository inventarioTiendaRepository;
    @Autowired
    InventarioproductoRepository inventarioproductoRepository;
    @Autowired
    VentaRepository ventaRepository;
    @Autowired
    TiendaRepository tiendaRepository;
    @Autowired
    SedeRepository sedeRepository;


    @GetMapping(value = {"/listaVentas", ""})
    public String listarVentas(Model model, HttpSession session, @RequestParam Map<String, Object> params, RedirectAttributes attr) {

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

        Page<Ventas> pageVent = ventaRepository.listaVentasPorSedePageable(usuariologueado.getSede_idsede().getIdsede(), pageRequest);

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

    @GetMapping("/buscador")
    public String buscadorSearch(@RequestParam Map<String, Object> params, HttpSession session, Model model, RedirectAttributes attr) {

        Usuario usuariologueado = (Usuario) session.getAttribute("usuario");

        String busqueda = (String) params.get("searchField");
        if (busqueda.isEmpty()) {
            attr.addFlashAttribute("msgBuscador", "Campo vacio. Ingrese el dato a buscar");

            return "redirect:/venta/listaVentas";
        } else {

            try {
                int page = params.get("page") != null ? (Integer.valueOf(params.get("page").toString()) - 1) : 0;
            } catch (NumberFormatException e) {
                return "redirect:/venta/listaVentas";
            }


            int page = params.get("page") != null ? (Integer.valueOf(params.get("page").toString()) - 1) : 0;

            PageRequest pageRequest = PageRequest.of(page, 5);


            Page<Ventas> pageVent = ventaRepository.buscadorVentas(busqueda, usuariologueado.getSede_idsede().getNombre(), pageRequest);
            int totalPage = pageVent.getTotalPages();
            if (totalPage > 0) {
                List<Integer> pages = IntStream.rangeClosed(1, totalPage).boxed().collect(Collectors.toList());
                if (page > pages.size() - 1) {
                    attr.addFlashAttribute("msgPagina", "No se encuentran datos en esa página");

                    return "redirect:/venta/listaVentas";
                }
                model.addAttribute("pages", pages);
            } else {
                attr.addFlashAttribute("msgPagina", "No se encuentran datos en esa página");

                return "redirect:/venta/listaVentas";


            }
            int totalElementos = (int) pageVent.getTotalElements();
            model.addAttribute("listaVentas", pageVent.getContent());
            model.addAttribute("current", page + 1);
            model.addAttribute("next", page + 2);
            model.addAttribute("busqueda", busqueda);
            model.addAttribute("prev", page);
            model.addAttribute("last", totalPage);
            model.addAttribute("totalElementos", totalElementos);

            return "venta/listaventa";
        }
    }


    @PostMapping("/agregarVenta")
    public String ingresarVentas(Model model, @ModelAttribute("ventas") @Valid Ventas ventas, BindingResult bindingResult,
                                 RedirectAttributes redirectAttributes, HttpSession session) {

        Usuario usuariologueado = (Usuario) session.getAttribute("usuario");
        Optional<Sede> sedeid = sedeRepository.findById(usuariologueado.getSede_idsede().getIdsede());
        ventas.setSede(sedeid.get());
        Optional<Tienda> byId = tiendaRepository.findById(ventas.getTienda().getIdtienda());
        if (bindingResult.hasErrors()) {
            model.addAttribute("ProductosEnTienda", inventarioTiendaRepository.listaProductoEnTienda(ventas.getTienda().getIdtienda()));
            ventas.setTienda(byId.get());
            model.addAttribute("tiendita", byId.get());
            return "venta/registroventa";
        } else {
            if (ventas.getIdventas() == 0) {
                //DATOS BIEN INGRESADOS
                Inventariotienda inventariotiendaReduceStock = inventarioTiendaRepository.productoEnTienda(ventas.getTienda().getIdtienda(), ventas.getInventariosede().getIdiventariosede());
                if (inventariotiendaReduceStock.getStocktienda() >= ventas.getCantidad()) {
                    Optional<Inventariosede> idSedeCambiaStock = inventarioSedeRepository.findById(ventas.getInventariosede().getIdiventariosede());

                    int aa = inventariotiendaReduceStock.getStocktienda();
                    int bb = ventas.getCantidad();
                    int StockNuevoEnTienda = aa - bb;
                    Optional<Inventarioproducto> CambiaCantidadProducto =
                            inventarioproductoRepository.findById(idSedeCambiaStock.get().getInventarioproductoidinventario().getIdinventario());
                    int nuevaCantidadProducto = CambiaCantidadProducto.get().getCantidad() - ventas.getCantidad();

                    //Actualizar cantidad en inventario Tienda
                    inventarioTiendaRepository.ActualizarCantidadInventarioTienda(StockNuevoEnTienda, inventariotiendaReduceStock.getIdiventariotienda());
                    //actualizar cantidad en inventario principal
                    inventarioproductoRepository.ActualizarCantidadInventarioPrincipal(nuevaCantidadProducto, CambiaCantidadProducto.get().getIdinventario());
                    redirectAttributes.addFlashAttribute("msgInfo", "Venta registrada exitosamente");
                    ventaRepository.save(ventas);
                    return "redirect:/venta";
                } else {
                    redirectAttributes.addFlashAttribute("msg1", "La cantidad ingresada debe menor a la Cantidad Disponible del Producto");
                    return "redirect:/venta/registroventa?id=" + ventas.getTienda().getIdtienda();
                }

            } else {
                Optional<Ventas> ventaVaACambiar = ventaRepository.findById(ventas.getIdventas());
                int cantidadViejaVenta = ventaVaACambiar.get().getCantidad();
                int cantidadNuevaVenta = ventas.getCantidad();
                Inventariotienda invenTiendaCambiaStock2 = inventarioTiendaRepository.productoEnTienda(ventas.getTienda().getIdtienda(), ventas.getInventariosede().getIdiventariosede());


                if (cantidadNuevaVenta > 0 && cantidadNuevaVenta <= invenTiendaCambiaStock2.getStocktienda()) {
                    if (ventaVaACambiar.get().getInventariosede().getIdiventariosede()
                            == ventas.getInventariosede().getIdiventariosede()) {
                        if (cantidadNuevaVenta == cantidadViejaVenta) {
                            ventaRepository.save(ventas);
                        } else {
                            Inventariotienda invenTiendaCambiaStock = inventarioTiendaRepository.productoEnTienda(ventas.getTienda().getIdtienda(), ventas.getInventariosede().getIdiventariosede());

                            if (cantidadNuevaVenta > cantidadViejaVenta) {
                                int diferencia = cantidadNuevaVenta - cantidadViejaVenta;
                                int nuevoStockEnTienda = invenTiendaCambiaStock.getStocktienda() - diferencia;
                                inventarioTiendaRepository.ActualizarCantidadInventarioTienda(nuevoStockEnTienda, invenTiendaCambiaStock.getIdiventariotienda());
                                Optional<Inventarioproducto> invPrinCambiaCantidad
                                        = inventarioproductoRepository.findById(ventas.getInventariosede().getInventarioproductoidinventario().getIdinventario());
                                int newCantInvPrin = invPrinCambiaCantidad.get().getCantidad() - diferencia;
                                inventarioproductoRepository.ActualizarCantidadInventarioPrincipal(newCantInvPrin, invPrinCambiaCantidad.get().getIdinventario());
                                ventaRepository.save(ventas);
                            } else {
                                int diferencia2 = cantidadViejaVenta - cantidadNuevaVenta;
                                int nuevoStockTienda2 = invenTiendaCambiaStock.getStocktienda() + diferencia2;
                                inventarioTiendaRepository.ActualizarCantidadInventarioTienda(nuevoStockTienda2, invenTiendaCambiaStock.getIdiventariotienda());
                                Optional<Inventarioproducto> invPrinCambiaCantidad2
                                        = inventarioproductoRepository.findById(ventas.getInventariosede().getInventarioproductoidinventario().getIdinventario());
                                int newCantInvPrin2 = invPrinCambiaCantidad2.get().getCantidad() + diferencia2;
                                inventarioproductoRepository.ActualizarCantidadInventarioPrincipal(newCantInvPrin2, invPrinCambiaCantidad2.get().getIdinventario());
                                ventaRepository.save(ventas);
                            }
                        }

                    } else {
                        //DARLE SU CANTIDAD A LA TIENDA
                        Inventariotienda invTiendaSumarCant =
                                inventarioTiendaRepository.productoEnTienda(ventas.getTienda().getIdtienda(), ventaVaACambiar.get().getInventariosede().getIdiventariosede());
                        int cantidadAnterVenta = ventaVaACambiar.get().getCantidad();
                        int NewTotalParatienda = cantidadAnterVenta + invTiendaSumarCant.getStocktienda();
                        inventarioTiendaRepository.ActualizarCantidadInventarioTienda(NewTotalParatienda, invTiendaSumarCant.getIdiventariotienda());
                        //DARLE SU CANTIDAD AL PRODUCTO DEL INVENTARIO
                        Optional<Inventarioproducto> invProductDevolverCant = inventarioproductoRepository.findById(ventaVaACambiar.get().getInventariosede().getInventarioproductoidinventario().getIdinventario());
                        int cantidadDevueltaTotal = invProductDevolverCant.get().getCantidad() + ventaVaACambiar.get().getCantidad();
                        inventarioproductoRepository.ActualizarCantidadInventarioPrincipal(cantidadDevueltaTotal, invProductDevolverCant.get().getIdinventario());

                        //CUANDO YA CAMBIASTE DE PRODUCTO
                        Inventariotienda inventariotiendaEditExiste = inventarioTiendaRepository.productoEnTienda(ventas.getTienda().getIdtienda(), ventas.getInventariosede().getIdiventariosede());
                        int cant = ventas.getCantidad();
                        Optional<Inventarioproducto> editProductoCantidad = inventarioproductoRepository.findById(ventas.getInventariosede().getInventarioproductoidinventario().getIdinventario());
                        int newCantInventProduct = editProductoCantidad.get().getCantidad() - cant;
                        inventarioproductoRepository.ActualizarCantidadInventarioPrincipal(newCantInventProduct, editProductoCantidad.get().getIdinventario());
                        if (inventariotiendaEditExiste.getIdiventariotienda() == 0) {
                            ventaRepository.save(ventas);
                        } else {
                            int cantTiendaEdit = inventariotiendaEditExiste.getStocktienda();
                            int newCantTienda = cantTiendaEdit - cant;
                            inventarioTiendaRepository.ActualizarCantidadInventarioTienda(newCantTienda, inventariotiendaEditExiste.getIdiventariotienda());
                            ventaRepository.save(ventas);
                        }
                    }
                } else {
                    redirectAttributes.addFlashAttribute("msg1", "La cantidad Ingresa debe ser Mayor a Cero");
                    return "redirect:/venta/editar?id=" + ventas.getIdventas();
                }

            }
            redirectAttributes.addFlashAttribute("msgInfo", "Venta actualizada exitosamente.");
            return "redirect:/venta";
        }

    }
}