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

import javax.jws.WebParam;
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
    @Autowired
    InventarioTiendaRepository inventarioTiendaRepository;
    @Autowired
    SedeRepository sedeRepository;

    @GetMapping(value = {"/listaVentas", ""})
    public String listarVentas(Model model, HttpSession session) {
        Usuario usuariologueado = (Usuario) session.getAttribute("usuario");
        model.addAttribute("listaVentas", ventaRepository.listaVentasPorSede(usuariologueado.getSede_idsede().getIdsede()));
        return "venta/listaventa";
    }

    @GetMapping("/registroventa")
    public String registrarVenta(Model model, @RequestParam("id") int idTienda,@ModelAttribute("ventas") Ventas ventas,
                                 RedirectAttributes att, HttpSession session) {

        Usuario usuariologueado = (Usuario) session.getAttribute("usuario");
        Optional<Tienda> byId = tiendaRepository.findById(idTienda);

        if(byId.isPresent()){
        int sedeUsuario = usuariologueado.getSede_idsede().getIdsede();
            List<Inventariotienda> inventariotiendas = inventarioTiendaRepository.listaProductoEnTienda(idTienda);
            if (inventariotiendas.isEmpty()){
                String nombreTienda = byId.get().getNombre();
                att.addFlashAttribute("msgError","La tienda" + nombreTienda + " no posee productos todavía");
                return "redirect:/tienda";
            }else{
                model.addAttribute("ProductosEnTienda", inventariotiendas);
                ventas.setTienda(byId.get());
                model.addAttribute("tiendita",byId.get());
                return "venta/registroventa";
            }

        }else{
            att.addFlashAttribute("msgError","Tienda seleccionada no Existe");
            return "redirect:/tienda";
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
            model.addAttribute("tiendita",byId.get());
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
                            redirectAttributes.addFlashAttribute("msg1","La cantidad ingresada debe menor a la Cantidad Disponible del Producto");
                            return "redirect:/venta/registroventa?id=" + ventas.getTienda().getIdtienda();
                        }

            } else {
                Optional<Ventas> ventaVaACambiar = ventaRepository.findById(ventas.getIdventas());
                int cantidadViejaVenta = ventaVaACambiar.get().getCantidad();
                int cantidadNuevaVenta = ventas.getCantidad();
                Inventariotienda invenTiendaCambiaStock2 = inventarioTiendaRepository.productoEnTienda(ventas.getTienda().getIdtienda(), ventas.getInventariosede().getIdiventariosede());


                if (cantidadNuevaVenta > 0 && cantidadNuevaVenta<=invenTiendaCambiaStock2.getStocktienda()) {
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
                            int newCantInvPrin = invPrinCambiaCantidad.get().getCantidad()-diferencia;
                            inventarioproductoRepository.ActualizarCantidadInventarioPrincipal(newCantInvPrin,invPrinCambiaCantidad.get().getIdinventario());
                            ventaRepository.save(ventas);
                        } else {
                            int diferencia2 = cantidadViejaVenta - cantidadNuevaVenta;
                            int nuevoStockTienda2 = invenTiendaCambiaStock.getStocktienda() + diferencia2;
                            inventarioTiendaRepository.ActualizarCantidadInventarioTienda(nuevoStockTienda2, invenTiendaCambiaStock.getIdiventariotienda());
                            Optional<Inventarioproducto> invPrinCambiaCantidad2
                                    = inventarioproductoRepository.findById(ventas.getInventariosede().getInventarioproductoidinventario().getIdinventario());
                            int newCantInvPrin2 = invPrinCambiaCantidad2.get().getCantidad()+diferencia2;
                            inventarioproductoRepository.ActualizarCantidadInventarioPrincipal(newCantInvPrin2,invPrinCambiaCantidad2.get().getIdinventario());
                            ventaRepository.save(ventas);
                        }
                    }

                }else{
                        //DARLE SU CANTIDAD A LA TIENDA
                        Inventariotienda invTiendaSumarCant =
                                inventarioTiendaRepository.productoEnTienda(ventas.getTienda().getIdtienda(), ventaVaACambiar.get().getInventariosede().getIdiventariosede());
                        int cantidadAnterVenta = ventaVaACambiar.get().getCantidad();
                        int NewTotalParatienda = cantidadAnterVenta + invTiendaSumarCant.getStocktienda();
                        inventarioTiendaRepository.ActualizarCantidadInventarioTienda(NewTotalParatienda,invTiendaSumarCant.getIdiventariotienda());
                        //DARLE SU CANTIDAD AL PRODUCTO DEL INVENTARIO
                        Optional<Inventarioproducto> invProductDevolverCant = inventarioproductoRepository.findById(ventaVaACambiar.get().getInventariosede().getInventarioproductoidinventario().getIdinventario());
                        int cantidadDevueltaTotal = invProductDevolverCant.get().getCantidad() + ventaVaACambiar.get().getCantidad();
                        inventarioproductoRepository.ActualizarCantidadInventarioPrincipal(cantidadDevueltaTotal,invProductDevolverCant.get().getIdinventario());

                        //CUANDO YA CAMBIASTE DE PRODUCTO
                        Inventariotienda inventariotiendaEditExiste = inventarioTiendaRepository.productoEnTienda(ventas.getTienda().getIdtienda(), ventas.getInventariosede().getIdiventariosede());
                        int cant = ventas.getCantidad();
                        Optional<Inventarioproducto> editProductoCantidad = inventarioproductoRepository.findById(ventas.getInventariosede().getInventarioproductoidinventario().getIdinventario());
                        int newCantInventProduct = editProductoCantidad.get().getCantidad() -cant;
                        inventarioproductoRepository.ActualizarCantidadInventarioPrincipal(newCantInventProduct,editProductoCantidad.get().getIdinventario());
                        if (inventariotiendaEditExiste.getIdiventariotienda()==0){
                            ventaRepository.save(ventas);
                        }else{
                            int cantTiendaEdit = inventariotiendaEditExiste.getStocktienda();
                            int newCantTienda = cantTiendaEdit-cant;
                            inventarioTiendaRepository.ActualizarCantidadInventarioTienda(newCantTienda,inventariotiendaEditExiste.getIdiventariotienda());
                            ventaRepository.save(ventas);
                        }
                    }
                }else{
                    redirectAttributes.addFlashAttribute("msg1","La cantidad Ingresa debe ser Mayor a Cero");
                    return "redirect:/venta/editar?id=" + ventas.getIdventas();
                }

            }
            redirectAttributes.addFlashAttribute("msgInfo", "Venta actualizada exitosamente.");
            return "redirect:/venta";
            }


    }


    @GetMapping("/editar")
    public String EditarLaventa(@RequestParam("id") int idVenta, @ModelAttribute Ventas ventas, RedirectAttributes att, Model model){
        Optional<Ventas> ventaqEdita = ventaRepository.findById(idVenta);
        if (ventaqEdita.isPresent()){
            ventas = ventaqEdita.get();
            model.addAttribute("ventas",ventas );
            Optional<Tienda> byId = tiendaRepository.findById(ventas.getTienda().getIdtienda());
            List<Inventariotienda> inventariotiendas = inventarioTiendaRepository.listaProductoEnTienda(ventas.getTienda().getIdtienda());
            model.addAttribute("ProductosEnTienda", inventariotiendas);
            model.addAttribute("tiendita",byId.get());
            return "venta/registroventa";
        }else{
            return "redirect:/venta";
        }

    }
}