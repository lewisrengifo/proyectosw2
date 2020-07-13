package com.example.demo.Controllers;

import com.example.demo.Entity.*;
import com.example.demo.Repository.*;
import com.example.demo.service.ConsignacionVentaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import javax.validation.ConstraintViolationException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/ConsignacionVenta")
public class ConsignacionventaController {

    @Autowired
    ConsignacionyventaRepository consignacionyventaRepository;
    @Autowired
    InventarioSedeRepository inventarioSedeRepository;
    @Autowired
    LineaRepository lineaRepository;
    @Autowired
    ProductoRepository productoRepository;
    @Autowired
    TamanoRepository tamanoRepository;
    @Autowired
    CategoriaRepository categoriaRepository;
    @Autowired
    InventarioproductoRepository inventarioproductoRepository;
    @Autowired
    ConsignacionVentaService consignacionVentaService;

    @GetMapping(value = {"/lista",""})
    public String listaConsignacionVenta(Model model,@ModelAttribute("consigYVenta") Consignacionyventa consigYventa){
        model.addAttribute("listaConsignacionVenta",consignacionyventaRepository.findAll());
        return "consigVenta/consignYventaLista";
    }

    @GetMapping("/agregarProductosCV")
    public String vistaagregarproductos(Model model, @ModelAttribute("inventarioProducto") Inventarioproducto invPro, @RequestParam("id") int id){

        Optional<Consignacionyventa> consigVentabyId = consignacionyventaRepository.findById(id);

        model.addAttribute("listalinea",lineaRepository.findAll());
        model.addAttribute("listaproducto",productoRepository.findAll());
        model.addAttribute("listacategoria",categoriaRepository.findAll());
        model.addAttribute("listatamano",tamanoRepository.findAll());

        model.addAttribute("consigYventa1",consigVentabyId.get());
        return "consigVenta/agregarProductos";
    }

    @PostMapping("/agregarProducto")
    public String agregarProductosEnPedido(Model model,
                                           @ModelAttribute("inventarioProducto") Inventarioproducto invPro, @RequestParam("idconsignacionVenta") int id, HttpSession session){

        Optional<Consignacionyventa> ultimaConsigOventa = consignacionyventaRepository.findById(id);
        invPro.setConsignacionyventa(ultimaConsigOventa.get());
        Date fechatudei = new Date();

        invPro.setFechainicio(fechatudei);
        if(ultimaConsigOventa.get().getTipo().equals("consignacion")){
            String lineac = invPro.getProducto().getLinea().getCodigolinea();
            String categoriac = invPro.getCategoria().getCodigocategoria();
            String productoc = invPro.getProducto().getCodigoproducto();
            String descriccionC = invPro.getProducto().getCodigodescripcionproducto();
            String tamano = invPro.getTamano().getCodigotamano();
            String comunidadC = invPro.getConsignacionyventa().getArtesano().getComunidad().getCodigocomunidad();
            String artesanoC = invPro.getConsignacionyventa().getArtesano().getCodigoartesano();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE");
            //OBTENER EL MES
            simpleDateFormat = new SimpleDateFormat("MMMM");
            String mesC= simpleDateFormat.format(invPro.getConsignacionyventa().getFechafin()).toUpperCase();
            //OBTENER EL AÑO
            simpleDateFormat = new SimpleDateFormat("YYYY");
            String yearco = simpleDateFormat.format(invPro.getConsignacionyventa().getFechafin()).toUpperCase();
            String totalCodigoGenerado = lineac+categoriac+productoc
                    +descriccionC+tamano+comunidadC+artesanoC+mesC+yearco;
            invPro.setCodigogenerado(totalCodigoGenerado);
        }else{
            String lineac = invPro.getProducto().getLinea().getCodigolinea();
            String categoriac = invPro.getCategoria().getCodigocategoria();
            String productoc = invPro.getProducto().getCodigoproducto();
            String descriccionC = invPro.getProducto().getCodigodescripcionproducto();
            String tamano = invPro.getTamano().getCodigotamano();
            String comunidadC = invPro.getConsignacionyventa().getArtesano().getComunidad().getCodigocomunidad();
            String totalCodigoGenerado = lineac+categoriac+productoc+descriccionC+tamano+comunidadC;
            invPro.setCodigogenerado(totalCodigoGenerado);
        }
        Usuario usuariologueado = (Usuario) session.getAttribute("usuario");

        Inventarioproducto invProductoUltimo = inventarioproductoRepository.save(invPro);

        Inventariosede inventariosede= new Inventariosede();
        inventariosede.setStock(invProductoUltimo.getCantidad());
        inventariosede.setFechallegada(fechatudei);
        inventariosede.setInventarioproductoidinventario(invProductoUltimo);
        inventariosede.setEstado("entregado");
        inventariosede.setSede(usuariologueado.getSede_idsede());
        inventarioSedeRepository.save(inventariosede);
        return "redirect:/ConsignacionVenta";
    }
    @GetMapping("/buscador")
    public String buscadorSearch(@RequestParam Map<String, Object> params, Model model) {

        String busqueda = (String) params.get("searchField");
        int page = params.get("page") != null ? (Integer.valueOf(params.get("page").toString()) - 1) : 0;

        Page<Consignacionyventa> paginaprincipal = consignacionVentaService.listSearch(busqueda, page);
        int totalPage = paginaprincipal.getTotalPages();
        long totalItems = paginaprincipal.getTotalElements();

        if (totalPage > 0) {
            List<Integer> pages = IntStream.rangeClosed(1, totalPage).boxed().collect(Collectors.toList());
            model.addAttribute("pages", pages);
        }
        model.addAttribute("totalItems", totalItems);
        model.addAttribute("busqueda", busqueda);
        model.addAttribute("listaConsignacionVenta", paginaprincipal.getContent());
        model.addAttribute("current", page + 1);
        model.addAttribute("next", page + 2);
        model.addAttribute("prev", page);
        model.addAttribute("last", totalPage);

        return "consigVenta/consignYventaLista";
    }

    /*
    @GetMapping("/borrar")
    public String borrarConsignacionVenta(Model model,
                                 @RequestParam("id") int id, RedirectAttributes att) {
        try {
            Optional<Consignacionyventa> optionalArtesano = consignacionyventaRepository.findById(id);
            if (optionalArtesano.isPresent()) {
                att.addFlashAttribute("msgAr", "Pedido borrado exitosamente");
                consignacionyventaRepository.deleteById(id);
                return "redirect:/ConsignacionVenta";
            }
            return "redirect:/ConsignacionVenta";
        }catch (ConstraintViolationException e){
            att.addFlashAttribute("msgAr", "Hay productos que poseen ese pedido");
            return "redirect:/ConsignacionVenta";
        }

    }*/

}
