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
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
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


    @GetMapping(value = {"/lista",""})
    public String listaConsignacionVenta(@RequestParam Map<String, Object> params, Model model, RedirectAttributes attr, @ModelAttribute("consigYVenta") Consignacionyventa consigYventa){


        try {
            int page = params.get("page") != null ? (Integer.valueOf(params.get("page").toString()) - 1) : 0;
        } catch (NumberFormatException e) {
            return "redirect:/ConsignacionVenta";
        }
        int page = params.get("page") != null ? (Integer.valueOf(params.get("page").toString()) - 1) : 0;

        if (page < 0) {
            return "redirect:/ConsignacionVenta";
        }

        PageRequest pageRequest = PageRequest.of(page, 5);

        Page<Consignacionyventa> pageConVent = consignacionyventaRepository.findAll(pageRequest);

        int totalPage = pageConVent.getTotalPages();
        if (totalPage > 0) {
            List<Integer> pages = IntStream.rangeClosed(1, totalPage).boxed().collect(Collectors.toList());
            if (page > pages.size() - 1) {
                attr.addFlashAttribute("msgPagina", "No se encuentran datos en esa página");

                return "redirect:/ConsignacionVenta";
            }
            model.addAttribute("pages", pages);
        } else {

            return "redirect:/ConsignacionVenta";
        }

        model.addAttribute("listaConsignacionVenta", pageConVent.getContent());
        model.addAttribute("current", page + 1);
        model.addAttribute("next", page + 2);
        model.addAttribute("prev", page);
        model.addAttribute("last", totalPage);
        return "consigVenta/consignYventaLista";


    }

    @GetMapping("/agregarProductosCV")
    public String vistaagregarproductos(Model model, @ModelAttribute("inventarioProducto") Inventarioproducto invPro, @RequestParam("id") String id){

        try{
            int idConVen = Integer.parseInt(id);;
            Optional<Consignacionyventa> consigVentabyId = consignacionyventaRepository.findById(idConVen);
            if(consigVentabyId.isPresent()){

                model.addAttribute("listalinea",lineaRepository.findAll());
                model.addAttribute("listaproducto",productoRepository.findAll());
                model.addAttribute("listacategoria",categoriaRepository.findAll());
                model.addAttribute("listatamano",tamanoRepository.findAll());

                model.addAttribute("consigYventa1",consigVentabyId.get());
                return "consigVenta/agregarProductos";
            }else{
                return "redirect:/ConsignacionVenta";
            }

        }catch (NumberFormatException e){
            return "redirect:/ConsignacionVenta";
        }




    }

    @PostMapping("/agregarProducto")
    public String agregarProductosEnPedido(Model model,@ModelAttribute("inventarioProducto") @Valid Inventarioproducto invPro, BindingResult bindingResult , @RequestParam("idconsignacionVenta") String id, HttpSession session){
        try{
            int idcoven = Integer.parseInt(id);
            Optional<Consignacionyventa> ultimaConsigOventa = consignacionyventaRepository.findById(idcoven);
            invPro.setConsignacionyventa(ultimaConsigOventa.get());
            if (bindingResult.hasErrors()) {
                    model.addAttribute("listalinea",lineaRepository.findAll());
                    model.addAttribute("listaproducto",productoRepository.findAll());
                    model.addAttribute("listacategoria",categoriaRepository.findAll());
                    model.addAttribute("listatamano",tamanoRepository.findAll());
                    model.addAttribute("consigYventa1",ultimaConsigOventa.get());
                    return "consigVenta/agregarProductos";
            } else {

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
                    String mesC = "";
                    if (invPro.getConsignacionyventa().getFechafin().getMonth() == 0) {
                        mesC = "ENE";
                    } else if (invPro.getConsignacionyventa().getFechafin().getMonth() == 1) {
                        mesC = "FEB";
                    } else if (invPro.getConsignacionyventa().getFechafin().getMonth() == 2) {
                        mesC = "MAR";
                    } else if (invPro.getConsignacionyventa().getFechafin().getMonth() == 3) {
                        mesC = "ABR";
                    } else if (invPro.getConsignacionyventa().getFechafin().getMonth() == 4) {
                        mesC = "MAY";
                    } else if (invPro.getConsignacionyventa().getFechafin().getMonth() == 5) {
                        mesC = "JUN";
                    } else if (invPro.getConsignacionyventa().getFechafin().getMonth() == 6) {
                        mesC = "JUL";
                    } else if (invPro.getConsignacionyventa().getFechafin().getMonth() == 7) {
                        mesC = "AGO";
                    } else if (invPro.getConsignacionyventa().getFechafin().getMonth() == 8) {
                        mesC = "SET";
                    } else if (invPro.getConsignacionyventa().getFechafin().getMonth() == 9) {
                        mesC = "OCT";
                    } else if (invPro.getConsignacionyventa().getFechafin().getMonth() == 10) {
                        mesC = "NOV";
                    } else if (invPro.getConsignacionyventa().getFechafin().getMonth() == 11) {
                        mesC = "DIC";
                    }
                    //OBTENER EL AÑO
                    simpleDateFormat = new SimpleDateFormat("YYYY");
                    String yearco = simpleDateFormat.format(invPro.getConsignacionyventa().getFechafin()).toUpperCase();
                    char[] yearchar = yearco.toCharArray();
                    String totalCodigoGenerado = lineac + categoriac + productoc
                            + descriccionC + tamano + comunidadC + artesanoC + mesC + yearchar[2] + yearchar[3];
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

                Inventariosede inventariosede = new Inventariosede();
                inventariosede.setStock(invProductoUltimo.getCantidad());
                inventariosede.setFechallegada(fechatudei);
                inventariosede.setInventarioproductoidinventario(invProductoUltimo);
                inventariosede.setEstado("recibido");
                inventariosede.setSede(usuariologueado.getSede_idsede());
                inventarioSedeRepository.save(inventariosede);
                return "redirect:/ConsignacionVenta";
            }

        }catch (NumberFormatException e){
            return "redirect:/ConsignacionVenta";
        }
    }
    @GetMapping("/buscador1")
    public String buscadorSearch1(@RequestParam Map<String, Object> params, Model model,RedirectAttributes attr) {
        String busqueda = (String) params.get("searchField");

        if (busqueda.isEmpty()) {
            attr.addFlashAttribute("msgBuscador", "Campo vacio. Ingrese el dato a buscar");

            return "redirect:/ConsignacionVenta/lista";
        } else {

            try {
                int page = params.get("page") != null ? (Integer.valueOf(params.get("page").toString()) - 1) : 0;
            } catch (NumberFormatException e) {
                return "redirect:/ConsignacionVenta/lista";
            }


            int page = params.get("page") != null ? (Integer.valueOf(params.get("page").toString()) - 1) : 0;


            PageRequest pageRequest = PageRequest.of(page, 10);


            Page<Consignacionyventa> pageInvConVen = consignacionyventaRepository.buscadorConsignacionesYVentas(busqueda, pageRequest);
            int totalPage = pageInvConVen.getTotalPages();
            if (totalPage > 0) {
                List<Integer> pages = IntStream.rangeClosed(1, totalPage).boxed().collect(Collectors.toList());
                if (page > pages.size() - 1) {
                    attr.addFlashAttribute("msgPagina", "No se encuentran datos en esa página");

                    return "redirect:/ConsignacionVenta/lista";
                }
                model.addAttribute("pages", pages);
            }else{
                attr.addFlashAttribute("msgPagina", "No se encuentran datos en esa página");

                return "redirect:/ConsignacionVenta/lista";

            }

            model.addAttribute("listaConsignacionVenta", pageInvConVen.getContent());
            model.addAttribute("current", page + 1);
            model.addAttribute("next", page + 2);
            model.addAttribute("busqueda", busqueda);
            model.addAttribute("prev", page);
            model.addAttribute("last", totalPage);

            return "consigVenta/consignYventaLista";
        }
    }

    @GetMapping("/buscador")
    public String buscadorSearch(@RequestParam Map<String, Object> params, Model model , RedirectAttributes attr ) {

        String busqueda = (String) params.get("searchField");
        if (busqueda.isEmpty()) {
            attr.addFlashAttribute("msgBuscador", "Campo vacio. Ingrese el dato a buscar");

            return "redirect:/ConsignacionVenta/lista";
        } else {

            try {
                int page = params.get("page") != null ? (Integer.valueOf(params.get("page").toString()) - 1) : 0;
            } catch (NumberFormatException e) {
                return "redirect:/ConsignacionVenta/lista";
            }


            int page = params.get("page") != null ? (Integer.valueOf(params.get("page").toString()) - 1) : 0;

            PageRequest pageRequest = PageRequest.of(page, 5);


            Page<Consignacionyventa> pageConsgVenta = consignacionyventaRepository.buscadorConsignacionesYVentas(busqueda, pageRequest);
            int totalPage = pageConsgVenta.getTotalPages();
            if (totalPage > 0) {
                List<Integer> pages = IntStream.rangeClosed(1, totalPage).boxed().collect(Collectors.toList());
                if (page > pages.size() - 1) {
                    attr.addFlashAttribute("msgPagina", "No se encuentran datos en esa página");

                    return "redirect:/ConsignacionVenta/lista";
                }
                model.addAttribute("pages", pages);
            } else {
                attr.addFlashAttribute("msgPagina", "No se encuentran datos en esa página");

                return "redirect:/ConsignacionVenta/lista";


            }

            model.addAttribute("listaConsignacionVenta", pageConsgVenta.getContent());
            model.addAttribute("current", page + 1);
            model.addAttribute("next", page + 2);
            model.addAttribute("busqueda", busqueda);
            model.addAttribute("prev", page);
            model.addAttribute("last", totalPage);
            return "consigVenta/consignYventaLista";
        }
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
